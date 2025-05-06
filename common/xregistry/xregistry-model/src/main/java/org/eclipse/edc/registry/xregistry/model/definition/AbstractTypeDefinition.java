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

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.NAME_VALIDATION;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.ARRAY;

/**
 * An XRegistry model definition.
 */
public abstract class AbstractTypeDefinition {
    protected String singular;
    protected String plural;
    protected String context;

    protected Map<String, AttributeDefinition> attributes = new LinkedHashMap<>();

    public String getSingular() {
        return singular;
    }

    public String getPlural() {
        return plural;
    }

    public String getContext() {
        return context;
    }

    public Map<String, AttributeDefinition> getAttributes() {
        return attributes;
    }

    protected void setContext(String context) {
        this.context = context;
    }

    public static class Builder<T extends AbstractTypeDefinition, B extends Builder<T, B>> {
        protected T definition;

        @SuppressWarnings("unchecked")
        public B singular(String name) {
            definition.singular = name;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B plural(String name) {
            definition.plural = name;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B attribute(AttributeDefinition attributeDefinition) {
            definition.attributes.put(attributeDefinition.getName(), attributeDefinition);
            return (B) this;
        }

        protected T build() {
            requireNonNull(definition.singular, "singular");
            requireNonNull(definition.plural, "plural");

            if (!NAME_VALIDATION.matcher(definition.singular).matches()) {
                throw new IllegalArgumentException("Singular name is invalid: " + definition.singular);
            }

            if (!NAME_VALIDATION.matcher(definition.plural).matches()) {
                throw new IllegalArgumentException("Plural name is invalid: " + definition.plural);
            }
            return definition;
        }

        protected Builder(T definition) {
            this.definition = definition;
        }

        protected void addRequiredAttribute(String name, ValueType type) {
            var attributeDefinition = AttributeDefinition.Builder
                    .newInstance()
                    .name(name)
                    .type(type)
                    .clientRequired(true)
                    .serverRequired(true)
                    .build();
            definition.attributes.put(name, attributeDefinition);
        }

        protected void addOptionalAttribute(String name, ValueType type) {
            definition.attributes.put(name, createDefinition(name, type));
        }

        protected AttributeDefinition createDefinition(String name, ValueType type) {
            return AttributeDefinition.Builder.newInstance()
                    .name(name)
                    .type(type)
                    .build();
        }

        protected AttributeDefinition createArrayDefinition(String name, ValueType componentType) {
            return AttributeDefinition.Builder.newInstance()
                    .name(name)
                    .type(ARRAY)
                    .type(componentType)
                    .build();
        }
    }
}
