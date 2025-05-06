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

package org.eclipse.edc.registry.reconciler.core.registry;

import org.eclipse.edc.boot.util.TopologicalSort;
import org.eclipse.edc.registry.spi.reconciler.ResourceReconciler;
import org.eclipse.edc.registry.spi.reconciler.ResourceReconcilerRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation.
 */
public class ResourceReconcilerRegistryImpl implements ResourceReconcilerRegistry {
    private Map<String, ResourceReconciler> reconcilers = new HashMap<>();

    @Override
    public void registerReconciler(ResourceReconciler reconciler) {
        reconcilers.put(reconciler.resourceType(), reconciler);
    }

    @Override
    public List<ResourceReconciler> getReconcilers() {
        var sort = new TopologicalSort<ResourceReconciler>();

        reconcilers.values().forEach(reconciler -> reconciler.dependentResources()
                .forEach(resourceType -> {
                    var dependency = reconcilers.get(resourceType);
                    if (dependency != null) {
                        sort.addDependency(reconciler, dependency);
                    }
                }));

        var list = new ArrayList<>(reconcilers.values());
        sort.sort(list);
        return list;
    }


}
