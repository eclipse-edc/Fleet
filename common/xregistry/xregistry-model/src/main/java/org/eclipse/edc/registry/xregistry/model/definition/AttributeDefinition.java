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
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.NAME_VALIDATION;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.ANY;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.ARRAY;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.MAP;

/**
 * Defines an XRegistry model attribute.
 */
public class AttributeDefinition {
    private static final Function<Object, String> ALWAYS_CHECKER = o -> null;

    private String name;
    private ValueType type = ANY;
    private ValueType componentType = ANY;
    private String target;
    private String description;
    private boolean immutable;
    private boolean serverRequired;
    private boolean clientRequired;
    private boolean readonly;
    private List<String> enumeration;

    // Performs additional type constraints checking. If they pass, null is returned; otherwise an error
    private Function<Object, String> typeConstraintsChecker = ALWAYS_CHECKER;

    private Map<String, AttributeDefinition> attributes = new HashMap<>();

    public String getName() {
        return name;
    }

    public ValueType getType() {
        return type;
    }

    public ValueType getComponentType() {
        return componentType;
    }

    public boolean scalarType() {
        return type == MAP || type == ARRAY;
    }

    public boolean serverRequired() {
        return serverRequired;
    }

    public boolean clientRequired() {
        return clientRequired;
    }

    public Map<String, AttributeDefinition> getAttributes() {
        return attributes;
    }

    public String getTarget() {
        return target;
    }

    public String getDescription() {
        return description;
    }

    public boolean isImmutable() {
        return immutable;
    }

    public boolean isServerRequired() {
        return serverRequired;
    }

    public boolean isClientRequired() {
        return clientRequired;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public List<String> getEnumeration() {
        return enumeration;
    }

    public Function<Object, String> getTypeConstraintsChecker() {
        return typeConstraintsChecker;
    }

    private AttributeDefinition() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        AttributeDefinition that = (AttributeDefinition) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static class Builder {
        private AttributeDefinition definition;

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder name(String name) {
            definition.name = name;
            return this;
        }

        public Builder clientRequired(boolean value) {
            definition.clientRequired = value;
            return this;
        }

        public Builder serverRequired(boolean value) {
            definition.serverRequired = value;
            return this;
        }

        public Builder type(ValueType type) {
            definition.type = type;
            return this;
        }

        public Builder typeConstraintsChecker(Function<Object, String> checker) {
            definition.typeConstraintsChecker = checker;
            return this;
        }

        public Builder attribute(AttributeDefinition definition) {
            definition.attributes.put(definition.getName(), definition);
            return this;
        }

        public AttributeDefinition build() {
            if (!NAME_VALIDATION.matcher(requireNonNull(definition.name, "Attribute name is null")).matches()) {
                throw new IllegalArgumentException("Attribute name must be between 1 and 63 lowercase alpha numeric characters and not start with a digit: " + definition.name);
            }
            return definition;
        }

        private Builder() {
            definition = new AttributeDefinition();
        }
    }

}
