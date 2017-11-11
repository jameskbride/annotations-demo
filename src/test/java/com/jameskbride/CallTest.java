package com.jameskbride;

import okhttp3.Request;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class CallTest {

    @Test
    public void itReturnsTheRequestItWasCreatedWith() {
        okhttp3.Call okhttpCall = mock(okhttp3.Call.class);
        Request request = mock(Request.class);

        Call call = new Call(okhttpCall, request);

        assertEquals(request, call.request());
    }
}