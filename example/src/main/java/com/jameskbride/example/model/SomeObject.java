package com.jameskbride.example.model;

import com.jameskbride.example.runtime.ClassMarker;
import com.jameskbride.example.runtime.MethodMarker;
import com.jameskbride.example.runtime.PropertyMarker;

import java.io.Serializable;

@ClassMarker(className = "This is SomeObject")
public class SomeObject implements Serializable {

    @PropertyMarker(propertyName = "The stringValue")
    private String stringValue;
    @PropertyMarker(propertyName = "The integerValue")
    private Integer integerValue;

    public SomeObject(String stringValue, Integer integerValue) {
        this.stringValue = stringValue;
        this.integerValue = integerValue;
    }

    @MethodMarker(methodName = "Method 1")
    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    @MethodMarker(methodName = "Method 2")
    public Integer getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
    }

    @Override
    public String toString() {
        return "SomeObject{" +
                "stringValue='" + stringValue + '\'' +
                ", integerValue=" + integerValue +
                '}';
    }
}
