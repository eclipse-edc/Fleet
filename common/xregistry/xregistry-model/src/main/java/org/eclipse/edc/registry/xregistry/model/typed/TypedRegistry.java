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

import org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants;
import org.eclipse.edc.registry.xregistry.model.definition.RegistryDefinition;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toMap;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.SELF;

/**
 * A typed view of an XRegistry.
 */
public class TypedRegistry extends AbstractType<RegistryDefinition> {
    private TypeFactory typeFactory;

    protected TypedRegistry(Map<String, Object> untyped, RegistryDefinition definition, TypeFactory typeFactory) {
        super(untyped, definition, typeFactory);
        this.typeFactory = typeFactory;
        this.definition = definition;
    }

    @SuppressWarnings("unchecked")
    public <T extends TypedGroup> Map<String, T> getGroups(String name) {
        return (Map<String, T>) getTypedGroups().getOrDefault(name, emptyMap());
    }

    public URL getUrl() {
        return getUrl(SELF); // registry level self attributes are URLs
    }

    public Builder toBuilder() {
        return Builder.newInstance()
                .untyped(untyped)
                .definition(definition)
                .typeFactory(typeFactory);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, TypedGroup>> getTypedGroups() {
        return this.definition.getGroups().values().stream().map(groupDefinition -> {
            var groupContainerName = groupDefinition.getPlural();
            var groupsMap = (Map<String, Map<String, Object>>) untyped.get(groupContainerName);
            if (groupsMap == null) {
                groupsMap = emptyMap();
            }
            var typedGroupsMap = groupsMap.values().stream()
                    .map(group -> TypedGroup.Builder.newInstance()
                            .untyped(group)
                            .definition(groupDefinition)
                            .typeFactory(typeFactory)
                            .build()).collect(toMap(TypedGroup::getId, group -> group));
            return new GroupHolder(groupContainerName, typedGroupsMap);
        }).collect(toMap(h -> h.name, h -> h.groups));
    }

    private record GroupHolder(String name, Map<String, TypedGroup> groups) {
    }

    public static class Builder extends AbstractType.Builder<RegistryDefinition, Builder> {

        public static Builder newInstance() {
            return new Builder();
        }

        @SuppressWarnings("unchecked")
        public Builder url(String url) {
            checkModifiableState();
            untyped.put(RegistryConstants.URL, url);
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder group(TypedGroup typedGroup) {
            checkModifiableState();
            var groupContainer = (Map<String, Object>) untyped.computeIfAbsent(typedGroup.getDefinition().getPlural(), k -> new HashMap<>());
            groupContainer.put(typedGroup.getId(), typedGroup.getUntyped());
            recalculateGroups();
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder removeGroup(String name) {
            checkModifiableState();
            this.definition.getGroups().values().forEach(groupDefinition -> {
                var groupContainerName = groupDefinition.getPlural();
                var groupsMap = (Map<String, Map<String, Object>>) untyped.get(groupContainerName);
                groupsMap.remove(name);
                if (groupsMap.isEmpty()) {
                    untyped.remove(groupContainerName);
                }
                recalculateGroups();
            });
            return this;
        }

        @SuppressWarnings("unchecked")
        private void recalculateGroups() {
            this.definition.getGroups().values().forEach(groupDefinition -> {
                var groupContainerName = groupDefinition.getPlural();
                var groupsMap = (Map<String, Map<String, Object>>) untyped.get(groupContainerName);
                if (groupsMap == null || groupsMap.isEmpty()) {
                    untyped.remove(groupContainerName + "url");
                    untyped.remove(groupContainerName + "count");
                } else {
                    untyped.put(groupContainerName + "url", "#/" + groupContainerName);
                    untyped.put(groupContainerName + "count", groupsMap.size());
                }
            });
        }

        public TypedRegistry build() {
            validate();
            return new TypedRegistry(untyped, definition, typeFactory);
        }

        private Builder() {
        }
    }

}
