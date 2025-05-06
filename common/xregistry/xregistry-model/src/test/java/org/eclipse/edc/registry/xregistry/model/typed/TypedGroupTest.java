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

package org.eclipse.edc.registry.xregistry.model.typed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.edc.registry.xregistry.model.definition.GroupDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.registry.xregistry.model.typed.TestSerializations.TYPED_GROUP;

class TypedGroupTest {
    private TypedGroup group;
    private ResourceDefinition resourceDefinition;
    private TypeFactory typeFactory;

    @Test
    void verify_typedGroup() throws JsonProcessingException {
        TypedMockResource resource = group.getResource("entry1");
        assertThat(resource).isNotNull();

        Collection<TypedMockResource> resources = group.getResourcesOfType(TypedMockResource.class);
        assertThat(resources.size()).isEqualTo(1);
        assertThat(resources.iterator().next().getId()).isNotNull();

        assertThat(group.getId()).isNotNull();
        assertThat(group.getCreatedAt()).isNotNull();
        assertThat(group.getModifiedAt()).isNotNull();
        assertThat(group.getEpoch()).isGreaterThan(0);
        assertThat(group.getSelf()).isNotNull();
        assertThat(group.getXid()).isNotNull();
    }

    @Test
    void verify_modify_resources() {
        var builder = group.toBuilder();
        var resource = TypedMockResource.Builder
                .newInstance()
                .untyped(Map.of("entryid", "entry2"))
                .definition(resourceDefinition)
                .typeFactory(typeFactory)
                .build();

        var modified = builder.resource(resource).build();

        var resources = modified.getResourcesOfType(TypedMockResource.class);
        assertThat(resources.size()).isEqualTo(2);
        assertThat(resources).allMatch(r -> r.getId().equals("entry1") || r.getId().equals("entry2"));

        modified = modified.toBuilder()
                .deleteResource("entry1")
                .build();

        resources = modified.getResourcesOfType(TypedMockResource.class);
        assertThat(resources.size()).isEqualTo(1);
        assertThat(resources).allMatch(r -> r.getId().equals("entry2"));
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        typeFactory = new TypeFactoryImpl();
        typeFactory.registerResource("entry", TypedMockResource::new);

        var untyped = mapper.readValue(TYPED_GROUP, Map.class);

        resourceDefinition = ResourceDefinition.Builder.newInstance()
                .singular("entry")
                .plural("entries")
                .build();
        group = TypedGroup.Builder.newInstance()
                .untyped(untyped)
                .definition(GroupDefinition.Builder.newInstance()
                        .singular("testgroup")
                        .plural("testgroups")
                        .resource(resourceDefinition)
                        .build())
                .typeFactory(typeFactory)
                .build();


    }

}