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
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.registry.xregistry.library.validation.AttributeValueValidator.validateArrayValues;
import static org.eclipse.edc.registry.xregistry.library.validation.AttributeValueValidator.validateAttributeValue;

class AttributeValueValidatorTest {

    @Test
    void validate_scalars() {
        assertThat(validateAttributeValue(1, ValueType.ANY)).isTrue();
        assertThat(validateAttributeValue(null, ValueType.ANY)).isTrue();

        assertThat(validateAttributeValue(1, ValueType.DECIMAL)).isTrue();
        assertThat(validateAttributeValue("2", ValueType.DECIMAL)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.DECIMAL)).isFalse();

        assertThat(validateAttributeValue(1, ValueType.INTEGER)).isTrue();
        assertThat(validateAttributeValue("2", ValueType.INTEGER)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.INTEGER)).isFalse();

        assertThat(validateAttributeValue(1, ValueType.UINTEGER)).isTrue();
        assertThat(validateAttributeValue(-1, ValueType.UINTEGER)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.UINTEGER)).isFalse();

        assertThat(validateAttributeValue("TEST", ValueType.STRING)).isTrue();
        assertThat(validateAttributeValue(1, ValueType.STRING)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.STRING)).isFalse();

        assertThat(validateAttributeValue(true, ValueType.BOOLEAN)).isTrue();
        assertThat(validateAttributeValue("false", ValueType.BOOLEAN)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.BOOLEAN)).isFalse();

        assertThat(validateAttributeValue("1996-12-19T16:39:57-08:00", ValueType.TIMESTAMP)).isTrue();
        assertThat(validateAttributeValue("1990-12-31T23:59:60Z", ValueType.TIMESTAMP)).isTrue();
        assertThat(validateAttributeValue("-1990-12-31T23:59:60Z", ValueType.BOOLEAN)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.BOOLEAN)).isFalse();

    }

    @Test
    void validate_collections() {
        assertThat(validateAttributeValue(List.of(), ValueType.ARRAY)).isTrue();
        assertThat(validateAttributeValue(new Object(), ValueType.ARRAY)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.ARRAY)).isFalse();

        assertThat(validateAttributeValue(Map.of(), ValueType.MAP)).isTrue();
        assertThat(validateAttributeValue(Map.of("a-._a", "value"), ValueType.MAP)).isTrue();
        assertThat(validateAttributeValue(Map.of("aa", "value"), ValueType.MAP)).isTrue();
        assertThat(validateAttributeValue(Map.of(1, "value"), ValueType.MAP)).isFalse();
        assertThat(validateAttributeValue(Map.of("1aaa", "value"), ValueType.MAP)).isFalse();
        assertThat(validateAttributeValue(Map.of("k:ey", "value"), ValueType.MAP)).isFalse();
        assertThat(validateAttributeValue(Map.of("a".repeat(65), "value"), ValueType.MAP)).isFalse();
        assertThat(validateAttributeValue(new Object(), ValueType.MAP)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.MAP)).isFalse();
    }

    @Test
    void validate_references() {
        assertThat(validateAttributeValue("/", ValueType.XID)).isTrue();
        assertThat(validateAttributeValue("/test/test", ValueType.XID)).isTrue();
        assertThat(validateAttributeValue("invalid", ValueType.XID)).isFalse();
        assertThat(validateAttributeValue("http://test.com", ValueType.XID)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.XID)).isFalse();

        assertThat(validateAttributeValue("/test", ValueType.URI)).isTrue();
        assertThat(validateAttributeValue("https://test.com", ValueType.URI)).isTrue();
        assertThat(validateAttributeValue(1, ValueType.URI)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.URI)).isFalse();

        assertThat(validateAttributeValue("https://test.com", ValueType.URI_ABSOLUTE)).isTrue();
        assertThat(validateAttributeValue("/test", ValueType.URI_ABSOLUTE)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.URI_ABSOLUTE)).isFalse();

        assertThat(validateAttributeValue("/test", ValueType.URI_RELATIVE)).isTrue();
        assertThat(validateAttributeValue("https://test.com", ValueType.URI_RELATIVE)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.URI_RELATIVE)).isFalse();

        assertThat(validateAttributeValue("https://test.com", ValueType.URL)).isTrue();
        assertThat(validateAttributeValue("/test", ValueType.URL)).isTrue();
        assertThat(validateAttributeValue(1, ValueType.URL)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.URL)).isFalse();

        assertThat(validateAttributeValue("https://test.com", ValueType.URL_ABSOLUTE)).isTrue();
        assertThat(validateAttributeValue("/test", ValueType.URL_ABSOLUTE)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.URL_ABSOLUTE)).isFalse();

        assertThat(validateAttributeValue("/test", ValueType.URL_RELATIVE)).isTrue();
        assertThat(validateAttributeValue("https://test.com", ValueType.URL_RELATIVE)).isFalse();
        assertThat(validateAttributeValue(null, ValueType.URL_RELATIVE)).isFalse();
    }

    @Test
    void validate_array_types() {
        assertThat(validateArrayValues(List.of("string", "string"), ValueType.STRING)).isTrue();
        assertThat(validateArrayValues(List.of("string", 1), ValueType.STRING)).isFalse();
        assertThat(validateArrayValues("test", ValueType.STRING)).isFalse();  // not an array of strings
    }

}