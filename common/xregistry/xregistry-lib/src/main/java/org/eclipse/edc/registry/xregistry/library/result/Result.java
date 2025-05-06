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

package org.eclipse.edc.registry.xregistry.library.result;

/**
 * Result of an operation.
 */
public class Result<T> {
    public enum Status {
        SUCCESS,
        ERROR,
        NOT_FOUND
    }

    private T content;
    private String error;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public T getContent() {
        return content;
    }

    public String getError() {
        return error;
    }

    public static <T> Result<T> success(T content) {
        return new Result<T>(content);
    }

    public static <T> Result<T> error(String error) {
        return new Result<T>(error);
    }

    public static <T> Result<T> notFound() {
        return new Result<T>();
    }

    private Result() {
        this.status = Status.NOT_FOUND;
    }

    private Result(T content) {
        this.content = content;
        this.status = Status.SUCCESS;
    }

    private Result(String error) {
        this.error = error;
        this.status = Status.ERROR;
    }
}
