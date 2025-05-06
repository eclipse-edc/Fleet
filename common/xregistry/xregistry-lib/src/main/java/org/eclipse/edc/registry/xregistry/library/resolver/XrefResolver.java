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

package org.eclipse.edc.registry.xregistry.library.resolver;

import org.eclipse.edc.registry.xregistry.library.result.Result;

import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.eclipse.edc.registry.xregistry.library.result.Result.error;
import static org.eclipse.edc.registry.xregistry.library.result.Result.notFound;
import static org.eclipse.edc.registry.xregistry.library.result.Result.success;

/**
 * Resolves {@code xref} attributes against a root registry document.
 */
public class XrefResolver {
    public static Result<Map<String, Object>> resolveXref(String xref, Map<String, Object> root) {
        requireNonNull(xref, "xRef");
        requireNonNull(root, "root");
        if (!xref.startsWith("/")) {
            return error("An xref must start with '/'");
        } else if (xref.length() == 1) {
            return error("Invalid xref :" + xref);
        }
        var paths = xref.substring(1).split("/"); // strip leading '/'
        var current = root;
        for (int i = 0; i < paths.length - 1; i++) {
            var path = paths[i];
            var value = current.get(path);
            if (value == null) {
                return notFound();
            } else if (!(value instanceof Map)) {
                return error("Path type must be a map :" + path);
            }
            //noinspection unchecked
            current = (Map<String, Object>) value;
        }
        //noinspection unchecked
        return success((Map<String, Object>) current.get(paths[paths.length - 1]));
    }
}
