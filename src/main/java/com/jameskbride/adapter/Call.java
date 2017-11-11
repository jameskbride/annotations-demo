package com.jameskbride.adapter;

import okhttp3.Request;

public class Call<T> {

    private final okhttp3.Call delegate;
    private final Request request;
    private Class responseType;

    public Call(okhttp3.Call delegate, Request request, Class responseType) {
        this.delegate = delegate;
        this.request = request;
        this.responseType = responseType;
    }

    public void enqueue(Callback<T> responseCallback) {
        delegate.enqueue(new InterceptingCallback(responseCallback, responseType));
    }

    public Request request() {
        return request;
    }
}
