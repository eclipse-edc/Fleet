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

package org.eclipse.edc.registry.xregistry.schema.model.definition;

import org.eclipse.edc.registry.xregistry.model.definition.AttributeDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.GroupDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.ValueType;
import org.eclipse.edc.registry.xregistry.model.definition.VersionDefinition;

import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.STRING;

/**
 * Creates XRegistry definitions for schema artifacts.
 */
public class RegistrySchemaDefinitions {

    public static GroupDefinition createSchemaGroupDefinition() {
        return GroupDefinition.Builder.newInstance()
                .singular("schemagroup")
                .plural("schemagroups")
                .resource(createSchemaResourceDefinition())
                .build();
    }

    public static ResourceDefinition createSchemaResourceDefinition() {
        var versionDefinition = VersionDefinition.Builder.newInstance()
                .resourceName("schema")
                .attribute(createDefinition("format", STRING))
                .attribute(createDefinition("schema", STRING))
                .attribute(createDefinition("schemabase64", STRING))
                .build();
        return ResourceDefinition.Builder.newInstance()
                .singular("schema")
                .plural("schemas")
                .versionDefinition(versionDefinition)
                .build();
    }

    private static AttributeDefinition createDefinition(String name, ValueType type) {
        return AttributeDefinition.Builder.newInstance()
                .name(name)
                .type(type)
                .build();
    }


}
