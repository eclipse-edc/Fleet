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
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.registry.xregistry.model.definition.ValueType.STRING;

class AttributeValidatorTest {
    private static final String CONSTRAINT_FAILURE = "constraint failure";
    private static final String KEY = "test";

    @Test
    void verify_constraintsValidation() {
        var validator = new AttributeValidator();

        var f = validator.validate(Map.of(KEY, KEY), new TestDefinition());

        assertThat(f.valid()).isFalse();
        assertThat(f.violations()).allMatch(v -> v.contains(CONSTRAINT_FAILURE));
    }


    public static class TestDefinition extends AbstractTypeDefinition {

        TestDefinition() {
            singular = "test";
            plural = "tests";
            setContext("test");
            attributes.put(KEY, AttributeDefinition.Builder.newInstance()
                    .name(KEY)
                    .type(STRING)
                    .typeConstraintsChecker(o -> CONSTRAINT_FAILURE)
                    .build());
        }
    }


}