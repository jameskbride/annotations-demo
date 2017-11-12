package com.jameskbride.adapter;

public class Response<T> {
    private T deserializedResponse;

    public Response(T deserializedResponse) {

        this.deserializedResponse = deserializedResponse;
    }

    public T body() {
        return deserializedResponse;
    }
}
