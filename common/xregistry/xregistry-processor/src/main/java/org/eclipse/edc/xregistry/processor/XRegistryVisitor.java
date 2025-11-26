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

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Visitor interface for processing stream items with callbacks. Each visitor implementation defines how to handle
 * stream data.
 * <p>
 * Note that artifact references may be invoked multiple times to provide the actual stream data.
 */
public interface XRegistryVisitor {

    /**
     * Called when processing starts.
     */
    default void onStart() {
    }

    /**
     * Called when a policy is encountered.
     */
    void onPolicy(Artifact artifact, Supplier<InputStream> ref);

    /**
     * Called when a schema is encountered.
     */
    void onSchema(Artifact artifact, Supplier<InputStream> ref);

    /**
     * Called when a rule is encountered.
     */
    void onRule(Artifact artifact, Supplier<InputStream> ref);

    /**
     * Called when an error is encountered.
     */
    void onError(String problem);

    /**
     * Called when processing completes successfully.
     */
    default void onComplete() {
    }


}
