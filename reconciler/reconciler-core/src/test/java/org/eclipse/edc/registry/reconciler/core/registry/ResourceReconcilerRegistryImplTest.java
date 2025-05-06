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

import org.eclipse.edc.registry.spi.reconciler.ReconciliationContext;
import org.eclipse.edc.registry.spi.reconciler.ResourceReconciler;
import org.eclipse.edc.registry.xregistry.model.typed.TypedRegistry;
import org.eclipse.edc.spi.result.ServiceResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class ResourceReconcilerRegistryImplTest {

    @Test
    void verify_sort() {
        var registry = new ResourceReconcilerRegistryImpl();

        registry.registerReconciler(new MockReconciler("bar", emptyList()));
        registry.registerReconciler(new MockReconciler("baz", List.of("foo")));
        registry.registerReconciler(new MockReconciler("foo", List.of("bar")));

        var reconcilers = registry.getReconcilers();
        assertThat(reconcilers.get(0).resourceType()).isEqualTo("bar");
        assertThat(reconcilers.get(1).resourceType()).isEqualTo("foo");
        assertThat(reconcilers.get(2).resourceType()).isEqualTo("baz");
    }

    private static class MockReconciler implements ResourceReconciler {
        private String type;
        private final List<String> dependencies;

        MockReconciler(String type, List<String> dependencies) {
            this.type = type;
            this.dependencies = dependencies;
        }

        @Override
        public List<String> dependentResources() {
            return dependencies;
        }

        @Override
        public String resourceType() {
            return type;
        }

        @Override
        public ServiceResult<Void> reconcile(TypedRegistry registry, ReconciliationContext context) {
            throw new UnsupportedOperationException();
        }
    }
}