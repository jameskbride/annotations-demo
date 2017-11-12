package com.jameskbride.adapter;

import com.jameskbride.adapter.Call;
import okhttp3.OkHttpClient;

public class CallFactory<T> {

    private OkHttpClient client;

    public CallFactory(OkHttpClient client) {

        this.client = client;
    }

    public Call<T> make() {
        return null;
    }
}
