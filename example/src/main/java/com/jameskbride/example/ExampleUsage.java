package com.jameskbride.example;

import com.jameskbride.adapter.Call;
import com.jameskbride.adapter.Callback;
import com.jameskbride.adapter.Response;
import com.jameskbride.example.model.SomeObject;
import com.jameskbride.example.network.ExampleApi;
import com.jameskbride.example.network.ExampleApiProxy;

import java.io.IOException;

public class ExampleUsage {

    public static void main(String[] args) {
        ExampleApi exampleApi = new ExampleApiProxy();
        Call<SomeObject> call = exampleApi.getSomeObject();
        call.enqueue(new Callback<SomeObject>() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                System.out.println("Something went terribly wrong");
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response<SomeObject> response) throws IOException {
                System.out.println("Got the response: " + response.body().toString());
            }
        });
    }
}
