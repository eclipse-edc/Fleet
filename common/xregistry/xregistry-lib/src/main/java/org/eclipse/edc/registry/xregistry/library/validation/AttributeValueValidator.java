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

import org.eclipse.edc.registry.xregistry.model.definition.ValueType;

import java.net.URI;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.NAME_VALIDATION;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.ARRAY;

/**
 * Validates an attribute value by type.
 */
public class AttributeValueValidator {
    private static final Map<ValueType, Predicate<Object>> VALIDATORS = new HashMap<>();

    static {
        VALIDATORS.put(ValueType.ANY, o -> true);
        VALIDATORS.put(ValueType.BOOLEAN, o -> o instanceof Boolean);
        VALIDATORS.put(ValueType.DECIMAL, o -> o instanceof Number);
        VALIDATORS.put(ValueType.INTEGER, o -> o instanceof Integer);
        VALIDATORS.put(ValueType.UINTEGER, o -> o instanceof Integer && ((Integer) o) >= 0);
        VALIDATORS.put(ValueType.STRING, o -> o instanceof String);
        VALIDATORS.put(ValueType.XID, o -> o instanceof String s && s.startsWith("/"));
        VALIDATORS.put(ValueType.URI, AttributeValueValidator::validateUri);
        VALIDATORS.put(ValueType.URI_ABSOLUTE, AttributeValueValidator::validateAbsoluteUri);
        VALIDATORS.put(ValueType.URI_RELATIVE, AttributeValueValidator::validateRelativeUri);
        VALIDATORS.put(ValueType.URI_TEMPLATE, AttributeValueValidator::validateUri);
        VALIDATORS.put(ValueType.URL, AttributeValueValidator::validateUri);
        VALIDATORS.put(ValueType.URL_ABSOLUTE, AttributeValueValidator::validateAbsoluteUri);
        VALIDATORS.put(ValueType.URL_RELATIVE, AttributeValueValidator::validateRelativeUri);
        VALIDATORS.put(ARRAY, o -> o instanceof List);

        VALIDATORS.put(ValueType.MAP, o -> {
            //noinspection rawtypes
            if (!(o instanceof Map m)) {
                return false;
            }
            for (var key : m.keySet()) {
                if (!(key instanceof String s) || s.length() > 63) {
                    return false;
                }
                if (!NAME_VALIDATION.matcher(s).matches()) {
                    return false;
                }
            }
            return true;
        });

        VALIDATORS.put(ValueType.TIMESTAMP, o -> {
            if (!(o instanceof String s)) {
                return false;
            }
            try {
                Instant.parse(s);
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        });

    }

    public static boolean validateAttributeValue(Object value, ValueType type) {
        requireNonNull(type, "type");
        return VALIDATORS.get(type).test(value);
    }

    public static boolean validateArrayValues(Object value, ValueType componentType) {
        requireNonNull(componentType, "componentType");
        if (!VALIDATORS.get(ARRAY).test(value)) {
            return false;
        }
        var arrayValue = (List<?>) value;
        var validator = VALIDATORS.get(componentType);
        for (var element : arrayValue) {
            if (!validator.test(element)) {
                return false;
            }
        }
        return true;
    }

    private static boolean validateRelativeUri(Object o) {
        try {
            if (!(o instanceof String s)) {
                return false;
            }
            return !URI.create(s).isAbsolute();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean validateAbsoluteUri(Object o) {
        try {
            if (!(o instanceof String s)) {
                return false;
            }
            return URI.create(s).isAbsolute();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean validateUri(Object o) {
        try {
            if (!(o instanceof String s)) {
                return false;
            }
            //noinspection ResultOfMethodCallIgnored
            URI.create(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
