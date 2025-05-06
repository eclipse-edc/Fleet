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

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation.
 */
public class TypeFactoryImpl implements TypeFactory {
    private Map<String, Instantiator> instantiators = new HashMap<>();

    @Override
    public TypedResource<?> instantiate(Map<String, Object> untyped, ResourceDefinition definition) {
        return instantiators
                .computeIfAbsent(definition.getSingular(), key -> {
                    throw new IllegalArgumentException("Unknown type: " + key);
                })
                .instantiate(untyped, definition, this);
    }

    @Override
    public void registerResource(String name, Instantiator instantiator) {
        instantiators.put(name, instantiator);
    }

}
