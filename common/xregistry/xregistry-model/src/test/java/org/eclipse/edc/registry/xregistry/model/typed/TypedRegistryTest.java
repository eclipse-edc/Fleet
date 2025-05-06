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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.edc.registry.xregistry.model.definition.RegistryDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.registry.xregistry.model.typed.TestSerializations.TYPED_REGISTRY;
import static org.mockito.Mockito.mock;

public class TypedRegistryTest {
    private ObjectMapper mapper;

    @Test
    @SuppressWarnings("unchecked")
    void verify_typedRegistry() throws JsonProcessingException {
        var untyped = mapper.readValue(TYPED_REGISTRY, Map.class);

        var registry = TypedRegistry.Builder.newInstance()
                .untyped(untyped)
                .definition(RegistryDefinition.Builder.newInstance().build())
                .typeFactory(mock(TypeFactory.class))
                .build();

        assertThat(registry.getId()).isNotNull();
        assertThat(registry.getCreatedAt()).isNotNull();
        assertThat(registry.getModifiedAt()).isNotNull();
        assertThat(registry.getEpoch()).isGreaterThan(0);
        assertThat(registry.getSelf()).isNotNull();
        assertThat(registry.getUrl()).isNotNull();
        assertThat(registry.getXid()).isNotNull();
    }

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }
}
