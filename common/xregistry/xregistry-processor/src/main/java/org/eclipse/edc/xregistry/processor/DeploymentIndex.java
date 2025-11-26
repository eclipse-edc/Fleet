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

package org.eclipse.edc.xregistry.processor;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * An index of xRegistry supported artifacts.
 */
public record DeploymentIndex(
        Map<Artifact, Supplier<InputStream>> artifacts,
        Map<Artifact, Supplier<InputStream>> policies,
        Map<Artifact, Supplier<InputStream>> schemas,
        Map<Artifact, Supplier<InputStream>> rules) {

    public DeploymentIndex {
        requireNonNull(artifacts, "artifacts cannot be null");
        requireNonNull(policies, "policies cannot be null");
        requireNonNull(schemas, "schemas cannot be null");
        requireNonNull(rules, "rules cannot be null");
    }

    public Supplier<InputStream> findArtifact(Artifact artifact) {
        return artifacts.get(artifact);
    }

}
