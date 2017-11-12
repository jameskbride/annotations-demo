package com.jameskbride.example.network;

import com.jameskbride.adapter.Call;
import com.jameskbride.annotations.Base;
import com.jameskbride.annotations.GET;
import com.jameskbride.example.model.SomeObject;

@Base("http://localhost")
public interface ExampleApi {

    @GET("/")
    Call<SomeObject> getSomeObject();
}
