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

import org.eclipse.edc.xregistry.processor.DeploymentIndexVisitor;
import org.eclipse.edc.xregistry.processor.ExpandedFileSystemWalker;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ExpandedFileSystemWalkerTest {

    @Test
    void verifyExpanded() {
        var visitor = new DeploymentIndexVisitor();
        var resourcesPath = Paths.get("src/test/resources/xregistry-expanded");

        var walker = new ExpandedFileSystemWalker(visitor);
        walker.walk(resourcesPath);

        assertThat(visitor.validate().valid()).isTrue();

        var index = visitor.getIndex();
        assertThat(index.artifacts().size()).isEqualTo(1);
        assertThat(index.policies().size()).isEqualTo(1);
    }
}