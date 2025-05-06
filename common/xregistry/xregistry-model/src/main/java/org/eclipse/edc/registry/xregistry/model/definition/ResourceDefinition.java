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

import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.ID;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.META;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.META_URL;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.SELF;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.VERSIONS_COUNT;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.VERSIONS_URL;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.MAP;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.STRING;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.UINTEGER;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.URL;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.XID;

/**
 * Defines an XRegistry resource.
 */
public class ResourceDefinition extends AbstractTypeDefinition {
    private VersionDefinition versionDefinition;

    public VersionDefinition getVersionDefinition() {
        return versionDefinition;
    }

    @Override
    protected void setContext(String context) {
        var resourceContext = context + "." + getSingular();
        super.setContext(resourceContext);
        versionDefinition.setContext(resourceContext);
    }

    private ResourceDefinition() {
    }

    public static class Builder extends AbstractTypeDefinition.Builder<ResourceDefinition, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder versionDefinition(VersionDefinition versionDefinition) {
            this.definition.versionDefinition = versionDefinition;
            return this;
        }

        public ResourceDefinition build() {
            var result = super.build();
            addRequiredAttribute(definition.singular + ID, STRING);
            addRequiredAttribute(SELF, STRING);   // TODO handle JSON Pointer
            addRequiredAttribute(RegistryConstants.XID, XID);
            addRequiredAttribute(META_URL, URL);
            addRequiredAttribute(VERSIONS_URL, URL);
            addRequiredAttribute(VERSIONS_COUNT, UINTEGER);

            addOptionalAttribute(META, MAP);

            if (definition.versionDefinition == null) {
                result.versionDefinition = VersionDefinition.Builder.newInstance()
                        .resourceName(definition.singular)
                        .build();
            }

            return result;
        }

        private Builder() {
            super(new ResourceDefinition());
        }
    }
}

