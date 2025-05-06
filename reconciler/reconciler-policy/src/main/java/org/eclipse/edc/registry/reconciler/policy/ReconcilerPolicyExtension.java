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

package org.eclipse.edc.registry.reconciler.policy;

import org.eclipse.edc.connector.controlplane.policy.spi.store.PolicyDefinitionStore;
import org.eclipse.edc.registry.spi.reconciler.ResourceReconcilerRegistry;
import org.eclipse.edc.registry.xregistry.model.definition.RegistrySpecification;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactory;
import org.eclipse.edc.registry.xregistry.policy.model.typed.TypedPolicyResource;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.transaction.spi.TransactionContext;

import static org.eclipse.edc.registry.xregistry.policy.model.definition.RegistryPolicyDefinitions.createPolicyGroupDefinition;

/**
 * Loads XRegistry policy extensions.
 */
public class ReconcilerPolicyExtension implements ServiceExtension {

    @Inject
    private PolicyDefinitionStore policyStore;

    @Inject
    private RegistrySpecification specification;

    @Inject
    private ResourceReconcilerRegistry reconcilerRegistry;

    @Inject
    private TypeFactory typeFactory;

    @Inject
    private TransactionContext transactionContext;

    @Inject
    private Monitor monitor;

    @Override
    public String name() {
        return "Policy Reconciler";
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        specification.registerGroup(createPolicyGroupDefinition());
        typeFactory.registerResource("policy", TypedPolicyResource::new);
        reconcilerRegistry.registerReconciler(new PolicyResourceReconciler(policyStore, transactionContext, monitor));
    }


}
