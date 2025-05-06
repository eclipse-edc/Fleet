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

package org.eclipse.edc.registry.policy.memory;

import org.eclipse.edc.registry.server.spi.resource.ResourceTypeStore;
import org.eclipse.edc.registry.xregistry.model.definition.RegistrySpecification;
import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactory;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactoryImpl;
import org.eclipse.edc.registry.xregistry.model.typed.TypedGroup;
import org.eclipse.edc.registry.xregistry.policy.model.typed.TypedPolicyResource;
import org.eclipse.edc.spi.result.ServiceResult;
import org.eclipse.edc.util.concurrency.LockManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import static org.eclipse.edc.registry.xregistry.policy.model.definition.RegistryPolicyDefinitions.createPolicyGroupDefinition;
import static org.eclipse.edc.registry.xregistry.policy.model.definition.RegistryPolicyDefinitions.createPolicyResourceDefinition;

/**
 * An In-memory store for policy resources.
 */
public class InMemoryPolicyResourceTypeStore implements ResourceTypeStore<TypedPolicyResource> {
    private LockManager lockManager;
    private RegistrySpecification specification;
    private TypeFactory typeFactory;
    private ResourceDefinition resourceDefinition;

    public InMemoryPolicyResourceTypeStore(LockManager lockManager) {
        this.lockManager = lockManager;
        typeFactory = new TypeFactoryImpl();
        resourceDefinition = createPolicyResourceDefinition();
    }

    @Override
    public Class<TypedPolicyResource> getType() {
        return TypedPolicyResource.class;
    }

    @Override
    public @NotNull Collection<TypedGroup> fetchGroups(int offset, int maxResults) {
        return lockManager.readLock(() -> {
            var policyResource = TypedPolicyResource.Builder.newInstance()
                    .untyped(new LinkedHashMap<>())
                    .typeFactory(typeFactory)
                    .definition(resourceDefinition)
                    .id("policy1")
                    .build();
            var typedGroup = TypedGroup.Builder.newInstance()
                    .untyped(new LinkedHashMap<>())
                    .typeFactory(typeFactory)
                    .definition(createPolicyGroupDefinition())
                    .resource(policyResource)
                    .id("testpolicies")
                    .build();

            return List.of(typedGroup);
        });
    }

    @Override
    public ServiceResult<Void> createResource(TypedPolicyResource resource) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServiceResult<Void> updateResource(TypedPolicyResource resource) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServiceResult<Void> deleteResource(String id) {
        throw new UnsupportedOperationException();
    }
}
