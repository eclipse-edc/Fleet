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
import org.eclipse.edc.registry.spi.reconciler.ReconciliationContext;
import org.eclipse.edc.registry.spi.reconciler.ResourceReconciler;
import org.eclipse.edc.registry.xregistry.model.typed.TypedRegistry;
import org.eclipse.edc.registry.xregistry.policy.model.typed.TypedPolicyResource;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.result.ServiceResult;
import org.eclipse.edc.transaction.spi.TransactionContext;

import static org.eclipse.edc.registry.xregistry.policy.model.PolicyConstants.GROUPS_NAME;
import static org.eclipse.edc.spi.result.ServiceResult.success;

/**
 * Reconciles policy objects.
 */
public class PolicyResourceReconciler implements ResourceReconciler {
    private PolicyDefinitionStore policyStore;
    private TransactionContext transactionContext;

    private final Monitor monitor;

    public PolicyResourceReconciler(PolicyDefinitionStore policyStore,
                                    TransactionContext transactionContext,
                                    Monitor monitor) {
        this.policyStore = policyStore;
        this.transactionContext = transactionContext;
        this.monitor = monitor;
    }

    @Override
    public String resourceType() {
        return "policy";
    }

    @Override
    public ServiceResult<Void> reconcile(TypedRegistry registry, ReconciliationContext context) {
        var groups = registry.getGroups(GROUPS_NAME);
        transactionContext.execute(() -> {
            groups.values().forEach(group -> group.getResourcesOfType(TypedPolicyResource.class)
                    .forEach(this::processResource));
        });
        return success();
    }

    private void processResource(TypedPolicyResource resource) {
        // TODO retrieve latest policy and compare it with the existing one
        var version = resource.getLatestVersion();
        String policyId = "";
        var policy = policyStore.findById(policyId);
        if (policy != null) {
            // TODO rehydrate policy and update
        } else {
            // TODO create policy
        }
    }
}
