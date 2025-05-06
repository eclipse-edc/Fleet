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

package org.eclipse.edc.registry.spi.reconciler;

import org.eclipse.edc.registry.xregistry.model.typed.TypedRegistry;
import org.eclipse.edc.spi.result.ServiceResult;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Reconciles the state for a resource.
 */
public interface ResourceReconciler {

    /**
     * Returns the resource type handled by this reconciler.
     */
    String resourceType();

    /**
     * Returns a collection of resources types that this reconciler depends on.
     */
    default List<String> dependentResources() {
        return emptyList();
    }

    /**
     * Performs a reconciliation operation. Reconciliation may be called multiple times per cycle, for example, when registry
     * resources are paginated.
     */
    ServiceResult<Void> reconcile(TypedRegistry registry, ReconciliationContext context);

}
