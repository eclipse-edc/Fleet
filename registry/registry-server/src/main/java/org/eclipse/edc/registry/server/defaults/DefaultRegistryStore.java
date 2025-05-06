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

package org.eclipse.edc.registry.server.defaults;

import org.eclipse.edc.registry.server.spi.resource.ResourceTypeStore;
import org.eclipse.edc.registry.server.spi.store.RegistryStore;
import org.eclipse.edc.registry.xregistry.model.definition.RegistryDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.RegistrySpecification;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactory;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactoryImpl;
import org.eclipse.edc.registry.xregistry.model.typed.TypedRegistry;
import org.eclipse.edc.registry.xregistry.model.typed.TypedResource;
import org.eclipse.edc.spi.result.ServiceResult;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Default implementation.
 */
public class DefaultRegistryStore implements RegistryStore {
    private static final String DEFAULT_REGISTRY_NAME = "edc-registry";
    private static final String REGISTRY_XID = "/registry";
    private final RegistrySpecification specification;

    private TypeFactory typeFactory;
    private RegistryDefinition registryDefinition;

    private Instant created;

    private Map<Class<?>, ResourceTypeStore<?>> cache = new HashMap<>();

    public DefaultRegistryStore(RegistrySpecification specification) {
        this.specification = specification;
        typeFactory = new TypeFactoryImpl();
        registryDefinition = RegistryDefinition.Builder.newInstance().build();
        created = Instant.now();
    }

    @Override
    public void register(ResourceTypeStore<?> store) {
        cache.put(store.getType(), store);
    }

    @Override
    public @NotNull Map<String, Object> fetch(int offset, int maxResults) {
        var registry = TypedRegistry.Builder.newInstance()
                .definition(registryDefinition)
                .untyped(new LinkedHashMap<>())
                .typeFactory(typeFactory);

        configureRegistry(registry);

        var groupCounts = new HashMap<String, AtomicInteger>();
        var groupUrls = new HashMap<String, String>();
        cache.values().stream()
                .flatMap(store -> store.fetchGroups(offset, maxResults).stream())
                .forEach(typedGroup -> {
                    var plural = typedGroup.getDefinition().getPlural();
                    groupCounts.computeIfAbsent(plural + "count", k -> new AtomicInteger()).incrementAndGet();
                    groupUrls.put(plural + "url", "#/" + plural);
                    registry.group(typedGroup);
                });
        groupCounts.forEach((key, count) -> registry.set(key, count.intValue()));
        groupUrls.forEach(registry::set);
        return registry.build().asMap();
    }

    @Override
    public ServiceResult<Void> createResource(TypedResource<?> resource) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServiceResult<Void> updateResource(TypedResource<?> resource) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServiceResult<Void> deleteResource(String id) {
        throw new UnsupportedOperationException();
    }

    private void configureRegistry(TypedRegistry.Builder registry) {
        registry.id(DEFAULT_REGISTRY_NAME)
                .self(specification.getUrl())
                .url(specification.getUrl())
                .epoch(1)  // TODO FIXME
                .createdAt(created)
                .modifiedAt(created)
                .xid(REGISTRY_XID)
        ;
    }
}
