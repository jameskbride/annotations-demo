package com.jameskbride.adapter;

import okhttp3.Request;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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
        Call call = new Call(okhttpCall, request);

        assertEquals(request, call.request());
    }

    @Test
    public void itCanEnqueueTheCallback() {
        Call call = new Call(okhttpCall, request);

        Callback responseCallback = new Callback();
        call.enqueue(responseCallback);

        verify(okhttpCall).enqueue(responseCallback);
    }

}