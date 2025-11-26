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

import org.gradle.api.Project;
import org.gradle.api.tasks.bundling.Tar;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.eclipse.edc.xregistry.oci.publisher.Constants.PLUGIN_PARAM_OCI_ARTIFACT_NAME;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.PLUGIN_PARAM_OCI_ARTIFACT_TAG;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.XREGISTRY_SOURCE_DIR;
import static org.eclipse.edc.xregistry.oci.publisher.TestConstants.PLUGIN_ID;
import static org.eclipse.edc.xregistry.oci.publisher.XRegistryOciPublisherPlugin.BUILD_X_REGISTRY_TASK;
import static java.nio.file.Files.write;
import static org.assertj.core.api.Assertions.assertThat;

class XRegistryOciPublisherPluginTest {

    @TempDir
    Path tempDir;

    private Project project;

    @Test
    void testTaskConfiguration() throws IOException {
        project.getPluginManager().apply(PLUGIN_ID);

        project.getExtensions().getExtraProperties().set(PLUGIN_PARAM_OCI_ARTIFACT_NAME, "ghcr.io/metaform/xr-sample");
        project.getExtensions().getExtraProperties().set(PLUGIN_PARAM_OCI_ARTIFACT_TAG, "1.0");

        var sourceDir = new File(project.getProjectDir(), XREGISTRY_SOURCE_DIR);
        sourceDir.mkdirs();
        write(new File(sourceDir, "test-schema.json").toPath(), "test".getBytes());

        var packageTask = (Tar) project.getTasks().findByName(PackageArtifactAction.TASK_NAME);
        var publishTask = project.getTasks().findByName(PublishToRegistryAction.TASK_NAME);
        var buildTask = project.getTasks().findByName(BUILD_X_REGISTRY_TASK);

        assertThat(packageTask).isNotNull();
        assertThat(publishTask).isNotNull();
        assertThat(buildTask).isNotNull();

        assertThat(packageTask.getArchiveFileName().get()).endsWith(".tar");

        assertThat(publishTask.getTaskDependencies().getDependencies(publishTask))
                .anySatisfy(task -> assertThat(task.getName()).isEqualTo(PackageArtifactAction.TASK_NAME));
        assertThat(buildTask.getTaskDependencies().getDependencies(buildTask))
                .anySatisfy(task -> assertThat(task.getName()).isEqualTo(PublishToRegistryAction.TASK_NAME));
    }

    @BeforeEach
    void setUp() {
        project = ProjectBuilder.builder().withProjectDir(tempDir.toFile()).build();
        project.setVersion("1.0.0");
    }


}