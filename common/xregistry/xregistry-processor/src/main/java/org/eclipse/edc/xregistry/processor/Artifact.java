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

import static java.util.Objects.requireNonNull;

/**
 * Represents an xRegistry artifact.
 */
public record Artifact(String group, String name, String version) {

    public Artifact {
        requireNonNull(group, "group cannot be null");
        requireNonNull(name, "name cannot be null");
        requireNonNull(version, "version cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Artifact artifact)) return false;

        return name.equals(artifact.name()) && group.equals(artifact.group()) && version.equals(artifact.version());
    }

    @Override
    public int hashCode() {
        int result = group.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }
}
