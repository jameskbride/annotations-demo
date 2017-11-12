package com.jameskbride.adapter;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CallFactory<T> {

    private OkHttpClient client;
    private Class returnType;

    public CallFactory(OkHttpClient client, Class returnType) {
        this.client = client;
        this.returnType = returnType;
    }

    public Call<T> make(String baseUrl, String path) {
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(baseUrl + path)
                .headers(Headers.of("ContentType", "application/json"))
                .build();
        okhttp3.Call okHttpCall = client.newCall(request);

        return new Call<>(okHttpCall, request, returnType);
    }
}
