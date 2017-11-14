package com.jameskbride.example.compiletime.network;

import com.jameskbride.adapter.Call;
import com.jameskbride.example.model.SomeObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExampleApiTest {

    @Test
    public void itCanUseTheGeneratedClass() {
        ExampleApi exampleApi = new ExampleApiProxy();
        Call<SomeObject> call = exampleApi.getSomeObject();
        assertEquals("http://localhost:1080/", call.request().url().toString());
    }
}
