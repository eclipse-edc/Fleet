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
import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;

import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.eclipse.edc.registry.xregistry.library.validation.ValidationResult.invalidType;
import static org.eclipse.edc.registry.xregistry.library.validation.ValidationResult.success;

/**
 * Validates a group entry.
 */
public class GroupValidator implements RegistryTypeValidator<GroupDefinition> {
    private RegistryTypeValidator<AbstractTypeDefinition> attributeValidator;

    public GroupValidator(RegistryTypeValidator<AbstractTypeDefinition> attributeValidator) {
        this.attributeValidator = requireNonNull(attributeValidator, "attributeValidator");
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ValidationResult validate(Map<String, Object> groupContainer, GroupDefinition groupDefinition) {
        return groupContainer.entrySet().stream().map(groupEntry -> {
            if (!(groupEntry.getValue() instanceof Map groupMap)) {
                // groups must be a map
                return invalidType(format("%s[%s]", groupDefinition.getContext(), groupEntry.getKey()));
            }
            var result = attributeValidator.validate(groupMap, groupDefinition);
            return groupDefinition.getResources().values().stream().map(resourceDefinition -> {
                var resources = groupMap.get(resourceDefinition.getPlural());
                if (resources == null) {
                    return success();
                }
                if (!(resources instanceof Map resourceMap)) {
                    var context = format("%s[%s].%s",
                            groupDefinition.getContext(),
                            groupEntry.getKey(),
                            resourceDefinition.getPlural());
                    return invalidType(context);
                }
                Set<Map.Entry> resourceSet = resourceMap.entrySet();
                return resourceSet.stream()
                        .map(resourceEntry -> validateResource(
                                resourceEntry,
                                groupDefinition.getContext(),
                                groupEntry.getKey(),
                                resourceDefinition))
                        .reduce(success(), ValidationResult::coalesce);
            }).reduce(result, ValidationResult::coalesce);
        }).reduce(success(), ValidationResult::coalesce);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private ValidationResult validateResource(Map.Entry<String, Object> resourceEntry,
                                              String rootContext,
                                              String groupKey,
                                              ResourceDefinition resourceDefinition) {
        if (!(resourceEntry.getValue() instanceof Map resourceEntryMap)) {
            var context = format("%s.%s[%s]", rootContext, resourceDefinition.getPlural(), resourceEntry.getKey());
            return invalidType(context);
        }
        // validate the resource
        var resourceResult = attributeValidator.validate(resourceEntryMap, resourceDefinition);

        // validate resource versions
        var versions = resourceEntryMap.get("versions");
        if (versions == null) {
            return resourceResult;
        }
        if (!(versions instanceof Map versionMap)) {
            var context = format("%s[%s].%s[%s].versions",
                    rootContext,
                    groupKey,
                    resourceDefinition.getPlural(),
                    resourceEntry.getKey());
            return invalidType(context);
        }
        Set<Map.Entry<String, Object>> versionSet = versionMap.entrySet();
        return versionSet.stream().map(versionEntry -> {
            if (!(versionEntry.getValue() instanceof Map versionEntryMap)) {
                var context = format("%s.%s[%s].[%s]",
                        rootContext,
                        resourceDefinition.getPlural(),
                        resourceEntry.getKey(),
                        versionEntry.getKey());
                return invalidType(context);
            }
            return attributeValidator.validate(versionEntryMap, resourceDefinition.getVersionDefinition());
        }).reduce(resourceResult, ValidationResult::coalesce);
    }

}