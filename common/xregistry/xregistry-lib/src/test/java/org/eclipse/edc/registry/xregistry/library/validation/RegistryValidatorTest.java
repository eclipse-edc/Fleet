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
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.registry.xregistry.library.validation.ValidationResult.missingProperty;
import static org.eclipse.edc.registry.xregistry.library.validation.ValidationResult.success;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistryValidatorTest {
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    @SuppressWarnings("unchecked")
    void verify_validation() throws JsonProcessingException {
        var groupValidator = mock(RegistryTypeValidator.class);
        when(groupValidator.validate(isA(Map.class), isA(GroupDefinition.class))).thenReturn(success());

        var attributeValidator = mock(RegistryTypeValidator.class);
        when(attributeValidator.validate(isA(Map.class), isA(RegistryDefinition.class))).thenReturn(success());

        var validator = new RegistryValidator(groupValidator, attributeValidator);

        var data = mapper.readValue(EXAMPLE, Map.class);

        var fooGroupDefinition = GroupDefinition.Builder.newInstance()
                .singular("foogroup")
                .plural("foogroups")
                .build();

        var barGroupDefinition = GroupDefinition.Builder.newInstance()
                .singular("bargroup")
                .plural("bargroups")
                .build();

        var bazGroupDefinition = GroupDefinition.Builder.newInstance()
                .singular("bazgroup")
                .plural("bazgroups")
                .build();

        var registryDefinition = RegistryDefinition.Builder.newInstance()
                .group(fooGroupDefinition)
                .group(barGroupDefinition)
                .group(bazGroupDefinition)
                .build();

        var result = validator.validate(data, registryDefinition);

        assertThat(result.valid()).isTrue();
        verify(groupValidator, times(3)).validate(isA(Map.class), isA(GroupDefinition.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void verify_invalidGroup_fails() throws JsonProcessingException {
        var groupValidator = mock(RegistryTypeValidator.class);
        when(groupValidator.validate(isA(Map.class), isA(GroupDefinition.class)))
                .thenReturn(missingProperty("prop1"))
                .thenReturn(success())
                .thenReturn(missingProperty("prop2"));

        var attributeValidator = mock(RegistryTypeValidator.class);
        when(attributeValidator.validate(isA(Map.class), isA(RegistryDefinition.class))).thenReturn(success());

        var validator = new RegistryValidator(groupValidator, attributeValidator);

        var data = mapper.readValue(EXAMPLE, Map.class);

        var fooGroupDefinition = GroupDefinition.Builder.newInstance()
                .singular("foogroup")
                .plural("foogroups")
                .build();

        var barGroupDefinition = GroupDefinition.Builder.newInstance()
                .singular("bargroup")
                .plural("bargroups")
                .build();

        var bazGroupDefinition = GroupDefinition.Builder.newInstance()
                .singular("bazgroup")
                .plural("bazgroups")
                .build();

        var registryDefinition = RegistryDefinition.Builder.newInstance()
                .group(fooGroupDefinition)
                .group(barGroupDefinition)
                .group(bazGroupDefinition)
                .build();

        var result = validator.validate(data, registryDefinition);

        assertThat(result.valid()).isFalse();

        assertThat(result.violations())
                .containsSequence(missingProperty("prop1").coalesce(missingProperty("prop2")).violations());
    }

    @Test
    @SuppressWarnings("unchecked")
    void verify_validateUnknownGroupIgnored() throws JsonProcessingException {
        var groupValidator = mock(RegistryTypeValidator.class);
        when(groupValidator.validate(isA(Map.class), isA(GroupDefinition.class))).thenReturn(success());

        var attributeValidator = mock(RegistryTypeValidator.class);
        when(attributeValidator.validate(isA(Map.class), isA(RegistryDefinition.class))).thenReturn(success());

        var validator = new RegistryValidator(groupValidator, attributeValidator);

        var data = mapper.readValue(EXAMPLE, Map.class);

        var fooGroupDefinition = GroupDefinition.Builder.newInstance()
                .singular("foogroup")
                .plural("foogroups")
                .build();

        var barGroupDefinition = GroupDefinition.Builder.newInstance()
                .singular("bargroup")
                .plural("bargroups")
                .build();

        var registryDefinition = RegistryDefinition.Builder.newInstance()
                .group(fooGroupDefinition)
                .group(barGroupDefinition)
                .build();

        var result = validator.validate(data, registryDefinition);

        assertThat(result.valid()).isTrue();

        // two invocations, the bazgroups entry must be ignored
        verify(groupValidator, times(2)).validate(isA(Map.class), isA(GroupDefinition.class));
    }

    private static final String EXAMPLE = """
            {
              "specversion": "0.5",
              "registryid": "foo-registry",
              "foogroups": {
                "Fabrikam.Type1": {
                  "entries": {
                  }
                }
              },
              "bargroups": {
                "Fabrikam.Type2": {
                  "entries": {
                  }
                }
              },
              "bazgroups": {
                "Fabrikam.Type3": {
                  "entries": {
                  }
                }
              }
            }""";

}