package com.jameskbride.adapter;

import okhttp3.Call;

import java.io.IOException;

public interface Callback<T> {

    void onFailure(Call call, IOException e);

    void onResponse(Call call, Response<T> response) throws IOException;
}
