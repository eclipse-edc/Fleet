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

package org.eclipse.edc.registry.xregistry.policy.model.typed;

import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactory;
import org.eclipse.edc.registry.xregistry.model.typed.TypedResource;

import java.util.Map;

/**
 * Typed view of policy resources.
 */
public class TypedPolicyResource extends TypedResource<TypedPolicyVersion> {

    public Builder toBuilder() {
        return Builder.newInstance()
                .untyped(untyped)
                .definition(definition)
                .typeFactory(typeFactory);
    }

    public TypedPolicyResource(Map<String, Object> untyped, ResourceDefinition definition, TypeFactory typeFactory) {
        super(untyped, definition, typeFactory);
    }

    @Override
    protected TypedPolicyVersion createVersion(Map<String, Object> untypedVersion) {
        return new TypedPolicyVersion(untypedVersion, definition.getVersionDefinition(), typeFactory);
    }

    public static class Builder extends TypedResource.Builder<TypedPolicyVersion, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public TypedPolicyResource build() {
            validate();
            return new TypedPolicyResource(untyped, definition, typeFactory);
        }
    }
}
