/*
 *  Copyright (c) 2025 Metaform Systems, Inc.
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Metaform Systems, Inc. - initial API and implementation
 *
 */

package org.eclipse.edc.xregistry.oci.publisher;

import land.oras.ContainerRef;
import land.oras.Registry;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.startupcheck.IsRunningStartupCheckStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

import static java.lang.String.format;
import static java.nio.file.Files.writeString;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Performs end-to-end publishing to a Zot registry from a Gradle build.
 */
@Testcontainers
public class XRegistryOciPublisherPluginFunctionalTest {
    private static final String ARTIFACT_TEMPLATE = "localhost:%s/metaform/xr-sample";
    private static final String OCI_ARTIFACT_TAG = "1.0";
    private static final String XREGISTRY_CONTENT_TYPE = "application/vnd.dspace.xregistry.v1+1";
    private static final int ZOT_PORT = 5000;
    private static final String ZOT_IMAGE = "registry:2";

    private static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(60);
    private static final String ZOT_USERNAME = "zot";
    private static final String ZOT_PASSWORD = "zot";

    @TempDir
    private Path tempDir;

    private File projectDir;
    private String artifact;

    @Container
    @SuppressWarnings("resource")
    static GenericContainer<?> zotRegistry = new GenericContainer<>(DockerImageName.parse(ZOT_IMAGE))
            .withExposedPorts(ZOT_PORT)
            .withStartupCheckStrategy(new IsRunningStartupCheckStrategy())
            .waitingFor(Wait.forListeningPort().withStartupTimeout(STARTUP_TIMEOUT));

    @Test
    void test_PublishArtifact() {

        GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments("tasks", "buildXRegistryOciPublish")
                .withPluginClasspath()
                .build();

        // verify manifest exists
        var registry = Registry.builder()
                .insecure()
                .defaults(ZOT_USERNAME, ZOT_PASSWORD)
                .build();

        var ref = ContainerRef.parse(artifact + ":" + OCI_ARTIFACT_TAG);

        var manifest = registry.getManifest(ref);

        assertThat(manifest).isNotNull();
        assertThat(manifest.getArtifactType().getMediaType()).isEqualTo(XREGISTRY_CONTENT_TYPE);
    }

    @BeforeEach
    void setup() throws IOException {
        projectDir = tempDir.toFile();
        var buildFile = new File(projectDir, "build.gradle.kts");
        var settingsFile = new File(projectDir, "settings.gradle.kts");
        var xRegistryDir = projectDir.toPath().resolve("src").resolve("main").resolve("xregistry").toFile();
        xRegistryDir.mkdirs();

        var port = zotRegistry.getMappedPort(ZOT_PORT);
        artifact = format(ARTIFACT_TEMPLATE, port);

        // create build.gradle.kts with plugin applied
        writeString(buildFile.toPath(), format("""
                import org.eclipse.edc.fleet.xregistry.oci.publisher.XRegistryOciPublisherExtension
                import java.time.Instant
                
                plugins {
                    base
                    id("org.eclipse.edc.xregistry-oci-publisher") version "1.0"
                }
                
                repositories {
                    mavenCentral()
                    mavenLocal()
                }
                
                configure<XRegistryOciPublisherExtension> {
                    xRegistrySourceDir.set("src/main/xregistry")
                
                    ociArtifactName.set("%s")
                    ociArtifactTag.set("1.0")
                
                    // Registry authentication
                    ociRegistryUsername.set("zot")
                    ociRegistryPassword.set("zot")
                    insecure.set(true)
                
                    // Custom manifest annotations
                    manifestAnnotations.set(
                        mapOf(
                            "org.opencontainers.image.title" to "xRegistry Policy Bundle",
                            "org.opencontainers.image.description" to "Fleet management policies",
                            "org.opencontainers.image.version" to project.version.toString(),
                            "org.opencontainers.image.created" to Instant.now().toString(),
                            "org.opencontainers.image.authors" to "ACME, Inc."
                        )
                    )
                }
                
                project.defaultTasks("buildXRegistryOci")
                """, artifact));

        writeString(new File(xRegistryDir, "test.json").toPath(), """
                    {
                        "type": "object",
                        "properties": {
                            "name": {"type": "string"}
                        }
                    }
                """);

        // create settings.gradle.kts
        writeString(settingsFile.toPath(), """
                    rootProject.name = "test-project"
                """);
    }

}
