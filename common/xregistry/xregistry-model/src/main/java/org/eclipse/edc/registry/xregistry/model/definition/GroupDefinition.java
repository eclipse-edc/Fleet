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

package org.eclipse.edc.registry.xregistry.model.definition;

import java.util.HashMap;
import java.util.Map;

import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.COUNT;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.CREATED_AT;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.DESCRIPTION;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.EPOCH;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.ID;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.LABELS;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.MODIFIED_AT;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.NAME;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.SELF;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.SHORT_SELF;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.MAP;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.STRING;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.TIMESTAMP;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.UINTEGER;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.URL;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.XID;

/**
 * Defines an XRegistry group.
 */
public class GroupDefinition extends AbstractTypeDefinition {
    private Map<String, ResourceDefinition> resources = new HashMap<>();

    public Map<String, ResourceDefinition> getResources() {
        return resources;
    }

    public ResourceDefinition getResource(String name) {
        return resources.get(name);
    }

    protected void setContext(String parent) {
        super.setContext(parent + "." + getPlural());
        resources.values().forEach(r -> r.setContext(context));
    }

    private GroupDefinition() {
    }

    public static class Builder extends AbstractTypeDefinition.Builder<GroupDefinition, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder resource(ResourceDefinition resource) {
            definition.resources.put(resource.getPlural(), resource);
            return this;
        }

        public GroupDefinition build() {
            var result = super.build();
            addRequiredAttribute(definition.singular + ID, STRING);
            addRequiredAttribute(SELF, STRING); // TODO handle JSON Pointer
            addRequiredAttribute(RegistryConstants.XID, XID);
            addRequiredAttribute(EPOCH, UINTEGER);
            addRequiredAttribute(CREATED_AT, TIMESTAMP);
            addRequiredAttribute(MODIFIED_AT, TIMESTAMP);

            definition.resources.values().forEach(resource -> {
                addRequiredAttribute(resource.getPlural() + RegistryConstants.URL, URL);
                addRequiredAttribute(resource.getPlural() + COUNT, UINTEGER);
            });

            addOptionalAttribute(SHORT_SELF, URL);
            addOptionalAttribute(NAME, STRING);
            addOptionalAttribute(DESCRIPTION, STRING);
            addOptionalAttribute(LABELS, MAP);
            return result;
        }

        private Builder() {
            super(new GroupDefinition());
        }
    }
}
