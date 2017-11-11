package com.jameskbride;

import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Call implements okhttp3.Call {

    private final okhttp3.Call okhttpCall;
    private final Request request;

    public Call(okhttp3.Call okhttpCall, Request request) {
        this.okhttpCall = okhttpCall;
        this.request = request;
    }

    @Override
    public void enqueue(Callback responseCallback) {

    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response execute() throws IOException {
        return null;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public okhttp3.Call clone() {
        return null;
    }
}
