package com.jameskbride.example.compiletime;

import com.jameskbride.adapter.Call;
import com.jameskbride.adapter.Callback;
import com.jameskbride.adapter.Response;
import com.jameskbride.example.model.SomeObject;
import com.jameskbride.example.compiletime.network.ExampleApi;
import com.jameskbride.example.compiletime.network.ExampleApiProxy;
import com.jameskbride.example.runtime.SomeObjectProcessor;

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
                SomeObject body = response.body();
                SomeObjectProcessor someObjectProcessor = new SomeObjectProcessor(body);
                System.out.println("Got the response: " + body.toString());
                System.out.println("Class marker: " + someObjectProcessor.getClassMarkerValue());
                try {
                    System.out.println("Class property 1: " + someObjectProcessor.getPropertyMarker("stringValue"));
                    System.out.println("Class property 2: " + someObjectProcessor.getPropertyMarker("integerValue"));
                    System.out.println("Class method 1: " + someObjectProcessor.getMethodMarker("getStringValue"));
                    System.out.println("Class method 2: " + someObjectProcessor.getMethodMarker("getIntegerValue"));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
