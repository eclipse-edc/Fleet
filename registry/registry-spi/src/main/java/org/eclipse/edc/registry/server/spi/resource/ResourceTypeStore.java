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

package org.eclipse.edc.registry.server.spi.resource;

import org.eclipse.edc.registry.xregistry.model.typed.TypedGroup;
import org.eclipse.edc.registry.xregistry.model.typed.TypedResource;
import org.eclipse.edc.runtime.metamodel.annotation.ExtensionPoint;
import org.eclipse.edc.spi.result.ServiceResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Manages groups for a resource type.
 */
@ExtensionPoint
public interface ResourceTypeStore<T extends TypedResource<?>> {

    /**
     * Returns the type this store manages.
     */
    Class<T> getType();

    /**
     * Returns group data.
     *
     * @param offset     the pagination offset
     * @param maxResults the maximum number of resources to include in each group.
     */
    @NotNull
    Collection<TypedGroup> fetchGroups(int offset, int maxResults);

    ServiceResult<Void> createResource(T resource);

    ServiceResult<Void> updateResource(T resource);

    ServiceResult<Void> deleteResource(String id);
}
