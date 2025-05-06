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

package org.eclipse.edc.registry.xregistry.schema.model.definition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.edc.registry.xregistry.library.validation.AttributeValidator;
import org.eclipse.edc.registry.xregistry.library.validation.GroupValidator;
import org.eclipse.edc.registry.xregistry.library.validation.RegistryValidator;
import org.eclipse.edc.registry.xregistry.model.definition.RegistryDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.registry.xregistry.schema.model.definition.RegistrySchemaDefinitions.createSchemaGroupDefinition;
import static org.eclipse.edc.registry.xregistry.schema.model.definition.SchemaDefinitions.SIMPLE_SCHEMA_RESOURCE;

class RegistrySchemaDefinitionsTest {
    private ObjectMapper mapper;
    private RegistryDefinition registry;
    private RegistryValidator validator;

    @Test
    void verify_createAndValidate() throws JsonProcessingException {
        var registryResult = mapper.readValue(SIMPLE_SCHEMA_RESOURCE, Map.class);

        @SuppressWarnings("unchecked")
        var result = validator.validate(registryResult, registry);

        assertThat(result.valid()).isTrue();
    }

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        registry = RegistryDefinition.Builder.newInstance().group(createSchemaGroupDefinition()).build();
        var attributeValidator = new AttributeValidator();
        var groupValidator = new GroupValidator(attributeValidator);
        validator = new RegistryValidator(groupValidator, attributeValidator);
    }

}