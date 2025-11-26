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

package org.eclipse.edc.xregistry.oci.publisher;

import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.jspecify.annotations.NonNull;

import static org.eclipse.edc.xregistry.oci.publisher.Constants.XREGISTRY_SOURCE_DIR;

/**
 * Gradle extension for the xRegistry OCI publisher plugin.
 * <p></p>
 * The plugin can be applied and configured as follows:
 * <pre>
 *
 * plugins {
 *     id("org.eclipse.edc.xregistry-oci-publisher")
 * }
 *
 * configure<XRegistryOciPublisherExtension> {
 *     xRegistrySourceDir.set("src/main/xregistry")
 *
 *     ociArtifactName.set("ghcr.io/acme/xr-sample")
 *     ociArtifactTag.set("1.0")
 *
 *     // Registry authentication
 *     ociRegistryUsername.set("user")
 *     ociRegistryPassword.set(System.getenv("OCI_REGISTRY_PASSWORD") ?: "")
 *
 *     // Custom manifest annotations
 *     manifestAnnotations.set(mapOf(
 *         "org.opencontainers.image.title" to "XRegistry Policy Bundle",
 *         "org.opencontainers.image.description" to "Fleet management policies",
 *         "org.opencontainers.image.version" to project.version.toString(),
 *         "org.opencontainers.image.created" to java.time.Instant.now().toString(),
 *         "org.opencontainers.image.authors" to "ACME, Inc."))
 * }
 *
 * </pre>
 *
 */
public abstract class XRegistryOciPublisherExtension {

    /**
     * Source directory for xRegistry artifacts.
     */
    public abstract Property<@NonNull String> getXRegistrySourceDir();

    /**
     * Name to publish the xRegistry archive as to the OCI registry.
     */
    public abstract Property<@NonNull String> getOciArtifactName();

    /**
     * xRegistry archive version.
     */
    public abstract Property<@NonNull String> getOciArtifactTag();

    /**
     * Registry username for authentication.
     */
    public abstract Property<@NonNull String> getOciRegistryUsername();

    /**
     * Registry password for authentication.
     */
    public abstract Property<@NonNull String> getOciRegistryPassword();

    /**
     * Uses HTTP instead of HTTPS for publishing when testing.
     */
    public abstract Property<@NonNull Boolean> getInsecure();

    /**
     * Sets OCI manifest annotations.
     */
    public abstract MapProperty<@NonNull String, @NonNull String> getManifestAnnotations();

    public XRegistryOciPublisherExtension() {
        getXRegistrySourceDir().convention(XREGISTRY_SOURCE_DIR);
        getInsecure().convention(false);
    }
}