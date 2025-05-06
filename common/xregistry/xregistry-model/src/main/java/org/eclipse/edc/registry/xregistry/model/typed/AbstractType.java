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

import org.eclipse.edc.registry.xregistry.model.definition.AbstractTypeDefinition;
import org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.COUNT;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.CREATED_AT;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.EPOCH;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.ID;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.MODIFIED_AT;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.SELF;
import static org.eclipse.edc.registry.xregistry.model.definition.RegistryConstants.XID;

/**
 * Base type.
 * <p>
 * Subclasses provide a typed view on untyped XRegistry models.
 */
public abstract class AbstractType<D extends AbstractTypeDefinition> {
    protected Map<String, Object> untyped;
    protected Map<String, Object> unmodifiableUntyped;

    protected D definition;

    protected final TypeFactory typeFactory;

    protected AbstractType(Map<String, Object> untyped, D definition, TypeFactory typeFactory) {
        this.untyped = requireNonNull(untyped, "untyped");
        this.unmodifiableUntyped = unmodifiableMap(untyped);
        this.definition = definition;
        this.typeFactory = requireNonNull(typeFactory, "typeFactory");
    }

    public D getDefinition() {
        return definition;
    }

    public String getId() {
        return getString(definition.getSingular() + ID);
    }

    public int getCollectionCount(String collectionName) {
        return getInt(collectionName + COUNT);
    }

    public URL getCollectionUrl(String collectionName) {
        return getUrl(collectionName + RegistryConstants.URL);
    }

    public int getEpoch() {
        return getInt(EPOCH);
    }

    public Instant getCreatedAt() {
        return getTimestamp(CREATED_AT);
    }

    public Instant getModifiedAt() {
        return getTimestamp(MODIFIED_AT);
    }

    public String getSelf() {
        return getString(SELF);
    }

    public String getXid() {
        return getString(XID);
    }

    public Object get(String name) {
        return untyped.get(name);
    }

    public String getString(String name) {
        var value = untyped.get(name);
        return value == null ? null : value.toString();
    }

    public Integer getInteger(String name) {
        var value = untyped.get(name);
        return value == null ? null : (Integer) value;
    }

    public int getInt(String name) {
        var value = untyped.get(name);
        return value == null ? 0 : (Integer) value;
    }

    public boolean getBool(String name) {
        var value = untyped.get(name);
        return value != null && (Boolean) value;
    }

    public URL getUrl(String key) {
        var value = untyped.get(key);
        try {
            return value == null ? null : new URL((String) value);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Instant getTimestamp(String key) {
        var value = untyped.get(key);
        if (value == null) {
            return null;
        } else {
            if (!(value instanceof String)) {
                throw new IllegalArgumentException(format("Illegal value for key '%s': %s", key, value));
            }
            try {
                return Instant.parse((String) value);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException(format("Illegal timestamp value for key '%s': %s", key, value));
            }
        }
    }

    /**
     * Returns an unmodifiable view of the data as a map.
     */
    public Map<String, Object> asMap() {
        return unmodifiableUntyped;
    }

    Map<String, Object> getUntyped() {
        return untyped;
    }

    public static class Builder<D extends AbstractTypeDefinition, B extends Builder<D, B>> {
        protected Map<String, Object> untyped;
        protected D definition;
        protected TypeFactory typeFactory;

        @SuppressWarnings("unchecked")
        public B untyped(Map<String, Object> untyped) {
            this.untyped = untyped;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B definition(D definition) {
            this.definition = definition;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B typeFactory(TypeFactory typeFactory) {
            this.typeFactory = typeFactory;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B id(String id) {
            checkModifiableState();
            this.untyped.put(definition.getSingular() + ID, id);
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B epoch(int epoch) {
            checkModifiableState();
            this.untyped.put(EPOCH, epoch);
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B createdAt(Instant timestamp) {
            checkModifiableState();
            this.untyped.put(CREATED_AT, timestamp.toString());
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B modifiedAt(Instant timestamp) {
            checkModifiableState();
            this.untyped.put(MODIFIED_AT, timestamp.toString());
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B self(String self) {
            checkModifiableState();
            this.untyped.put(SELF, self);
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B xid(String xid) {
            checkModifiableState();
            this.untyped.put(XID, xid);
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B set(String key, Object value) {
            checkModifiableState();
            this.untyped.put(key, value);
            return (B) this;
        }

        protected void validate() {
            requireNonNull(untyped, "untyped");
            requireNonNull(definition, "definition");
            requireNonNull(typeFactory, "typeFactory");
        }

        protected void checkModifiableState() {
            if (untyped == null) {
                throw new IllegalStateException("Untyped entity must be set prior a modification operation");
            }
        }

        public Builder() {
        }
    }

}
