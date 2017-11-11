package com.jameskbride.adapter;

import okhttp3.Request;

public class Call {

    private final okhttp3.Call delegate;
    private final Request request;

    public Call(okhttp3.Call delegate, Request request) {
        this.delegate = delegate;
        this.request = request;
    }

    public void enqueue(Callback responseCallback) {
        delegate.enqueue(responseCallback);
    }

    public Request request() {
        return request;
    }
}
