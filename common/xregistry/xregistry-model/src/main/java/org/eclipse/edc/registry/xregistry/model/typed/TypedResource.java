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

import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toMap;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.VERSIONS;

/**
 * A typed view of an XRegistry resource.
 */
public abstract class TypedResource<V extends TypedVersion> extends AbstractType<ResourceDefinition> {
    protected ResourceDefinition definition;

    @SuppressWarnings("unchecked")
    public Map<String, V> getVersions() {
        var untypedVersions = (Map<String, Map<String, Object>>) untyped.get(VERSIONS);
        if (untypedVersions != null) {
            Set<Map.Entry<String, Map<String, Object>>> entries = untypedVersions.entrySet();
            return entries.stream().map(entry -> createVersion(entry.getValue()))
                    .collect(toMap(AbstractType::getId, v -> v));
        }
        return emptyMap();
    }

    @SuppressWarnings("unchecked")
    public V getLatestVersion() {
        var untypedVersions = (Map<String, Map<String, Object>>) untyped.get(VERSIONS);
        if (untypedVersions != null) {
            var sorted = new TreeMap<>(untypedVersions);
            var version = sorted.lastEntry();
            if (version != null) {
                return createVersion(version.getValue());
            }
        }
        return null;
    }

    public ResourceDefinition getDefinition() {
        return definition;
    }

    protected TypedResource(Map<String, Object> untyped, ResourceDefinition definition, TypeFactory typeFactory) {
        super(untyped, definition, typeFactory);
        this.definition = definition;
    }

    protected abstract V createVersion(Map<String, Object> untypedVersion);

    public static class Builder<V extends TypedVersion, B extends Builder<V, B>> extends AbstractType.Builder<ResourceDefinition, B> {

        @SuppressWarnings("unchecked")
        public B version(String name, V version) {
            checkModifiableState();
            var versions = (Map<String, Map<String, Object>>) untyped.computeIfAbsent(VERSIONS, k -> new HashMap<>());
            versions.put(version.getId(), version.getUntyped());
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B removeVersion(String name) {
            @SuppressWarnings("unchecked")
            var untypedVersions = (Map<String, Map<String, Object>>) this.untyped.get(VERSIONS);
            if (untypedVersions == null) {
                return (B) this;
            }
            untypedVersions.remove(name);
            return (B) this;
        }


        protected Builder() {
        }
    }
}
