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

import org.jetbrains.annotations.Nullable;

/**
 * Parses compact file names
 */
public class FileParser {
    private static final int TOKEN_COUNT = 4;  // group, resource name, version, extension
    private static final int GROUP = 0;
    private static final int RESOURCE_NAME = 1;

    /**
     * Parse a filename in the format "group.resource-name.version.extension" and return the artifact.
     *
     * @param filename the filename to parse
     * @return Artifact or null if format is invalid
     */
    @Nullable
    public static Artifact parseFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }

        var tokens = filename.split("\\.");
        if (tokens.length < TOKEN_COUNT) {
            return null; // Need at least group.resource.version.extension
        }

        var group = tokens[GROUP];
        var resourceName = tokens[RESOURCE_NAME];

        // everything from third token to second-to-last token is the version
        var version = new StringBuilder();
        for (var i = 2; i < tokens.length - 1; i++) {
            if (i > 2) {
                version.append(".");
            }
            version.append(tokens[i]);
        }
        return new Artifact(group, resourceName, version.toString());
    }
}
