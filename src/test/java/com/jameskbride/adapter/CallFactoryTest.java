package com.jameskbride.adapter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CallFactoryTest {

    @Test
    public void itMakesACall() {
        OkHttpClient client = new OkHttpClient();
        CallFactory<TestObject> callFactory = new CallFactory<>(client, TestObject.class);
        Call<TestObject> testObjectCall = callFactory.make("http://localhost/", "somePath");

        Request request = testObjectCall.request();
        assertEquals("http://localhost/somePath", request.url().toString());
    }
}