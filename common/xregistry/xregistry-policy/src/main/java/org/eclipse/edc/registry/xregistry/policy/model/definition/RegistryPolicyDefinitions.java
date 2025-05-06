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

package org.eclipse.edc.registry.xregistry.policy.model.definition;

import org.eclipse.edc.registry.xregistry.model.definition.AttributeDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.GroupDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.ValueType;
import org.eclipse.edc.registry.xregistry.model.definition.VersionDefinition;

import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.BOOLEAN;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.STRING;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.XID;
import static org.eclipse.edc.registry.xregistry.policy.model.PolicyConstants.ACCESS_POLICY;
import static org.eclipse.edc.registry.xregistry.policy.model.PolicyConstants.CONTROL_POLICY;
import static org.eclipse.edc.registry.xregistry.policy.model.PolicyConstants.GROUPS_NAME;
import static org.eclipse.edc.registry.xregistry.policy.model.PolicyConstants.GROUP_NAME;
import static org.eclipse.edc.registry.xregistry.policy.model.PolicyConstants.POLICY_DEFINITION;

/**
 * Creates XRegistry definitions for policy artifacts.
 */
public class RegistryPolicyDefinitions {

    public static GroupDefinition createPolicyGroupDefinition() {
        return GroupDefinition.Builder.newInstance()
                .singular(GROUP_NAME)
                .plural(GROUPS_NAME)
                .resource(createPolicyResourceDefinition())
                .build();
    }

    public static ResourceDefinition createPolicyResourceDefinition() {
        var versionDefinition = VersionDefinition.Builder.newInstance()
                .resourceName("policy")
                .attribute(createDefinition("description", STRING))
                .attribute(createDefinition(CONTROL_POLICY, BOOLEAN))
                .attribute(createDefinition(ACCESS_POLICY, BOOLEAN))
                .attribute(AttributeDefinition.Builder.newInstance()
                        .name(POLICY_DEFINITION)
                        .serverRequired(true)
                        .clientRequired(true)
                        .type(STRING)
                        .build())
                .build();
        return ResourceDefinition.Builder.newInstance()
                .singular("policy")
                .plural("policies")
                .attribute(createDefinition("schemauri", XID))
                .versionDefinition(versionDefinition)
                .build();
    }

    private static AttributeDefinition createDefinition(String name, ValueType type) {
        return AttributeDefinition.Builder.newInstance()
                .name(name)
                .type(type)
                .build();
    }

    private RegistryPolicyDefinitions() {
    }
}
