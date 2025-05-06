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

import java.util.regex.Pattern;

/**
 * Constants.
 */
public interface RegistryConstants {

    Pattern NAME_VALIDATION = Pattern.compile("^[a-z]{1}[a-z0-9-_.]{1,61}$");  // per spec

    String URL = "url";

    String CAPABILITIES = "capabilities";

    String COUNT = "count";

    String CREATED_AT = "createdat";

    String DESCRIPTION = "description";

    String DOCUMENTATION = "documentation";

    String ENFORCE_COMPAT = "enforcecompatibility";

    String EPOCH = "epoch";

    String FLAGS = "flags";

    String ID = "id";

    String IS_DEFAULT = "isdefault";

    String LABELS = "labels";

    String META = "meta";

    String META_URL = "meta" + URL;

    String MODEL = "model";

    String MODIFIED_AT = "modifiedat";

    String MUTABLE = "mutable";

    String NAME = "name";

    String PAGINATION = "pagination";

    String REGISTRIES = "registries";

    String REGISTRY = "registry";

    String REGISTRY_ID = REGISTRY + ID;

    String SCHEMAS = "schemas";

    String SELF = "self";

    String SHORT_SELF = "shortself";

    String SPEC_VERSION = "specversion";

    String SPEC_VERSIONS = SPEC_VERSION + "s";

    String VERSION = "version";

    String VERSIONS = VERSION + "s";

    String VERSIONS_COUNT = VERSIONS + COUNT;

    String VERSIONS_URL = VERSIONS + URL;

    String XID = "xid";
}

