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

package org.eclipse.edc.registry.reconciler.core.manager;

/**
 * Test definitions.
 */
public interface Definitions {

    String BASE_REGISTRY = """
            {
              "specversion": "0.5",
              "registryid": "sample",
              "self": "https://localhost:8080/xregistry",
              "xid": "/xregistry",
              "epoch": 1,
              "createdat": "2024-12-19T06:00:00Z",
              "modifiedat": "2024-12-19T06:00:00Z",
              "foogroupsurl": "#/foogroups/",
              "foogroupscount": 1,
              "foogroups": {
                "Fabrikam.Type1": {
                  "foogroupid": "Fabrikam.Type1",
                  "self": "#/foogroups/fabrikam.type1",
                  "xid": "/foogroups/fabrikam.type1",
                  "epoch": 1,
                  "createdat": "2024-12-19T06:00:00Z",
                  "modifiedat": "2024-12-19T06:00:00Z",
                  "foosurl": "#/foogroups/fabrikam.type1/entries",
                  "fooscount": 0,
                  "entries": {
                  }
                }
              }
            }""";
}
