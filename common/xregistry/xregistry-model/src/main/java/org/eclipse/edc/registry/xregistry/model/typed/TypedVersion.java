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

import org.eclipse.edc.registry.xregistry.model.definition.VersionDefinition;

import java.util.Map;

/**
 * A typed view of an XRegistry resource version.
 */
public abstract class TypedVersion extends AbstractType<VersionDefinition> {
    protected VersionDefinition definition;

    protected TypedVersion(Map<String, Object> untyped, VersionDefinition definition, TypeFactory typeFactory) {
        super(untyped, definition, typeFactory);
        this.definition = definition;
    }

    public static class Builder<V extends VersionDefinition, B extends TypedVersion.Builder<V, B>>
            extends AbstractType.Builder<VersionDefinition, B> {

        protected Builder() {
        }
    }


}



