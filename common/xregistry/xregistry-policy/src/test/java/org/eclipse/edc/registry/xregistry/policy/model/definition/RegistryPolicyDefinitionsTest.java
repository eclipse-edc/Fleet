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

package org.eclipse.edc.registry.xregistry.policy.model.definition;

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
import static org.eclipse.edc.registry.xregistry.policy.model.definition.RegistryPolicyDefinitions.createPolicyGroupDefinition;
import static org.eclipse.edc.registry.xregistry.policy.model.fixture.TestSerializations.POLICY_REGISTRY;

class RegistryPolicyDefinitionsTest {
    private ObjectMapper mapper;
    private RegistryDefinition registry;
    private RegistryValidator validator;

    @Test
    void verify_createAndValidate() throws JsonProcessingException {
        var policy = mapper.readValue(POLICY_REGISTRY, Map.class);

        @SuppressWarnings("unchecked")
        var result = validator.validate(policy, registry);

        assertThat(result.valid()).isTrue();
    }

    @Test
    void verify_validateMissingDefinitionProperty_fails() throws JsonProcessingException {
        var policy = mapper.readValue(POLICY_RESOURCE_MISSING_PROPERTY, Map.class);

        @SuppressWarnings("unchecked")
        var result = validator.validate(policy, registry);

        assertThat(result.valid()).isFalse();
        assertThat(result.violations()).allMatch(v -> v.contains("policydefinition"));
    }

    @Test
    void verify_validateInvalidResourceType_fails() throws JsonProcessingException {
        var policy = mapper.readValue(INVALID_POLICY_RESOURCE_TYPE, Map.class);

        @SuppressWarnings("unchecked")
        var result = validator.validate(policy, registry);

        assertThat(result.valid()).isFalse();
        assertThat(result.violations()).allMatch(v -> v.toLowerCase().contains("invalid type"));
    }

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        registry = RegistryDefinition.Builder.newInstance().group(createPolicyGroupDefinition()).build();
        var attributeValidator = new AttributeValidator();
        var groupValidator = new GroupValidator(attributeValidator);
        validator = new RegistryValidator(groupValidator, attributeValidator);
    }

    private static final String POLICY_RESOURCE_MISSING_PROPERTY = """
            {
              "specversion": "0.5",
              "registryid": "sample",
              "self": "https://localhost:8080/xregistry",
              "xid": "/xregistry",
              "epoch": 1,
              "createdat": "2024-12-19T06:00:00Z",
              "modifiedat": "2024-12-19T06:00:00Z",
              "policygroupsurl": "#/policygroups",
              "policygroupscount": 1,
              "policygroups": {
                "Corporate.Policies": {
                  "policygroupid": "Corporate.Policies",
                  "description": "Corporate policies",
                  "self": "#/policygroups/corporate.policies",
                  "xid": "/policygroups/corporate.policies",
                  "epoch": 1,
                  "createdat": "2024-12-19T06:00:00Z",
                  "modifiedat": "2024-12-19T06:00:00Z",
                  "policiesurl": "#/policygroups/corporate.policies/policies",
                  "policiescount": 0,
                  "policies": {
                    "Corporate.Headquarters.EU": {
                        "policyid": "Corporate.Headquarters.EU",
                        "self": "#/policygroups/corporate.policies/policies/corporate.headquarters.eu",
                        "xid": "/policygroups/corporate.policies/policies/corporate.headquarters.eu",
                        "metaurl": "#/policygroups/corporate.policies/policies/corporate.headquarters.eu/meta",
                        "versionsurl": "#/policygroups/corporate.policies/policies/corporate.headquarters.eu/url",
                        "versionscount": 1,
                        "versions": {
                          "1.0": {
                            "policyid": "Corporate.Headquarters.EU",
                            "versionid": "1.0",
                            "self": "#/policygroups/corporate.policies/policies/corporate.headquarters.eu/versions/1.0",
                            "xid": "/policygroups/corporate.policies/policies/corporate.headquarters.eu/versions/1.0",
                            "epoch": 1,
                            "isdefault": true,
                            "createdat": "2024-12-19T06:00:00Z",
                            "modifiedat": "2024-12-19T06:00:00Z"
                          }
                        }
                      }
                  }
                }
              }
            }""";

    private static final String INVALID_POLICY_RESOURCE_TYPE = """
            {
              "specversion": "0.5",
              "registryid": "sample",
              "self": "https://localhost:8080/xregistry",
              "xid": "/xregistry",
              "epoch": 1,
              "createdat": "2024-12-19T06:00:00Z",
              "modifiedat": "2024-12-19T06:00:00Z",
              "policygroupsurl": "#/policygroups",
              "policygroupscount": 1,
              "policygroups": {
                "Corporate.Policies": "string"
              }
            }""";


}