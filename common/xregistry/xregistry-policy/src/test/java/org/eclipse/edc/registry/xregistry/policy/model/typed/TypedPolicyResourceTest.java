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

package org.eclipse.edc.registry.xregistry.policy.model.typed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;
import org.eclipse.edc.registry.xregistry.model.typed.TypeFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.registry.xregistry.policy.model.fixture.TestSerializations.POLICY_RESOURCE;

class TypedPolicyResourceTest {
    private TypedPolicyResource resource;

    @Test
    void verify_typedGroup() {
        assertThat(resource.getId()).isNotNull();
        assertThat(resource.getSelf()).isNotNull();
        assertThat(resource.getXid()).isNotNull();

        Collection<TypedPolicyVersion> versions = resource.getVersions().values();
        assertThat(versions.size()).isEqualTo(1);
        assertThat(versions.iterator().next().getPolicyDefinition()).isNotNull();
        assertThat(versions.iterator().next().isControlPolicy()).isFalse();
        assertThat(versions.iterator().next().isAccessPolicy()).isFalse();
    }

    @Test
    void verify_modify() {
        var version = resource.getVersions().values().iterator().next();
        version.toBuilder()
                .controlPolicy(true)
                .accessPolicy(true)
                .policyDefinition("newpolicy")
                .build();

        assertThat(version.isControlPolicy()).isTrue();
        assertThat(version.isAccessPolicy()).isTrue();
        assertThat(version.getPolicyDefinition()).isEqualTo("newpolicy");
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var typeFactory = new TypeFactoryImpl();
        var untyped = mapper.readValue(POLICY_RESOURCE, Map.class);

        resource = TypedPolicyResource.Builder.newInstance()
                .untyped(untyped)
                .definition(ResourceDefinition.Builder.newInstance()
                        .singular("policy")
                        .plural("policies")
                        .build())
                .typeFactory(typeFactory)
                .build();
    }

}