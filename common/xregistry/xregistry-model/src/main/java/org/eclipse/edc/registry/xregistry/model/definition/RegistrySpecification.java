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

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Specifies an XRegistry and the extensible types it supports.
 */
public class RegistrySpecification {
    private Map<String, GroupDefinition> groups = new HashMap<>();
    private Map<String, ResourceDefinition> resources = new HashMap<>();
    private String url;

    public RegistrySpecification(String url) {
        this.url = requireNonNull(url, "url");
    }

    public String getUrl() {
        return url;
    }

    public void registerGroup(GroupDefinition group) {
        groups.put(group.getPlural(), group);
        group.getResources().values().forEach(resource -> resources.put(resource.getSingular(), resource));
    }

    public Map<String, GroupDefinition> getGroupDefinitions() {
        return groups;
    }

    public GroupDefinition getGroupDefinition(String type) {
        return groups.get(type);
    }

    public Map<String, ResourceDefinition> getResourceDefinitions() {
        return resources;
    }

    public ResourceDefinition getResourceDefinition(String type) {
        return resources.get(type);
    }

    public RegistryDefinition getRegistryDefinition() {
        var builder = RegistryDefinition.Builder.newInstance();
        groups.values().forEach(builder::group);
        return builder.build();
    }

}
