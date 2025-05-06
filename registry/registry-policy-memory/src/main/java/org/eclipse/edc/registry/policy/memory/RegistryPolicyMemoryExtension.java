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

import org.eclipse.edc.registry.server.spi.store.RegistryStore;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.util.concurrency.LockManager;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Contributes an in-memory implementation of the policy resource store.
 */
public class RegistryPolicyMemoryExtension implements ServiceExtension {

    @Inject
    private RegistryStore registryStore;

    @Override
    public String name() {
        return "Policy Registry Memory";
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var store = new InMemoryPolicyResourceTypeStore(new LockManager(new ReentrantReadWriteLock()));
        registryStore.register(store);
    }
}
