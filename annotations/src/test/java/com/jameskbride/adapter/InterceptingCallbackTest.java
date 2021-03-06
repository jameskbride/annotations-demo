package com.jameskbride.adapter;

import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InterceptingCallbackTest {

    private RecordingCallback testObjectCallback;

    @Before
    public void setUp() {
        testObjectCallback = new RecordingCallback();
    }

    @Test
    public void onResponseDeserializesTheBodyAsType() throws IOException {
        InterceptingCallback<TestObject> interceptingCallback = new InterceptingCallback(testObjectCallback, TestObject.class);
        okhttp3.Response response = mock(okhttp3.Response.class);
        ResponseBody responseBody = mock(ResponseBody.class);
        Gson gson = new Gson();
        when(responseBody.string()).thenReturn(gson.toJson(new TestObject("someValue", 1)));
        when(response.body()).thenReturn(responseBody);

        interceptingCallback.onResponse(null, response);

        assertEquals("someValue", testObjectCallback.getActualResponse().getStringValue());
        assertEquals(Integer.valueOf(1), testObjectCallback.getActualResponse().getIntegerValue());
    }

    @Test
    public void onFailureInvokesTheCallbackOnFailure() {
        InterceptingCallback<TestObject> interceptingCallback = new InterceptingCallback(testObjectCallback, TestObject.class);
        IOException ioException = new IOException();
        Call call = mock(Call.class);
        interceptingCallback.onFailure(call, ioException);

        assertTrue(testObjectCallback.onFailureCalled());
        assertEquals(call, testObjectCallback.getFailureCall());
        assertEquals(ioException, testObjectCallback.getFailureException());
    }

    static class RecordingCallback implements Callback<TestObject> {
        private TestObject actualResponse;
        private boolean onFailureCalled;
        private IOException failureException;
        private Call failureCall;

        @Override
        public void onFailure(Call call, IOException e) {
            onFailureCalled = true;
            this.failureCall = call;
            this.failureException = e;
        }

        @Override
        public void onResponse(Call call, Response<TestObject> response) throws IOException {
            actualResponse = response.body();
        }

        public TestObject getActualResponse() {
            return actualResponse;
        }

        public boolean onFailureCalled() {
            return onFailureCalled;
        }

        public IOException getFailureException() {
            return failureException;
        }

        public Call getFailureCall() {
            return failureCall;
        }
    }

}