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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.eclipse.edc.xregistry.processor.FileParser.parseFilename;
import static java.nio.file.Files.exists;

/**
 * Walks a file-system-based xRegistry that is in compact format.
 * <p>
 * Compact format is a directory structure with files in the format "group.resource-name.version.extension".
 * The following resource types are supported:
 * <ul>
 * <li>POLICY - under /policies</li>
 * <li>SCHEMA - under /schemas</li>
 * <li>RULE - under /rules</li>
 * </ul>
 */
public class CompactFileSystemWalker extends AbstractFileSystemWalker {

    public CompactFileSystemWalker(XRegistryVisitor visitor) {
        super(visitor);
    }

    protected void processPath(ArtifactType type, Path rootPath) {
        var resourcePath = rootPath.resolve(type.resourcesName());
        if (exists(resourcePath)) {
            try (var paths = Files.list(resourcePath)) {
                paths.filter(Files::isRegularFile)
                        .forEach(filePath -> processFile(type, filePath));
            } catch (IOException e) {
                visitor.onError(resourcePath.toString());
            }
        }
    }

    private void processFile(ArtifactType type, Path filePath) {
        var artifact = parseFilename(filePath.getFileName().toString());
        if (artifact == null) {
            return;
        }
        processFile(type, artifact, filePath);
    }

}
