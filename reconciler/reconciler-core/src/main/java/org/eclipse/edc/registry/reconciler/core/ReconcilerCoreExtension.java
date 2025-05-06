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

package org.eclipse.edc.registry.reconciler.core;

import org.eclipse.edc.registry.reconciler.core.registry.ResourceReconcilerRegistryImpl;
import org.eclipse.edc.registry.spi.reconciler.ResourceReconcilerRegistry;
import org.eclipse.edc.registry.xregistry.model.definition.RegistrySpecification;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactory;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactoryImpl;
import org.eclipse.edc.runtime.metamodel.annotation.Provider;
import org.eclipse.edc.runtime.metamodel.annotation.Setting;
import org.eclipse.edc.spi.system.ServiceExtension;

/**
 * Loads core services that handle resource reconciliation.
 */
public class ReconcilerCoreExtension implements ServiceExtension {
    @Setting(description = "Registry location", key = "edc.registry")
    private String registryUrl;

    private RegistrySpecification specification;

    @Override
    public String name() {
        return "Core Reconciler";
    }

    @Provider
    public TypeFactory typeFactory() {
        return new TypeFactoryImpl();
    }

    @Provider
    public ResourceReconcilerRegistry getResourceRegistry() {
        return new ResourceReconcilerRegistryImpl();
    }

    @Provider
    public RegistrySpecification getSpecification() {
        if (specification == null) {
            specification = new RegistrySpecification(registryUrl);
        }
        return specification;
    }

}
