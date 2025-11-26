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

import static java.nio.file.Files.exists;

/**
 * Walks an expanded file-system-based xRegistry. Expanded form consists of a directory structure containing versioned resources:
 * <p>
 * {@code [group type]/[group name]/[resource type]/[resource name]/[versions]/(1..N).(ext)}
 * <p>
 * The following resource types are supported:
 * <ul>
 * <li>POLICY - under /policygroups</li>
 * <li>SCHEMA - under /schemagroups</li>
 * <li>RULE - under /rulegroups</li>
 * </ul>
 */
public class ExpandedFileSystemWalker extends AbstractFileSystemWalker {
    private static final String VERSIONS = "versions";

    public ExpandedFileSystemWalker(XRegistryVisitor visitor) {
        super(visitor);
    }

    protected void processPath(ArtifactType type, Path rootPath) {
        var policyGroupPath = rootPath.resolve(type.groupName());
        if (exists(policyGroupPath)) {
            try (var paths = Files.list(policyGroupPath)) {
                paths.filter(Files::isDirectory)
                        .forEach(groupPath -> processGroup(type, groupPath));
            } catch (IOException e) {
                visitor.onError(policyGroupPath.toString());
            }
        }
    }

    private void processGroup(ArtifactType type, Path groupPath) {
        var resourcesPath = groupPath.resolve(type.resourcesName());
        try (var resourcePath = Files.list(resourcesPath)) {
            resourcePath.filter(Files::isDirectory)
                    .forEach(p -> processResource(type, groupPath.getFileName().toString(), p));
        } catch (IOException e) {
            visitor.onError(resourcesPath + ":" + e.getMessage());
        }
    }

    private void processResource(ArtifactType type, String group, Path resourcePath) {
        var versionsPath = resourcePath.resolve(VERSIONS);
        try (var version = Files.list(versionsPath)) {
            version.filter(Files::isRegularFile)
                    .forEach(p -> processResourceVersion(type, group, resourcePath.toFile().getName(), p));
        } catch (IOException e) {
            visitor.onError(versionsPath + ":" + e.getMessage());
        }
    }

    private void processResourceVersion(ArtifactType type, String group, String name, Path filePath) {
        processFile(type, new Artifact(group, name, filePath.toFile().getName()), filePath);
    }

}