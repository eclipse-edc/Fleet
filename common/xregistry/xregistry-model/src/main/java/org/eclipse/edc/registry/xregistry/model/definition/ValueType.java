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

package org.eclipse.edc.registry.xregistry.model.definition;

/**
 * Defined value types.
 */
public enum ValueType {
    ANY,
    BOOLEAN,
    DECIMAL,
    INTEGER,
    XID,
    STRING,
    TIMESTAMP,
    UINTEGER,
    URI,
    URI_ABSOLUTE,
    URI_RELATIVE,
    URI_TEMPLATE,
    URL,
    URL_ABSOLUTE,
    URL_RELATIVE,
    ARRAY,
    MAP
}
