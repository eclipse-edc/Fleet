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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.newInputStream;

/**
 * Base walker implementation.
 */
public abstract class AbstractFileSystemWalker {
    protected final XRegistryVisitor visitor;

    public AbstractFileSystemWalker(XRegistryVisitor visitor) {
        this.visitor = visitor;
    }

    /**
     * Walk the file system starting from the given root path.
     *
     * @param rootPath the root directory to start walking from
     */
    public void walk(Path rootPath) {
        visitor.onStart();

        try {
            if (!exists(rootPath)) {
                visitor.onError("Path does not exist: " + rootPath);
                return;
            }

            processPath(ArtifactType.SCHEMA, rootPath);
            processPath(ArtifactType.RULE, rootPath);
            processPath(ArtifactType.POLICY, rootPath);

            visitor.onComplete();
        } catch (Exception e) {
            visitor.onError(rootPath + ":" + e.getMessage());
        }
    }

    protected abstract void processPath(ArtifactType type, Path rootPath);

    protected void processFile(ArtifactType type, Artifact artifact, Path filePath) {
        switch (type) {
            case POLICY -> visitor.onPolicy(artifact, createSupplier(filePath));
            case SCHEMA -> visitor.onSchema(artifact, createSupplier(filePath));
            case RULE -> visitor.onRule(artifact, createSupplier(filePath));
        }
    }

    private @NotNull Supplier<InputStream> createSupplier(Path filePath) {
        return () -> {
            try {
                return newInputStream(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
