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
 *       Metaform Systems - initial API and implementation
 *
 */

plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    implementation(project(":registry:registry-spi"))
    implementation(project(":common:xregistry:xregistry-lib"))
    implementation(project(":common:xregistry:xregistry-policy"))
    implementation(project(":registry:registry-spi"))
    testImplementation(libs.edc.junit)
}
