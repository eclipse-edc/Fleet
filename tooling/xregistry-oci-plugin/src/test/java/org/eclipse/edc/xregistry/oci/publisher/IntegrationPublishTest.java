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
import org.gradle.api.Project;
import org.gradle.api.tasks.bundling.Tar;
import org.gradle.testfixtures.ProjectBuilder;
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

import static org.eclipse.edc.xregistry.oci.publisher.Constants.PLUGIN_PARAM_OCI_ARTIFACT_NAME;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.PLUGIN_PARAM_OCI_ARTIFACT_TAG;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.XREGISTRY_CONTENT_TYPE;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.XREGISTRY_SOURCE_DIR;
import static org.eclipse.edc.xregistry.oci.publisher.TestConstants.PLUGIN_ID;
import static java.lang.String.format;
import static java.nio.file.Files.write;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Performs an end-to-end integration test of the xRegistry OCI publisher plugin.
 * <p></p>
 * Deploys a local <a href="https://zotregistry.dev/">Zot Registry</a> for testing.
 */
@Testcontainers
public class IntegrationPublishTest {
    private static final String ARTIFACT_TEMPLATE = "localhost:%s/metaform/xr-sample";

    private static final byte[] CONTENT = """ 
            {"key": "value""}
            """.getBytes();

    private static final int ZOT_PORT = 5000;

    private static final String ZOT_USERNAME = "zot";
    private static final String ZOT_PASSWORD = "zot";

    private static final String ZOT_IMAGE = "registry:2";
    private static final String OCI_ARTIFACT_TAG = "1.0";

    private static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(60);

    @Container
    @SuppressWarnings("resource")
    static GenericContainer<?> zotRegistry = new GenericContainer<>(DockerImageName.parse(ZOT_IMAGE))
            .withExposedPorts(ZOT_PORT)
            .withStartupCheckStrategy(new IsRunningStartupCheckStrategy())
            .waitingFor(Wait.forListeningPort().withStartupTimeout(STARTUP_TIMEOUT));

    @TempDir
    Path tempDir;

    private Project project;

    @Test
    void test_ArtifactPublished() {
        var port = zotRegistry.getMappedPort(ZOT_PORT);

        var artifact = format(ARTIFACT_TEMPLATE, port);
        project.getExtensions().getExtraProperties().set(PLUGIN_PARAM_OCI_ARTIFACT_NAME, artifact);
        project.getExtensions().getExtraProperties().set(PLUGIN_PARAM_OCI_ARTIFACT_TAG, OCI_ARTIFACT_TAG);

        project.getPluginManager().apply(PLUGIN_ID);
        var extension = project.getExtensions().getByType(XRegistryOciPublisherExtension.class);
        extension.getOciRegistryUsername().set(ZOT_USERNAME);
        extension.getOciRegistryPassword().set(ZOT_PASSWORD);
        extension.getInsecure().set(true);  // HTTP localhost registry
        extension.getManifestAnnotations().set(java.util.Map.of("custom.annotation", "value"));

        var packageTask = (Tar) project.getTasks().findByName(PackageArtifactAction.TASK_NAME);
        assertThat(packageTask).isNotNull();

        var result = project.getTasks().getByName(PackageArtifactAction.TASK_NAME);
        result.getActions().forEach(action -> action.execute(result));

        // verify the tar file
        var distributionsDir = new File(project.getLayout().getBuildDirectory().get().getAsFile(), "distributions");
        assertThat(distributionsDir).exists();
        var tarFiles = distributionsDir.listFiles((dir, name) -> name.endsWith(".tar"));
        assertThat(tarFiles).isNotEmpty();
        assertThat(tarFiles[0].getName()).contains("xregistry");

        // execute the publish task
        var publishTask = project.getTasks().findByName(PublishToRegistryAction.TASK_NAME);
        assertThat(publishTask).isNotNull();
        publishTask.getActions().forEach(action -> action.execute(publishTask));

        // verify manifest exists
        var registry = Registry.builder()
                .insecure()
                .defaults(ZOT_USERNAME, ZOT_PASSWORD)
                .build();
        var ref = ContainerRef.parse(artifact + ":" + OCI_ARTIFACT_TAG);

        var manifest = registry.getManifest(ref);
        assertThat(manifest).isNotNull();
        assertThat(manifest.getArtifactType().getMediaType()).isEqualTo(XREGISTRY_CONTENT_TYPE);
        assertThat(manifest.getAnnotations()).containsEntry("custom.annotation", "value");
    }

    @BeforeEach
    void setUp() throws IOException {
        project = ProjectBuilder.builder().withProjectDir(tempDir.toFile()).build();
        project.setVersion("1.0.0");

        // setup artifact
        var sourceDir = new File(project.getProjectDir(), XREGISTRY_SOURCE_DIR);
        sourceDir.mkdirs();
        write(new File(sourceDir, "test-schema.json").toPath(), CONTENT);

        var ociLayoutDir = new File(project.getLayout().getBuildDirectory().get().getAsFile(), "oci-layout");
        ociLayoutDir.mkdirs();
        write(new File(ociLayoutDir, "index.json").toPath(), "{}".getBytes());
    }
}
