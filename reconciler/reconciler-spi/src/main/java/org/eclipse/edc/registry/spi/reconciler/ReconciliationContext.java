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

package org.eclipse.edc.registry.spi.reconciler;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to propagate reconciliation data
 */
public class ReconciliationContext {
    private Map<Class<?>, Object> cache = new HashMap<>();

    @SuppressWarnings("unchecked")
    <T> T getData(Class<T> type) {
        return (T) cache.get(type);
    }

    <T> void setData(Class<T> type, T data) {
        cache.put(type, data);
    }
}
