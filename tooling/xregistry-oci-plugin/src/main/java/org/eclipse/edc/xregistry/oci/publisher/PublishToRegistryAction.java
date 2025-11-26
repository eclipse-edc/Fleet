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

import land.oras.Annotations;
import land.oras.ArtifactType;
import land.oras.ContainerRef;
import land.oras.LocalPath;
import land.oras.Registry;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

import static org.eclipse.edc.xregistry.oci.publisher.Constants.DISTRIBUTIONS;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.GRADLE_TASK_GROUP;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.XREGISTRY_CONTENT_TYPE;
import static java.util.Collections.emptyMap;

/**
 * Publishes the packaged OCI artifact to a container registry.
 */
public class PublishToRegistryAction implements Action<Task> {
    public static final String TASK_NAME = "publishOciArtifact";

    private final Project project;
    private final Provider<String> artifactNameProvider;
    private final Provider<String> ociArtifactProvider;
    private final XRegistryOciPublisherExtension extension;

    public PublishToRegistryAction(Project project,
                                   Provider<String> artifactNameProvider,
                                   Provider<String> ociArtifactProvider,
                                   XRegistryOciPublisherExtension extension) {
        this.project = project;
        this.artifactNameProvider = artifactNameProvider;
        this.ociArtifactProvider = ociArtifactProvider;
        this.extension = extension;
    }

    @Override
    public void execute(Task task) {
        task.setDescription("Publishes an xRegistry archive to a container registry");
        task.setGroup(GRADLE_TASK_GROUP);
        task.doLast(t -> publishArtifact());
    }

    private void publishArtifact() {
        try {
            var ref = ContainerRef.parse(ociArtifactProvider.get());
            var type = ArtifactType.from(XREGISTRY_CONTENT_TYPE);

            var distro = project.getLayout().getBuildDirectory().dir(DISTRIBUTIONS).get().file(artifactNameProvider.get());
            var path = LocalPath.of(distro.toString());

            var registryBuilder = Registry.builder();

            if (extension.getInsecure().get()) {
                registryBuilder.insecure();
            }

            var username = extension.getOciRegistryUsername().getOrNull();
            var password = extension.getOciRegistryPassword().getOrNull();
            if (username != null && password != null) {
                registryBuilder.defaults(username, password);
            }

            var registry = registryBuilder.build();

            // add annotations from plugin configuration
            var customAnnotations = extension.getManifestAnnotations().getOrElse(emptyMap());
            var annotations = Annotations.ofManifest(customAnnotations);

            registry.pushArtifact(ref, type, annotations, path);
        } catch (Exception e) {
            throw new GradleException("Failed to publish xRegistry archive", e);
        }
    }
}