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

import java.util.Map;

/**
 * Validates a registry entry against a type definition.
 */
public interface RegistryTypeValidator<T extends AbstractTypeDefinition> {
    enum Mode {
        CLIENT, SERVER
    }

    /**
     * Validates the entry.
     */
    ValidationResult validate(Map<String, Object> entry, T definition);
}
