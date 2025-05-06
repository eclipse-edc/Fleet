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

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.eclipse.edc.http.spi.EdcHttpClient;
import org.eclipse.edc.registry.spi.reconciler.ResourceReconcilerRegistry;
import org.eclipse.edc.registry.xregistry.model.definition.RegistrySpecification;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactory;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.types.TypeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static okhttp3.Protocol.HTTP_1_1;
import static org.eclipse.edc.registry.reconciler.core.manager.Definitions.BASE_REGISTRY;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReconciliationManagerTest {
    private ResourceReconcilerRegistry registry;
    private RegistrySpecification specification;
    private TypeFactory typeFactory;
    private EdcHttpClient httpClient;
    private TypeManager typeManager;
    private Monitor monitor;
    private ReconciliationManager reconciliationManager;
    private ObjectMapper mapper;

    @Test
    void verify_reconciliation() throws IOException {

        when(registry.getReconcilers()).thenReturn(List.of());

        var response = responseBuilder(200, "{}").build();
        when(httpClient.execute(isA(Request.class))).thenReturn(response);

        when(typeManager.readValue(isA(String.class), (Class) notNull())).thenReturn(mapper.readValue(BASE_REGISTRY, Map.class));
        reconciliationManager.run();

    }

    @BeforeEach
    void setUp() {
        registry = mock(ResourceReconcilerRegistry.class);
        specification = new RegistrySpecification("https://test.com");
        typeFactory = mock(TypeFactory.class);
        httpClient = mock(EdcHttpClient.class);
        typeManager = mock(TypeManager.class);
        monitor = mock(Monitor.class);

        mapper = new ObjectMapper();
        reconciliationManager = new ReconciliationManager(
                registry,
                specification,
                typeFactory,
                httpClient,
                typeManager,
                monitor);

    }

    private Response.Builder responseBuilder(int code, String body) {
        return new Response.Builder()
                .protocol(HTTP_1_1)
                .code(code)
                .message("any")
                .body(ResponseBody.create(body, MediaType.get("application/json")))
                .request(new Request.Builder().url("http://test.com").build());
    }

}