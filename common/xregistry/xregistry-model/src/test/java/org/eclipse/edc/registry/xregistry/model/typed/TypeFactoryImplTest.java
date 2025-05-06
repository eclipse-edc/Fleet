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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TypeFactoryImplTest {
    private TypeFactoryImpl factory;

    @Test
    void validate_instantiate() {
        var definition = ResourceDefinition.Builder.newInstance()
                .singular("resource")
                .plural("resources")
                .build();

        assertThat(factory.instantiate(Map.of(), definition)).isNotNull();
    }

    @Test
    void validate_unknownType_fails() {
        var definition = ResourceDefinition.Builder.newInstance()
                .singular("unknown")
                .plural("unknowns")
                .build();

        assertThatThrownBy(() -> factory.instantiate(Map.of(), definition)).isInstanceOf(IllegalArgumentException.class);
    }

    @BeforeEach
    void setUp() {
        factory = new TypeFactoryImpl();
        factory.registerResource("resource", TypedMockResource::new);
    }
}