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
import org.eclipse.edc.registry.xregistry.model.definition.RegistryDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.ResourceDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.ID;
import static org.eclipse.edc.registry.xregistry.model.typed.TestSerializations.TYPED_REGISTRY;

class TypeManipulationTest {
    private static final String TESTGROUP = "testgroup";
    private static final String TESTGROUPS = "testgroups";
    private static final String ENTRY = "entry";
    private static final String ENTRIES = "entries";

    private TypedRegistry registry;
    private TypeFactoryImpl typeFactory;
    private GroupDefinition groupDefinition;

    @Test
    void verify_readRegistry() throws JsonProcessingException {
        Map<String, TypedGroup> testGroups = registry.getGroups(TESTGROUPS);
        assertThat(testGroups.size()).isEqualTo(1);

        TypedGroup typedGroup = testGroups.get("test.group1");
        assertThat(typedGroup.getResources().size()).isEqualTo(1);

        TypedMockResource resource = typedGroup.getResource("entry1");
        assertThat(resource.getId()).isEqualTo("entry1");
    }

    @Test
    void verify_modifyResource() {
        Map<String, TypedGroup> testGroups = registry.getGroups(TESTGROUPS);
        TypedGroup typedGroup = testGroups.get("test.group1");

        var resource = typedGroup.getResourcesOfType(TypedMockResource.class).iterator().next();

        var modified = resource.toBuilder().removeVersion("1.0").build();
        assertThat(modified.getVersions()).isEmpty();

        // check root was updated
        assertThat(registry.getGroups(TESTGROUPS).get("test.group1")
                .getResourcesOfType(TypedMockResource.class).iterator().next().getVersions()).isEmpty();
    }

    @Test
    void verify_modifyGroup() {
        registry.toBuilder().removeGroup("test.group1").build();

        Map<String, TypedGroup> testGroups = registry.getGroups(TESTGROUPS);
        assertThat(testGroups.containsKey("test.group1")).isFalse();

        var newGroup = TypedGroup.Builder.newInstance()
                .typeFactory(typeFactory)
                .definition(groupDefinition)
                .untyped(Map.of(groupDefinition.getSingular() + ID, "test.group2"))
                .build();

        registry.toBuilder().group(newGroup).build();

        assertThat(registry.getGroups(TESTGROUPS).get("test.group2")).isNotNull();
    }

    @Test
    void verify_modifyScalars() {
        registry.toBuilder()
                .id("newid")
                .epoch(123)
                .createdAt(Instant.ofEpochMilli(1))
                .modifiedAt(Instant.ofEpochMilli(2))
                .self("/newself")
                .xid("/newxid")
                .build();
        assertThat(registry.getId()).isEqualTo("newid");
        assertThat(registry.getEpoch()).isEqualTo(123);
        assertThat(registry.getCreatedAt()).isEqualTo(Instant.ofEpochMilli(1));
        assertThat(registry.getModifiedAt()).isEqualTo(Instant.ofEpochMilli(2));
        assertThat(registry.getSelf()).isEqualTo("/newself");
        assertThat(registry.getXid()).isEqualTo("/newxid");

    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws JsonProcessingException {
        var mapper = new ObjectMapper();
        typeFactory = new TypeFactoryImpl();
        typeFactory.registerResource(ENTRY, TypedMockResource::new);

        groupDefinition = GroupDefinition.Builder.newInstance()
                .singular(TESTGROUP)
                .plural(TESTGROUPS)
                .resource(ResourceDefinition.Builder.newInstance().singular(ENTRY).plural(ENTRIES).build())
                .build();
        var registryDefinition = RegistryDefinition.Builder.newInstance()
                .group(groupDefinition)
                .build();

        var entry = mapper.readValue(TYPED_REGISTRY, Map.class);

        registry = TypedRegistry.Builder.newInstance()
                .untyped(entry)
                .definition(registryDefinition)
                .typeFactory(typeFactory)
                .build();
    }


}

