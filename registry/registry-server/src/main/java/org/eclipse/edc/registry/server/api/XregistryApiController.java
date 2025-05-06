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

package org.eclipse.edc.registry.server.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.eclipse.edc.registry.server.spi.store.RegistryStore;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.ok;
import static java.lang.Integer.MAX_VALUE;

/**
 * Implements the XRegistry server API.
 */
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/registry")
public class XregistryApiController implements XregistryApi {
    private RegistryStore registryStore;

    public XregistryApiController(RegistryStore registryStore) {
        this.registryStore = registryStore;
    }

    @GET
    public Response getRegistry() {
        return ok(registryStore.fetch(0, MAX_VALUE)).build();
    }

}
