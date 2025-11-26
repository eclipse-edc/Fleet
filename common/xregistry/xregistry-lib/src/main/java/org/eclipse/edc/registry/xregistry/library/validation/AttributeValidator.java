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
import org.eclipse.edc.registry.xregistry.model.definition.AttributeDefinition;

import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static org.eclipse.edc.registry.xregistry.library.validation.AttributeValueValidator.validateArrayValues;
import static org.eclipse.edc.registry.xregistry.library.validation.AttributeValueValidator.validateAttributeValue;
import static org.eclipse.edc.registry.xregistry.library.validation.RegistryTypeValidator.Mode.CLIENT;
import static org.eclipse.edc.registry.xregistry.library.validation.RegistryTypeValidator.Mode.SERVER;
import static org.eclipse.edc.registry.xregistry.library.validation.ValidationResult.failure;
import static org.eclipse.edc.registry.xregistry.library.validation.ValidationResult.missingProperty;
import static org.eclipse.edc.registry.xregistry.library.validation.ValidationResult.success;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.ID;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.ARRAY;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.MAP;

/**
 * Validates attributes on an entry.
 */
public class AttributeValidator implements RegistryTypeValidator<AbstractTypeDefinition> {
    private RegistryValidator.Mode mode = SERVER;

    @Override
    public ValidationResult validate(Map<String, Object> entry, AbstractTypeDefinition typeDefinition) {
        var context = calculateContext(entry, typeDefinition);
        return typeDefinition.getAttributes().values().stream()
                .map(attributeDefinition -> validateAttribute(entry, attributeDefinition, context))
                .reduce(success(), ValidationResult::coalesce);
    }

    private ValidationResult validateAttribute(Map<String, Object> entry,
                                               AttributeDefinition attributeDefinition,
                                               String context) {
        var value = entry.get(attributeDefinition.getName());
        if (value == null && mode == SERVER && attributeDefinition.serverRequired()) {
            return missingProperty(context + "." + attributeDefinition.getName());
        } else if (value == null && mode == CLIENT && attributeDefinition.clientRequired()) {
            return missingProperty(context + "." + attributeDefinition.getName());
        } else if (value == null) {
            // optional
            return success();
        }
        var type = attributeDefinition.getType();
        var result = validateAttributeValue(value, type) ? success()
                : failure(Set.of(format("Invalid property type %s for %s: %s",
                type.toString(),
                context + "." + attributeDefinition.getName(),
                value)));
        if (!result.valid()) {
            return result;
        }
        if (MAP == type) {
            @SuppressWarnings("unchecked") // cast is safe because keys are validated
            var mapValue = (Map<String, Object>) value;
            return attributeDefinition.getAttributes()
                    .values()
                    .stream()
                    .map(subAttributeDefinition -> validateAttribute(mapValue, subAttributeDefinition, context + "." + attributeDefinition.getName()))
                    .reduce(result, ValidationResult::coalesce);
        } else if (ARRAY == type) {
            var componentType = attributeDefinition.getComponentType();
            return validateArrayValues(value, componentType) ? success()
                    : failure(Set.of(format("Property %s contains an invalid element. Types must be: %s",
                    context + "." + attributeDefinition.getName(),
                    componentType)));

        }
        var constraints = attributeDefinition.getTypeConstraintsChecker().apply(value);
        if (constraints == null) {
            return result;
        }
        return result.coalesce(failure(Set.of(format("Invalid property type %s for %s: %s",
                type.toString(),
                context + "." + attributeDefinition.getName(),
                constraints))));
    }

    /**
     * Calculates the context, including an entity ID if present.
     */
    private String calculateContext(Map<String, Object> entry, AbstractTypeDefinition typeDefinition) {
        var entityId = entry.get(typeDefinition.getSingular() + ID);
        return typeDefinition.getContext() + (entityId != null ? "[" + entityId + "]" : "");
    }

}
