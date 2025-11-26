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

package org.eclipse.edc.xregistry.processor;

import org.eclipse.edc.xregistry.processor.Artifact;
import org.eclipse.edc.xregistry.processor.FileParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FileParserTest {

    @Test
    void parseFilename_validFilename() {
        var filename = "dspace.example.1.0.0.json";
        var result = FileParser.parseFilename(filename);

        assertThat(result)
                .isNotNull()
                .satisfies(artifact -> {
                    assertThat(artifact.group()).isEqualTo("dspace");
                    assertThat(artifact.name()).isEqualTo("example");
                    assertThat(artifact.version()).isEqualTo("1.0.0");
                });
    }

    @Test
    void parseFilename_complexVersions() {
        var filename = "dspace.example.2.7.1.RELEASE.jar";
        var result = FileParser.parseFilename(filename);

        assertThat(result)
                .isNotNull()
                .extracting(Artifact::group, Artifact::name, Artifact::version)
                .containsExactly("dspace", "example", "2.7.1.RELEASE");
    }

    @Test
    void parseFilename_simpleVersion() {
        var filename = "dspace.example.1.jar";
        var result = FileParser.parseFilename(filename);

        assertThat(result)
                .isNotNull()
                .extracting(Artifact::group, Artifact::name, Artifact::version)
                .containsExactly("dspace", "example", "1");
    }

    @Test
    void parseFilename_complexVersion() {
        var filename = "dspace.example.4.1.82.Final.20221201.jar";
        var result = FileParser.parseFilename(filename);

        assertThat(result)
                .isNotNull()
                .extracting(Artifact::group, Artifact::name, Artifact::version)
                .containsExactly("dspace", "example", "4.1.82.Final.20221201");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void parseFilename_nullOrEmpty(String filename) {
        var result = FileParser.parseFilename(filename);
        assertThat(result).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "onlyonetoken",
            "two.tokens",
            "only.three.tokens",
            "no-dots-at-all",
            "starts.with.dot.",
    })
    void parseFilename_invalidFormat(String filename) {
        var result = FileParser.parseFilename(filename);
        assertThat(result).isNull();
    }




}