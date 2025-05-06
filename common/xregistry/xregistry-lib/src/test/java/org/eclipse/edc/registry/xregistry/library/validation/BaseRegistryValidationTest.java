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

package org.eclipse.edc.registry.xregistry.library.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.edc.registry.xregistry.model.definition.GroupDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.RegistryDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.CREATED_AT;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.EPOCH;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.MODIFIED_AT;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.REGISTRY_ID;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.SELF;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.SPEC_VERSION;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.XID;

public class BaseRegistryValidationTest {
    private static final Set<String> MANDATORY_REGISTRY = Set.of(REGISTRY_ID, SPEC_VERSION, SELF, XID, EPOCH, CREATED_AT, MODIFIED_AT, "foogroupsurl", "foogroupscount");
    private static final Set<String> MANDATORY_GROUP = Set.of(SELF, XID, EPOCH, CREATED_AT, MODIFIED_AT, "foogroupid", "foosurl", "fooscount");
    private ObjectMapper mapper;
    private RegistryValidator registryValidator;

    @Test
    @SuppressWarnings("unchecked")
    void verify_validateUnknownGroupIgnored() throws JsonProcessingException {
        var data = mapper.readValue(VALID_REGISTRY, Map.class);
        var groupDefinition = GroupDefinition.Builder.newInstance()
                .singular("foogroup")
                .plural("foogroups")
                .build();
        var registryDefinition = RegistryDefinition.Builder.newInstance()
                .group(groupDefinition)
                .build();
        var result = registryValidator.validate(data, registryDefinition);

        assertThat(result.valid()).isTrue();
    }

    @Test
    @SuppressWarnings("unchecked")
    void verify_missingMandatoryRegistryAttributes_fails() throws JsonProcessingException {
        var data = mapper.readValue(MISSING_MANDATORY_REGISTRY_ATTRIBUTES, Map.class);
        var groupDefinition = GroupDefinition.Builder.newInstance()
                .singular("foogroup")
                .plural("foogroups")
                .build();
        var registryDefinition = RegistryDefinition.Builder.newInstance()
                .group(groupDefinition)
                .build();
        var result = registryValidator.validate(data, registryDefinition);

        assertThat(result.valid()).isFalse();
        assertThat(result.violations().size()).isEqualTo(MANDATORY_REGISTRY.size());
        assertThat(result.violations()).allMatch(v -> assertMatch(v, MANDATORY_REGISTRY));
    }

    @Test
    @SuppressWarnings("unchecked")
    void verify_missingGroupAttributes_fails() throws JsonProcessingException {
        var data = mapper.readValue(MISSING_MANDATORY_GROUP_ATTRIBUTES, Map.class);
        var resourceDefinition = ResourceDefinition.Builder.newInstance()
                .singular("foo")
                .plural("foos")
                .build();
        var groupDefinition = GroupDefinition.Builder.newInstance()
                .singular("foogroup")
                .plural("foogroups")
                .resource(resourceDefinition)
                .build();
        var registryDefinition = RegistryDefinition.Builder.newInstance()
                .group(groupDefinition)
                .build();
        var result = registryValidator.validate(data, registryDefinition);

        assertThat(result.valid()).isFalse();
        assertThat(result.violations().size()).isEqualTo(MANDATORY_GROUP.size());
        assertThat(result.violations()).allMatch(v -> assertMatch(v, MANDATORY_GROUP));
    }

    @Test
    @SuppressWarnings("unchecked")
    void verify_invalidTypes_fails() throws JsonProcessingException {
        var data = mapper.readValue(INVALID_TYPES_REGISTRY, Map.class);
        var groupDefinition = GroupDefinition.Builder.newInstance()
                .singular("foogroup")
                .plural("foogroups")
                .build();
        var registryDefinition = RegistryDefinition.Builder.newInstance()
                .group(groupDefinition)
                .build();
        var result = registryValidator.validate(data, registryDefinition);

        assertThat(result.valid()).isFalse();
        assertThat(result.violations().size()).isEqualTo(MANDATORY_REGISTRY.size());
        assertThat(result.violations()).allMatch(v -> assertMatch(v, MANDATORY_REGISTRY));
    }

    @BeforeEach
    void setUp() {
        var attributeValidator = new AttributeValidator();
        var groupValidator = new GroupValidator(attributeValidator);
        registryValidator = new RegistryValidator(groupValidator, attributeValidator);

        mapper = new ObjectMapper();
    }

    private boolean assertMatch(String v, Set<String> set) {
        boolean found = false;
        for (var key : set) {
            if (v.contains(key)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private static final String VALID_REGISTRY = """
            {
              "specversion": "0.5",
              "registryid": "sample",
              "self": "https://localhost:8080/xregistry",
              "xid": "/xregistry",
              "epoch": 1,
              "createdat": "2024-12-19T06:00:00Z",
              "modifiedat": "2024-12-19T06:00:00Z",
              "foogroupsurl": "#/foogroups/",
              "foogroupscount": 1,
              "foogroups": {
                "Fabrikam.Type1": {
                  "foogroupid": "Fabrikam.Type1",
                  "self": "#/foogroups/fabrikam.type1",
                  "xid": "/foogroups/fabrikam.type1",
                  "epoch": 1,
                  "createdat": "2024-12-19T06:00:00Z",
                  "modifiedat": "2024-12-19T06:00:00Z",
                  "foosurl": "#/foogroups/fabrikam.type1/entries",
                  "fooscount": 0,
                  "entries": {
                  }
                }
              }
            }""";

    private static final String MISSING_MANDATORY_GROUP_ATTRIBUTES = """
            {
              "specversion": "0.5",
              "registryid": "sample",
              "self": "https://localhost:8080/xregistry",
              "xid": "/xregistry",
              "epoch": 1,
              "createdat": "2024-12-19T06:00:00Z",
              "modifiedat": "2024-12-19T06:00:00Z",
              "foogroupsurl": "#/foogroups/",
              "foogroupscount": 1,
              "foogroups": {
                "Fabrikam.Type1": {
                  "entries": {
                  }
                }
              }
            }""";

    private static final String INVALID_TYPES_REGISTRY = """
            {
              "specversion": 5,
              "registryid": 1,
              "self": 1,
              "xid": 1,
              "epoch": "epoch",
              "createdat": 1,
              "modifiedat":1
            }""";

    private static final String MISSING_MANDATORY_REGISTRY_ATTRIBUTES = "{}";
}
