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

import java.io.File;

/**
 * Plugin constants.
 */
public interface Constants {
    String XREGISTRY_CONTENT_TYPE = "application/vnd.dspace.xregistry.v1+1";

    String GRADLE_TASK_GROUP = "oci";

    String DISTRIBUTIONS = "distributions";

    String PLUGIN_EXTENSION_NAME = "xRegistryOciPublisher";

    String XREGISTRY_SOURCE_DIR = "src" + File.separator + "main" + File.separator + "xregistry";

    String PLUGIN_PARAM_SOURCE_DIR = "xRegistrySourceDir";
    String PLUGIN_PARAM_ARTIFACT_NAME = "artifactName";
    String PLUGIN_PARAM_ARTIFACT_VERSION = "artifactVersion";
    String PLUGIN_PARAM_OCI_ARTIFACT_NAME = "ociArtifactName";
    String PLUGIN_PARAM_OCI_ARTIFACT_TAG = "ociArtifactTag";

}