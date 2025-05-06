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

import org.eclipse.edc.registry.xregistry.model.definition.AbstractTypeDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.GroupDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.RegistryDefinition;

import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.eclipse.edc.registry.xregistry.library.validation.ValidationResult.invalidType;
import static org.eclipse.edc.registry.xregistry.library.validation.ValidationResult.success;

/**
 * Validates a registry entry.
 */
public class RegistryValidator implements RegistryTypeValidator<RegistryDefinition> {
    private RegistryTypeValidator<AbstractTypeDefinition> attributeValidator;
    private RegistryTypeValidator<GroupDefinition> groupValidator;

    public RegistryValidator(RegistryTypeValidator<GroupDefinition> groupValidator,
                             RegistryTypeValidator<AbstractTypeDefinition> attributeValidator) {
        this.groupValidator = requireNonNull(groupValidator, "groupValidator");
        this.attributeValidator = requireNonNull(attributeValidator, "attributeValidator");
    }

    public ValidationResult validate(Map<String, Object> registry, RegistryDefinition registryDefinition) {
        var result = attributeValidator.validate(registry, registryDefinition);

        // validate groups
        return registryDefinition.getGroups().values().stream()
                .map(groupDefinition -> {
                    var groupName = groupDefinition.getPlural();
                    var group = registry.get(groupName);
                    if (group == null) {
                        return success();
                    }
                    //noinspection rawtypes
                    if (group instanceof Map m) {
                        //noinspection unchecked
                        return groupValidator.validate(m, groupDefinition);
                    } else {
                        return invalidType("Registry." + groupName);
                    }
                })
                .reduce(result, ValidationResult::coalesce);
    }


}
