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

package org.eclipse.edc.registry.server;

import org.eclipse.edc.registry.server.api.XregistryApiController;
import org.eclipse.edc.registry.server.defaults.DefaultRegistryStore;
import org.eclipse.edc.registry.server.spi.store.RegistryStore;
import org.eclipse.edc.registry.xregistry.model.definition.RegistrySpecification;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactory;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactoryImpl;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.runtime.metamodel.annotation.Provider;
import org.eclipse.edc.runtime.metamodel.annotation.Setting;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.web.spi.WebService;

/**
 * Provides core xRegistry server extensions.
 */
public class RegistryServerExtension implements ServiceExtension {
    private static final String API_CONTEXT = "default";

    @Setting(description = "Registry URL", key = "edc.registry.url", required = false)
    private String registryUrl = "https://localhost:8181/xregistry";

    @Inject
    private WebService webService;

    @Inject
    private RegistryStore registryStore;

    private RegistrySpecification specification;

    @Override
    public String name() {
        return "Registry Server";
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        webService.registerResource(API_CONTEXT, new XregistryApiController(registryStore));
    }

    @Provider(isDefault = true)
    public RegistryStore defaultRegistryStore() {
        return new DefaultRegistryStore(getSpecification());
    }

    @Provider
    public TypeFactory typeFactory() {
        return new TypeFactoryImpl();
    }

    @Provider
    public RegistrySpecification getSpecification() {
        if (specification == null) {
            specification = new RegistrySpecification(registryUrl);
        }
        return specification;
    }

}
