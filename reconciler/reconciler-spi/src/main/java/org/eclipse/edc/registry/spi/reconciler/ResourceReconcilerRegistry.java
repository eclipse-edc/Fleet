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

import java.util.List;

/**
 * Registers and manages {@link ResourceReconciler}s.
 */
public interface ResourceReconcilerRegistry {

    /**
     * Registers a reconciler.
     */
    void registerReconciler(ResourceReconciler reconciler);

    /**
     * Returns a collection of {@link ResourceReconciler}s order by their dependencies.
     */
    List<ResourceReconciler> getReconcilers();
}
