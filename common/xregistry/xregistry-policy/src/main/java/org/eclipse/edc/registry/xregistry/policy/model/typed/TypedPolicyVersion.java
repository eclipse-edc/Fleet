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

import org.eclipse.edc.registry.xregistry.model.definition.VersionDefinition;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactory;
import org.eclipse.edc.registry.xregistry.model.typed.TypedVersion;

import java.util.Map;

import static org.eclipse.edc.registry.xregistry.policy.model.PolicyConstants.ACCESS_POLICY;
import static org.eclipse.edc.registry.xregistry.policy.model.PolicyConstants.CONTROL_POLICY;
import static org.eclipse.edc.registry.xregistry.policy.model.PolicyConstants.POLICY_DEFINITION;

/**
 * A typed view of a policy resource version.
 */
public class TypedPolicyVersion extends TypedVersion {

    public String getPolicyDefinition() {
        return getString(POLICY_DEFINITION);
    }

    public boolean isControlPolicy() {
        return getBool(CONTROL_POLICY);
    }

    public boolean isAccessPolicy() {
        return getBool(ACCESS_POLICY);
    }

    protected TypedPolicyVersion(Map<String, Object> untyped, VersionDefinition definition, TypeFactory typeFactory) {
        super(untyped, definition, typeFactory);
    }

    public Builder toBuilder() {
        return Builder.newInstance()
                .untyped(untyped)
                .definition(definition)
                .typeFactory(typeFactory);
    }

    public static class Builder extends TypedVersion.Builder<VersionDefinition, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder policyDefinition(String policyDefinition) {
            checkModifiableState();
            untyped.put(POLICY_DEFINITION, policyDefinition);
            return this;
        }

        public Builder controlPolicy(boolean value) {
            checkModifiableState();
            untyped.put(CONTROL_POLICY, value);
            return this;
        }

        public Builder accessPolicy(boolean value) {
            checkModifiableState();
            untyped.put(ACCESS_POLICY, value);
            return this;
        }

        public TypedPolicyVersion build() {
            validate();
            return new TypedPolicyVersion(untyped, definition, typeFactory);
        }

        private Builder() {
            super();
        }
    }
}
