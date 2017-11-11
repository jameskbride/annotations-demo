package com.jameskbride.adapter;

import com.google.gson.Gson;

import java.io.IOException;

public class InterceptingCallback<T> implements okhttp3.Callback {

    private Class responseType;
    Callback<T> responseCallback;

    public InterceptingCallback(Callback<T> responseCallback, Class responseType) {
        this.responseType = responseType;
        this.responseCallback = responseCallback;
    }

    @Override
    public void onFailure(okhttp3.Call call, IOException e) {

    }

    @Override
    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
        Gson gson = new Gson();
        Object deserializedResponse = gson.fromJson(response.body().string(), responseType);
        Response<T> tResponse = new Response<T>((T) deserializedResponse);
        responseCallback.onResponse(call, tResponse);
    }
}
