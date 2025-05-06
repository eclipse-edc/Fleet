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

package org.eclipse.edc.registry.reconciler.core.manager;

import okhttp3.Request;
import org.eclipse.edc.http.spi.EdcHttpClient;
import org.eclipse.edc.registry.spi.reconciler.ReconciliationContext;
import org.eclipse.edc.registry.spi.reconciler.ResourceReconciler;
import org.eclipse.edc.registry.spi.reconciler.ResourceReconcilerRegistry;
import org.eclipse.edc.registry.xregistry.library.validation.AttributeValidator;
import org.eclipse.edc.registry.xregistry.library.validation.GroupValidator;
import org.eclipse.edc.registry.xregistry.library.validation.RegistryValidator;
import org.eclipse.edc.registry.xregistry.library.validation.ValidationResult;
import org.eclipse.edc.registry.xregistry.model.definition.RegistrySpecification;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactory;
import org.eclipse.edc.registry.xregistry.model.typed.TypedRegistry;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.types.TypeManager;

import java.io.IOException;
import java.util.Map;

import static java.lang.String.join;

/**
 * Periodically performs reconciliation by delegating to an ordered list of {@link ResourceReconciler}s.
 */
public class ReconciliationManager implements Runnable {
    private ResourceReconcilerRegistry reconcilerRegistry;
    private RegistrySpecification specification;
    private final TypeFactory typeFactory;
    private final EdcHttpClient httpClient;
    private final TypeManager typeManager;
    private Monitor monitor;

    public ReconciliationManager(ResourceReconcilerRegistry reconcilerRegistry,
                                 RegistrySpecification specification,
                                 TypeFactory typeFactory,
                                 EdcHttpClient httpClient,
                                 TypeManager typeManager,
                                 Monitor monitor) {
        this.reconcilerRegistry = reconcilerRegistry;
        this.specification = specification;
        this.typeFactory = typeFactory;
        this.httpClient = httpClient;
        var paginate = true;
        this.typeManager = typeManager;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        var paginate = true;
        var context = new ReconciliationContext();
        while (paginate) {
            var request = new Request.Builder().url(specification.getUrl()).get().build();
            try (var response = httpClient.execute(request)) {
                if (response.code() != 200) {
                    monitor.severe("Registry request returned error code: " + response.code());
                    return;
                }
                try (var body = response.body()) {
                    if (body == null) {
                        monitor.severe("Registry request returned an empty body. Aborting reconciliation.");
                        return;
                    }
                    @SuppressWarnings("unchecked")
                    Map<String, Object> registryResult = typeManager.readValue(body.string(), Map.class);

                    var validationResult = validateRegistry(registryResult);
                    if (!validationResult.valid()) {
                        monitor.severe("Invalid registry data: \n" + join("\n", validationResult.violations()));
                        return;
                    }

                    var typed = convertToTyped(registryResult);

                    reconcilerRegistry.getReconcilers().forEach(reconciler -> reconciler.reconcile(typed, context));
                }
            } catch (IOException e) {
                monitor.severe("Registry request returned an exception. Aborting reconciliation.", e);
                return;
            }

            paginate = false; // TODO support pagination
        }
        monitor.debug("Reconciliation completed");
    }

    private ValidationResult validateRegistry(Map<String, Object> registryResult) {
        var registryDefinition = specification.getRegistryDefinition();
        var attributeValidator = new AttributeValidator();
        var groupValidator = new GroupValidator(attributeValidator);
        var validator = new RegistryValidator(groupValidator, attributeValidator);
        return validator.validate(registryResult, registryDefinition);
    }

    private TypedRegistry convertToTyped(Map<String, Object> registryResult) {
        return TypedRegistry.Builder.newInstance()
                .untyped(registryResult)
                .definition(specification.getRegistryDefinition())
                .typeFactory(typeFactory)
                .build();

    }
}
