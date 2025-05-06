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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.registry.xregistry.library.resolver.XrefResolver.resolveXref;
import static org.eclipse.edc.registry.xregistry.library.result.Result.Status.ERROR;
import static org.eclipse.edc.registry.xregistry.library.result.Result.Status.NOT_FOUND;
import static org.eclipse.edc.registry.xregistry.library.result.Result.Status.SUCCESS;

class XrefResolverTest {
    private Map<String, Object> root;

    @Test
    void verify_resolve() {
        var result = resolveXref("/schemagroups/group2/schemas/sharedSchema", root);
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getContent()).containsKey("schema");

        result = resolveXref("/schemagroups/group2/schemas", root);
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getContent()).containsKey("sharedSchema");

        result = resolveXref("/schemagroups/group2", root);
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getContent()).containsKey("schemas");

        result = resolveXref("/schemagroups", root);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsKey("group2");
    }

    @Test
    void verify_invalidRoot_fails() {
        assertThat(resolveXref("/", root).getStatus()).isEqualTo(ERROR);
    }

    @Test
    void verify_invalidPath_notFound() {
        assertThat(resolveXref("/schemagroups/group3/schemas", root).getStatus()).isEqualTo(NOT_FOUND);
    }

    @BeforeEach
    void setUp() {
        root = Map.of("schemagroups",
                Map.of("group2", Map.of("schemas", Map.of("sharedSchema",
                        Map.of("schema", "schema")))));
    }
}