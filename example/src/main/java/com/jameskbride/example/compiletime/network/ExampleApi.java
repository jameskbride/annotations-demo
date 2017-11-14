package com.jameskbride.example.compiletime.network;

import com.jameskbride.adapter.Call;
import com.jameskbride.annotations.Base;
import com.jameskbride.annotations.GET;
import com.jameskbride.example.model.SomeObject;

@Base("http://localhost:1080")
public interface ExampleApi {

    @GET("/")
    Call<SomeObject> getSomeObject();
}
