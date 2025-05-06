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

package org.eclipse.edc.registry.server.spi.store;

import org.eclipse.edc.registry.server.spi.resource.ResourceTypeStore;
import org.eclipse.edc.registry.xregistry.model.typed.TypedResource;
import org.eclipse.edc.spi.result.ServiceResult;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Manages resource persistence.
 * <p>
 * Some implementations may be read-only and not support write operations.
 */
public interface RegistryStore {

    /**
     * Registers a store for managing resource types.
     */
    void register(ResourceTypeStore<?> store);

    /**
     * Returns the registry.
     *
     * @param offset     the pagination offset
     * @param maxResults the maximum number of resources to include in each group.
     */
    @NotNull
    Map<String, Object> fetch(int offset, int maxResults);

    /**
     * Persists the resource.
     */
    ServiceResult<Void> createResource(TypedResource<?> resource);

    /**
     * Updates a resource.
     */
    ServiceResult<Void> updateResource(TypedResource<?> resource);

    /**
     * Deletes a resource
     */
    ServiceResult<Void> deleteResource(String id);

}
