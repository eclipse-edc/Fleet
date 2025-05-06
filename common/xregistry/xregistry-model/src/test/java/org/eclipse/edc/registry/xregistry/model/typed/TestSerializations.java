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

/**
 * XRegistry serializations for testing.
 */
public interface TestSerializations {

    String TYPED_REGISTRY = """
            {
              "specversion": "0.5",
              "registryid": "sample",
              "self": "https://localhost:8080/xregistry",
              "xid": "/xregistry",
              "url": "https://localhost:8080/xregistry",
              "epoch": 1,
              "createdat": "2024-12-19T06:00:00Z",
              "modifiedat": "2024-12-19T06:00:00Z",
              "testgroupsurl": "#/testgroups",
              "testgroupscount": 1,
              "testgroups": {
                "test.group1": {
                  "testgroupid": "test.group1",
                  "self": "#/testgroups/test.group1",
                  "xid": "/testgroups/test.group1",
                  "epoch": 1,
                  "createdat": "2024-12-19T06:00:00Z",
                  "modifiedat": "2024-12-19T06:00:00Z",
                  "entriesurl": "#/testgroups/test.group1/entries",
                  "entriescount": 0,
                  "entries": {
                    "entry1": {
                      "entryid": "entry1",
                      "self": "#/testgroups/test.group1/entries/entry1",
                      "xid": "/testgroups/test.group1/entries/entry1",
                      "metaurl": "#/testgroups/test.group1/entries/entry1/meta",
                      "versionsurl": "#/testgroups/test.group1/entries/entry1/versions",
                      "versionscount": 0,
                      "description:": "AssertscorporateheadquarterlocationisintheEU",
                      "versions": {
                        "1.0": {
                          "entryid": "entry1",
                          "versionid": "1.0",
                          "self": "#/testgroups/test.group1/entries/entry1/versions/1.0",
                          "xid": "/testgroups/test.group1/entries/entry1/versions/1.0",
                          "epoch": 1,
                          "isdefault": true,
                          "createdat": "2024-12-19T06:00:00Z",
                          "modifiedat": "2024-12-19T06:00:00Z",
                          "entrydefinition": "{\\"@context\\":{\\"edc\\":\\"https://w3id.org/edc/v0.0.1/ns/\\"},\\"@type\\":\\"entryDefinition\\",\\"entry\\":{\\"@context\\":\\"http://www.w3.org/ns/odrl.jsonld\\",\\"@id\\":\\"8c2ff88a-74bf-41dd-9b35-9587a3b95adf\\",\\"duty\\":[{\\"target\\":\\"http://example.com/asset:12345\\",\\"action\\":\\"use\\",\\"constraint\\":{\\"leftOperand\\":\\"headquarter_location\\",\\"operator\\":\\"eq\\",\\"rightOperand\\":\\"EU\\"}}]}\\n}"
                        }
                      }
                    }
                  }
                }
              }
            }
            """;

    String TYPED_GROUP = """
            {
              "testgroupid": "test.group1",
              "self": "#/testgroups/test.group1",
              "xid": "/testgroups/test.group1",
              "epoch": 1,
              "createdat": "2024-12-19T06:00:00Z",
              "modifiedat": "2024-12-19T06:00:00Z",
              "entriesurl": "#/testgroups/test.group1/entries",
              "entriescount": 0,
              "entries": {
                "entry1": {
                  "entryid": "entry1",
                  "self": "#/testgroups/test.group1/entries/entry1",
                  "xid": "/testgroups/test.group1/entries/entry1",
                  "metaurl": "#/testgroups/test.group1/entries/entry1/meta",
                  "versionsurl": "#/testgroups/test.group1/entries/entry1/versions",
                  "versionscount": 0,
                  "description:": "AssertscorporateheadquarterlocationisintheEU",
                  "versions": {
                    "1.0": {
                      "entryid": "entry1",
                      "versionid": "1.0",
                      "self": "#/testgroups/test.group1/entries/entry1/versions/1.0",
                      "xid": "/testgroups/test.group1/entries/entry1/versions/1.0",
                      "epoch": 1,
                      "isdefault": true,
                      "createdat": "2024-12-19T06:00:00Z",
                      "modifiedat": "2024-12-19T06:00:00Z",
                      "entrydefinition": "{\\"@context\\":{\\"edc\\":\\"https://w3id.org/edc/v0.0.1/ns/\\"},\\"@type\\":\\"entryDefinition\\",\\"entry\\":{\\"@context\\":\\"http://www.w3.org/ns/odrl.jsonld\\",\\"@id\\":\\"8c2ff88a-74bf-41dd-9b35-9587a3b95adf\\",\\"duty\\":[{\\"target\\":\\"http://example.com/asset:12345\\",\\"action\\":\\"use\\",\\"constraint\\":{\\"leftOperand\\":\\"headquarter_location\\",\\"operator\\":\\"eq\\",\\"rightOperand\\":\\"EU\\"}}]}\\n}"
                    }
                  }
                }
              }
            }
            """;

    String TYPED_RESOURCE = """
            {
               "entryid": "entry1",
               "self": "#/testgroups/test.group1/entries/entry1",
               "xid": "/testgroups/test.group1/entries/entry1",
               "metaurl": "#/testgroups/test.group1/entries/entry1/meta",
               "versionsurl": "#/testgroups/test.group1/entries/entry1/versions",
               "versionscount": 0,
               "description:": "AssertscorporateheadquarterlocationisintheEU",
               "versions": {
                 "1.0": {
                   "entryid": "entry1",
                   "versionid": "1.0",
                   "self": "#/testgroups/test.group1/entries/entry1/versions/1.0",
                   "xid": "/testgroups/test.group1/entries/entry1/versions/1.0",
                   "epoch": 1,
                   "isdefault": true,
                   "createdat": "2024-12-19T06:00:00Z",
                   "modifiedat": "2024-12-19T06:00:00Z",
                   "entrydefinition": "{\\"@context\\":{\\"edc\\":\\"https://w3id.org/edc/v0.0.1/ns/\\"},\\"@type\\":\\"entryDefinition\\",\\"entry\\":{\\"@context\\":\\"http://www.w3.org/ns/odrl.jsonld\\",\\"@id\\":\\"8c2ff88a-74bf-41dd-9b35-9587a3b95adf\\",\\"duty\\":[{\\"target\\":\\"http://example.com/asset:12345\\",\\"action\\":\\"use\\",\\"constraint\\":{\\"leftOperand\\":\\"headquarter_location\\",\\"operator\\":\\"eq\\",\\"rightOperand\\":\\"EU\\"}}]}\\n}"
                 },
                 "0.2": {
                   "entryid": "entry1",
                   "versionid": "0.2",
                   "self": "#/testgroups/test.group1/entries/entry1/versions/1.0",
                   "xid": "/testgroups/test.group1/entries/entry1/versions/1.0",
                   "epoch": 0,
                   "isdefault": true,
                   "createdat": "2023-12-19T06:00:00Z",
                   "modifiedat": "2023-12-19T06:00:00Z",
                   "entrydefinition": "{\\"@context\\":{\\"edc\\":\\"https://w3id.org/edc/v0.0.1/ns/\\"},\\"@type\\":\\"entryDefinition\\",\\"entry\\":{\\"@context\\":\\"http://www.w3.org/ns/odrl.jsonld\\",\\"@id\\":\\"8c2ff88a-74bf-41dd-9b35-9587a3b95adf\\",\\"duty\\":[{\\"target\\":\\"http://example.com/asset:12345\\",\\"action\\":\\"use\\",\\"constraint\\":{\\"leftOperand\\":\\"headquarter_location\\",\\"operator\\":\\"eq\\",\\"rightOperand\\":\\"EU\\"}}]}\\n}"
                 }
               }
            }
            """;
}
