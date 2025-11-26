
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

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.bundling.Tar;

import static org.eclipse.edc.xregistry.oci.publisher.Constants.DISTRIBUTIONS;
import static org.eclipse.edc.xregistry.oci.publisher.Constants.GRADLE_TASK_GROUP;
import static org.gradle.api.tasks.bundling.Compression.GZIP;

/**
 * Packages the file-system xRegistry as a tar archive for distribution.
 */
public class PackageArtifactAction implements Action<Tar> {
    public static final String TASK_NAME = "packageOciArtifact";

    private final Project project;
    private final Provider<String> artifactNameProvider;
    private final Provider<String> sourceLocationProvider;

    public PackageArtifactAction(Project project,
                                 Provider<String> artifactNameProvider,
                                 Provider<String> sourceLocationProvider) {
        this.project = project;
        this.artifactNameProvider = artifactNameProvider;
        this.sourceLocationProvider = sourceLocationProvider;
    }

    @Override
    public void execute(Tar task) {
        task.setDescription("Packages an xRegistry archive for distribution");

        var distributionDir = project.getLayout().getBuildDirectory().dir(DISTRIBUTIONS).get().getAsFile();
        distributionDir.mkdirs();

        task.setGroup(GRADLE_TASK_GROUP);
        task.setCompression(GZIP);
        task.getArchiveFileName().set(artifactNameProvider);
        task.from(project.getLayout().getBuildDirectory().dir(sourceLocationProvider));
        task.getDestinationDirectory().set(distributionDir);
    }
}