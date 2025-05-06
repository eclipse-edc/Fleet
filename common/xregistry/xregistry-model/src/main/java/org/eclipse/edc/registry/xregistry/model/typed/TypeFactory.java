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

package org.eclipse.edc.registry.xregistry.model.typed;

import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;

import java.util.Map;

/**
 * Delegates to {@link TypeFactory.Instantiator}s to create typed views of XRegistry resources.
 */
public interface TypeFactory {

    /**
     * Instantiates a typed view for the XRegistry data.
     */
    @FunctionalInterface
    interface Instantiator {
        TypedResource<?> instantiate(Map<String, Object> untyped, ResourceDefinition definition, TypeFactory typeFactory);
    }

    /**
     * Instantiates an XRegistry type.
     *
     * @param untyped    the untyped data
     * @param definition the type definition
     * @return the instance
     */
    TypedResource<?> instantiate(Map<String, Object> untyped, ResourceDefinition definition);

    /**
     * Registers a resource {@link Instantiator}.
     */
    void registerResource(String name, Instantiator instantiator);

}
