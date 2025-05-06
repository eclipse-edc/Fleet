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

package org.eclipse.edc.registry.xregistry.model.typed;

import org.eclipse.edc.registry.xregistry.model.definition.GroupDefinition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * A typed view of an XRegistry group.
 */
public class TypedGroup extends AbstractType<GroupDefinition> {

    public Map<String, TypedResource<?>> getResources() {
        return getTypedResources().collect(toMap(AbstractType::getId, resource -> resource));
    }

    @SuppressWarnings("unchecked")
    public <T extends TypedResource<?>> T getResource(String name) {
        return (T) getTypedResources()
                .filter(resource -> resource.getId().equals(name))
                .findFirst().orElseGet((() -> null));
    }

    @SuppressWarnings("unchecked")
    public <T extends TypedResource<?>> Collection<T> getResourcesOfType(Class<T> type) {
        return (Collection<T>) getTypedResources()
                .filter(typedResource -> typedResource.getClass().equals(type))
                .toList();
    }

    public Builder toBuilder() {
        return Builder.newInstance()
                .untyped(untyped)
                .definition(definition)
                .typeFactory(typeFactory);
    }

    private TypedGroup(Map<String, Object> untyped, GroupDefinition definition, TypeFactory typeFactory) {
        super(untyped, definition, typeFactory);
        this.definition = definition;
    }

    @SuppressWarnings("unchecked")
    private Stream<? extends TypedResource<?>> getTypedResources() {
        return definition.getResources().values().stream().flatMap(resourceDefinition -> {
            Map<String, Map<String, Object>> resources = (Map<String, Map<String, Object>>) untyped.get(resourceDefinition.getPlural());
            return resources == null ? null : resources.values().stream()
                    .map(resource -> typeFactory.instantiate(resource, resourceDefinition));
        }).filter(Objects::nonNull);
    }

    public static class Builder extends AbstractType.Builder<GroupDefinition, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        @SuppressWarnings("unchecked")
        public <T extends TypedResource<?>> Builder resource(T resource) {
            checkModifiableState();
            var resources = (Map<String, Object>) untyped.computeIfAbsent(resource.getDefinition().getPlural(), k -> new HashMap<>());
            resources.put(resource.getId(), resource.getUntyped());
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder deleteResource(String name) {
            checkModifiableState();
            definition.getResources().values().forEach(resourceDefinition -> {
                Map<String, Object> resources = (Map<String, Object>) untyped.get(resourceDefinition.getPlural());
                if (resources == null) {
                    return;
                }
                resources.remove(name);
            });
            return this;
        }

        public TypedGroup build() {
            validate();
            return new TypedGroup(untyped, definition, typeFactory);
        }

        private Builder() {
        }
    }

}
