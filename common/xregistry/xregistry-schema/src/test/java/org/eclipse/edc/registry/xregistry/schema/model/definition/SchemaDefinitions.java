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

package org.eclipse.edc.registry.xregistry.schema.model.definition;

public interface SchemaDefinitions {

    String SIMPLE_SCHEMA_RESOURCE = """
            {
              "specversion": "0.5",
              "registryid": "CloudEvents",
              "self": "#/",
              "xid": "/",
              "epoch": 1,
              "name": "CloudEvents Registry",
              "description": "An impl of the CloudEvents xReg spec",
              "documentation": "https://github.com/duglin/xreg-github",
              "createdat": "2025-02-27T12:52:48.703439224Z",
              "modifiedat": "2025-02-27T12:52:48.703439224Z",
            
              "capabilities": {
                "enforcecompatibility": false,
                "flags": [
                  "doc",
                  "epoch",
                  "filter",
                  "inline",
                  "nodefaultversionid",
                  "nodefaultversionsticky",
                  "noepoch",
                  "noreadonly",
                  "offered",
                  "schema",
                  "setdefaultversionid",
                  "specversion"
                ],
                "maxmaxversions": 0,
                "mutable": [
                  "capabilities",
                  "entities",
                  "model"
                ],
                "pagination": false,
                "schemas": [
                  "xregistry-json/0.5"
                ],
                "shortself": false,
                "specversions": [
                  "0.5"
                ],
                "sticky": true
              },
              "model": {
                "attributes": {
                  "specversion": {
                    "name": "specversion",
                    "type": "string",
                    "readonly": true,
                    "immutable": true,
                    "serverrequired": true
                  },
                  "registryid": {
                    "name": "registryid",
                    "type": "string",
                    "immutable": true,
                    "serverrequired": true
                  },
                  "self": {
                    "name": "self",
                    "type": "url",
                    "readonly": true,
                    "serverrequired": true
                  },
                  "xid": {
                    "name": "xid",
                    "type": "xid",
                    "readonly": true,
                    "serverrequired": true
                  },
                  "epoch": {
                    "name": "epoch",
                    "type": "uinteger",
                    "serverrequired": true
                  },
                  "name": {
                    "name": "name",
                    "type": "string"
                  },
                  "description": {
                    "name": "description",
                    "type": "string"
                  },
                  "documentation": {
                    "name": "documentation",
                    "type": "url"
                  },
                  "labels": {
                    "name": "labels",
                    "type": "map",
                    "item": {
                      "type": "string"
                    }
                  },
                  "createdat": {
                    "name": "createdat",
                    "type": "timestamp",
                    "serverrequired": true
                  },
                  "modifiedat": {
                    "name": "modifiedat",
                    "type": "timestamp",
                    "serverrequired": true
                  }
                },
                "groups": {
                  "schemagroups": {
                    "plural": "schemagroups",
                    "singular": "schemagroup",
                    "attributes": {
                      "schemagroupid": {
                        "name": "schemagroupid",
                        "type": "string",
                        "immutable": true,
                        "serverrequired": true
                      },
                      "self": {
                        "name": "self",
                        "type": "url",
                        "readonly": true,
                        "serverrequired": true
                      },
                      "xid": {
                        "name": "xid",
                        "type": "xid",
                        "readonly": true,
                        "serverrequired": true
                      },
                      "epoch": {
                        "name": "epoch",
                        "type": "uinteger",
                        "serverrequired": true
                      },
                      "name": {
                        "name": "name",
                        "type": "string"
                      },
                      "description": {
                        "name": "description",
                        "type": "string"
                      },
                      "documentation": {
                        "name": "documentation",
                        "type": "url"
                      },
                      "labels": {
                        "name": "labels",
                        "type": "map",
                        "item": {
                          "type": "string"
                        }
                      },
                      "createdat": {
                        "name": "createdat",
                        "type": "timestamp",
                        "serverrequired": true
                      },
                      "modifiedat": {
                        "name": "modifiedat",
                        "type": "timestamp",
                        "serverrequired": true
                      },
                      "*": {
                        "name": "*",
                        "type": "any"
                      }
                    },
                    "resources": {
                      "schemas": {
                        "plural": "schemas",
                        "singular": "schema",
                        "maxversions": 0,
                        "setversionid": true,
                        "setdefaultversionsticky": true,
                        "hasdocument": true,
                        "attributes": {
                          "schemaid": {
                            "name": "schemaid",
                            "type": "string",
                            "immutable": true,
                            "serverrequired": true
                          },
                          "versionid": {
                            "name": "versionid",
                            "type": "string",
                            "immutable": true,
                            "serverrequired": true
                          },
                          "self": {
                            "name": "self",
                            "type": "url",
                            "readonly": true,
                            "serverrequired": true
                          },
                          "xid": {
                            "name": "xid",
                            "type": "xid",
                            "readonly": true,
                            "serverrequired": true
                          },
                          "epoch": {
                            "name": "epoch",
                            "type": "uinteger",
                            "serverrequired": true
                          },
                          "name": {
                            "name": "name",
                            "type": "string"
                          },
                          "isdefault": {
                            "name": "isdefault",
                            "type": "boolean",
                            "readonly": true,
                            "serverrequired": true,
                            "default": false
                          },
                          "description": {
                            "name": "description",
                            "type": "string"
                          },
                          "documentation": {
                            "name": "documentation",
                            "type": "url"
                          },
                          "labels": {
                            "name": "labels",
                            "type": "map",
                            "item": {
                              "type": "string"
                            }
                          },
                          "createdat": {
                            "name": "createdat",
                            "type": "timestamp",
                            "serverrequired": true
                          },
                          "modifiedat": {
                            "name": "modifiedat",
                            "type": "timestamp",
                            "serverrequired": true
                          },
                          "contenttype": {
                            "name": "contenttype",
                            "type": "string"
                          },
                          "format": {
                            "name": "format",
                            "type": "string",
                            "description": "Schema format identifier for this schema version"
                          },
                          "*": {
                            "name": "*",
                            "type": "any"
                          }
                        },
                        "metaattributes": {
                          "schemaid": {
                            "name": "schemaid",
                            "type": "string",
                            "immutable": true,
                            "serverrequired": true
                          },
                          "self": {
                            "name": "self",
                            "type": "url",
                            "readonly": true,
                            "serverrequired": true
                          },
                          "xid": {
                            "name": "xid",
                            "type": "xid",
                            "readonly": true,
                            "serverrequired": true
                          },
                          "xref": {
                            "name": "xref",
                            "type": "url"
                          },
                          "epoch": {
                            "name": "epoch",
                            "type": "uinteger",
                            "serverrequired": true
                          },
                          "createdat": {
                            "name": "createdat",
                            "type": "timestamp",
                            "serverrequired": true
                          },
                          "modifiedat": {
                            "name": "modifiedat",
                            "type": "timestamp",
                            "serverrequired": true
                          },
                          "readonly": {
                            "name": "readonly",
                            "type": "boolean",
                            "serverrequired": true,
                            "default": false
                          },
                          "compatibility": {
                            "name": "compatibility",
                            "type": "string",
                            "enum": [
                              "none",
                              "backward",
                              "backward_transitive",
                              "forward",
                              "forward_transitive",
                              "full",
                              "full_transitive"
                            ],
                            "strict": false,
                            "serverrequired": true,
                            "default": "none"
                          },
                          "defaultversionid": {
                            "name": "defaultversionid",
                            "type": "string",
                            "serverrequired": true
                          },
                          "defaultversionurl": {
                            "name": "defaultversionurl",
                            "type": "url",
                            "readonly": true,
                            "serverrequired": true
                          },
                          "defaultversionsticky": {
                            "name": "defaultversionsticky",
                            "type": "boolean",
                            "readonly": true,
                            "serverrequired": true,
                            "default": false
                          },
                          "validation": {
                            "name": "validation",
                            "type": "boolean",
                            "description": "Verify compliance with specified schema 'format'"
                          }
                        }
                      }
                    }
                  }
                }
              },
              "schemagroupsurl": "#/schemagroups",
              "schemagroups": {
                "g1": {
                  "schemagroupid": "g1",
                  "self": "#/schemagroups/g1",
                  "xid": "/schemagroups/g1",
                  "epoch": 1,
                  "createdat": "2025-02-27T12:52:48.728291165Z",
                  "modifiedat": "2025-02-27T12:52:48.728291165Z",
                  "format": "text",
            
                  "schemasurl": "#/schemagroups/g1/schemas",
                  "schemas": {
                    "popped": {
                      "schemaid": "popped",
                      "self": "#/schemagroups/g1/schemas/popped",
                      "xid": "/schemagroups/g1/schemas/popped",
            
                      "metaurl": "#/schemagroups/g1/schemas/popped/meta",
                      "meta": {
                        "schemaid": "popped",
                        "self": "#/schemagroups/g1/schemas/popped/meta",
                        "xid": "/schemagroups/g1/schemas/popped/meta",
                        "epoch": 1,
                        "createdat": "2025-02-27T12:52:48.728291165Z",
                        "modifiedat": "2025-02-27T12:52:48.728291165Z",
                        "readonly": false,
                        "compatibility": "none",
            
                        "defaultversionid": "v2.0",
                        "defaultversionurl": "#/schemagroups/g1/schemas/popped/versions/v2.0",
                        "defaultversionsticky": false
                      },
                      "versionsurl": "#/schemagroups/g1/schemas/popped/versions",
                      "versions": {
                        "v1.0": {
                          "schemaid": "popped",
                          "versionid": "v1.0",
                          "self": "#/schemagroups/g1/schemas/popped/versions/v1.0",
                          "xid": "/schemagroups/g1/schemas/popped/versions/v1.0",
                          "epoch": 1,
                          "isdefault": false,
                          "createdat": "2025-02-27T12:52:48.728291165Z",
                          "modifiedat": "2025-02-27T12:52:48.728291165Z",
                          "format": "text"
                        },
                        "v2.0": {
                          "schemaid": "popped",
                          "versionid": "v2.0",
                          "self": "#/schemagroups/g1/schemas/popped/versions/v2.0",
                          "xid": "/schemagroups/g1/schemas/popped/versions/v2.0",
                          "epoch": 1,
                          "isdefault": true,
                          "createdat": "2025-02-27T12:52:48.728291165Z",
                          "modifiedat": "2025-02-27T12:52:48.728291165Z",
                          "format": "text"
                        }
                      },
                      "versionscount": 2
                    }
                  },
                  "schemascount": 1
                }
              },
              "schemagroupscount": 1
            }""";

}
