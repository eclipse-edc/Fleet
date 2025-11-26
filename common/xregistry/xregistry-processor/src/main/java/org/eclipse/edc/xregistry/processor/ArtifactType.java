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

/**
 * Supported xRegistry artifact types.
 */
public enum ArtifactType {
    POLICY("policygroups", "policies"),
    SCHEMA("schemagroups", "schemas"),
    RULE("rulegroups", "rules");

    private final String groupName;
    private final String resourcesName;

    ArtifactType(String groupName, String resourcesName) {
        this.groupName = groupName;
        this.resourcesName = resourcesName;
    }

    String groupName() {
        return groupName;
    }

    String resourcesName() {
        return resourcesName;
    }
}
