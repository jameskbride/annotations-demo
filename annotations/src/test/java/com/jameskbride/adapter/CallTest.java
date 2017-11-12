package com.jameskbride.adapter;

import okhttp3.Request;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CallTest {

    @Mock
    private okhttp3.Call okhttpCall;

    @Mock
    private Request request;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void itReturnsTheRequestItWasCreatedWith() {
        Call call = new Call<TestObject>(okhttpCall, request, TestObject.class);

        assertEquals(request, call.request());
    }

    @Test
    public void itCanEnqueueTheCallback() {
        Call call = new Call<TestObject>(okhttpCall, request, TestObject.class);

        Callback<TestObject> responseCallback = buildEmptyCallback();
        call.enqueue(responseCallback);

        ArgumentCaptor<InterceptingCallback> callbackArgumentCaptor = ArgumentCaptor.forClass(InterceptingCallback.class);

        verify(okhttpCall).enqueue(callbackArgumentCaptor.capture());
        assertEquals(responseCallback, callbackArgumentCaptor.getValue().responseCallback);
    }

    private Callback<TestObject> buildEmptyCallback() {
        return new Callback<TestObject>() {

                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, Response<TestObject> response) throws IOException {

                }
            };
    }

}