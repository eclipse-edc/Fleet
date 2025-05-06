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

import java.util.LinkedHashSet;
import java.util.Set;

import static java.lang.String.format;

/**
 * The result of a validation operation.
 */
public class ValidationResult {
    private boolean valid;
    private Set<String> violations = new LinkedHashSet<>();

    public boolean valid() {
        return valid;
    }

    public Set<String> violations() {
        return violations;
    }

    /**
     * Coalesces this validation with the other by adding violations, if present.
     */
    public ValidationResult coalesce(ValidationResult other) {
        if (other.valid()) {
            return this;
        }
        valid = false;
        violations.addAll(other.violations);
        return this;
    }

    public static ValidationResult success() {
        return new ValidationResult();
    }

    public static ValidationResult failure(Set<String> violations) {
        return new ValidationResult(violations);
    }

    public static ValidationResult missingProperty(String property) {
        return new ValidationResult(Set.of("Missing property: " + property));
    }

    public static ValidationResult invalidPropertyType(String property, String type, String actual) {
        return new ValidationResult(Set.of(format("Invalid property type for %s. Expecting %s but was: %s", property, type, actual)));
    }

    public static ValidationResult invalidType(String property) {
        return new ValidationResult(Set.of(format("Invalid type for " + property)));
    }

    private ValidationResult() {
        this.valid = true;
    }

    private ValidationResult(Set<String> violations) {
        this.valid = false;
        this.violations.addAll(violations);
    }
}
