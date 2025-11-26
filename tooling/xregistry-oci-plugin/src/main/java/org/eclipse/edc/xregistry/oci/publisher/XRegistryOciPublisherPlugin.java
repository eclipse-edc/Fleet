
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

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.bundling.Tar;
import org.jspecify.annotations.NonNull;

import static org.eclipse.edc.xregistry.oci.publisher.Constants.GRADLE_TASK_GROUP;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.PLUGIN_EXTENSION_NAME;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.PLUGIN_PARAM_ARTIFACT_NAME;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.PLUGIN_PARAM_ARTIFACT_VERSION;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.PLUGIN_PARAM_OCI_ARTIFACT_NAME;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.PLUGIN_PARAM_OCI_ARTIFACT_TAG;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.PLUGIN_PARAM_SOURCE_DIR;
import static java.util.Objects.requireNonNullElseGet;

/**
 * A plugin responsible for publishing xRegistry artifacts as OCI distributions.
 */
public class XRegistryOciPublisherPlugin implements Plugin<@NonNull Project> {
    public static final String BUILD_X_REGISTRY_TASK = "buildXRegistryOciPublish";

    private static final String ARTIFACT_SUFFIX = "-xregistry";

    @Override
    public void apply(Project project) {
        var extension = project.getExtensions().create(PLUGIN_EXTENSION_NAME, XRegistryOciPublisherExtension.class);
        // create lazy providers for configuration values
        var sourceLocationProvider = createSourceLocationProvider(project, extension);
        var artifactNameProvider = createArtifactNameProvider(project);
        var ociArtifactProvider = createOciArtifactProvider(project, extension);

        createTasks(project, sourceLocationProvider, artifactNameProvider, ociArtifactProvider, extension);
    }

    private Provider<String> createOciArtifactProvider(Project project, XRegistryOciPublisherExtension extension) {
        return project.provider(() -> {
            var name = (String) project.getProperties().get(PLUGIN_PARAM_OCI_ARTIFACT_NAME);
            if (name == null) {
                // Check if the property is present before calling .get()
                if (!extension.getOciArtifactName().isPresent()) {
                    throw new GradleException("ociArtifactName must be configured in the xRegistryOciPublisher extension or provided as a project property");
                }
                name = extension.getOciArtifactName().get();
            }
            var tag = (String) project.findProperty(PLUGIN_PARAM_OCI_ARTIFACT_TAG);
            if (tag == null) {
                if (!extension.getOciArtifactTag().isPresent()) {
                    throw new GradleException("ociArtifactTag must be configured in the xRegistryOciPublisher extension or provided as a project property");
                }
                tag = extension.getOciArtifactTag().get();
            }
            return name + ":" + tag;
        });
    }

    private Provider<String> createSourceLocationProvider(Project project, XRegistryOciPublisherExtension extension) {
        return project.provider(() -> {
            var customSourceDir = (String) project.findProperty(PLUGIN_PARAM_SOURCE_DIR);
            var sourceFile = project.getLayout().getProjectDirectory()
                    .file(requireNonNullElseGet(customSourceDir, () -> extension.getXRegistrySourceDir().get()))
                    .getAsFile();

            if (!sourceFile.exists()) {
                throw new GradleException(
                        "XRegistry source directory does not exist: " + sourceFile.getAbsolutePath() +
                        ". Specify a valid source directory using the '" + PLUGIN_PARAM_SOURCE_DIR + "' property."
                );
            }

            if (!sourceFile.isDirectory()) {
                throw new GradleException("XRegistry source path is not a directory: " + sourceFile.getAbsolutePath());
            }

            return sourceFile.getAbsolutePath();
        });
    }

    private void createTasks(Project project,
                             Provider<String> sourceLocationProvider,
                             Provider<String> artifactNameProvider,
                             Provider<String> ociArtifactProvider,
                             XRegistryOciPublisherExtension extension) {

        var packageArtifact = project.getTasks()
                .register(PackageArtifactAction.TASK_NAME, Tar.class, task -> {
                    new PackageArtifactAction(project, artifactNameProvider, sourceLocationProvider).execute(task);
                });

        var publishArtifact = project.getTasks()
                .register(PublishToRegistryAction.TASK_NAME, task -> {
                    new PublishToRegistryAction(project, artifactNameProvider, ociArtifactProvider, extension).execute(task);
                    task.dependsOn(packageArtifact);
                });

        project.getTasks().register(BUILD_X_REGISTRY_TASK, task -> {
            task.setDescription("Builds and publishes an xRegistry OCI artifact");
            task.setGroup(GRADLE_TASK_GROUP);
            task.dependsOn(publishArtifact);
        });
    }

    private Provider<String> createArtifactNameProvider(Project project) {
        return project.provider(() -> {
            var artifactName = (String) project.findProperty(PLUGIN_PARAM_ARTIFACT_NAME);
            if (artifactName == null) {
                artifactName = project.getName() + ARTIFACT_SUFFIX;
            }

            var version = (String) project.findProperty(PLUGIN_PARAM_ARTIFACT_VERSION);
            if (version == null) {
                version = project.getVersion().toString();
            }
            return artifactName + "-" + version + ".tar";
        });
    }

}