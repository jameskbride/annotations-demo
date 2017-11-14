package com.jameskbride.example.runtime;

import com.jameskbride.example.model.SomeObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SomeObjectProcessor {
    private SomeObject someObject;

    public SomeObjectProcessor(SomeObject someObject) {

        this.someObject = someObject;
    }

    public String getClassMarkerValue() {
        ClassMarker classMarker = someObject.getClass().getAnnotation(ClassMarker.class);
        return classMarker.className();
    }

    public String getPropertyMarker(String propertyName) throws NoSuchFieldException {
        Field stringValueField = someObject.getClass().getDeclaredField(propertyName);
        PropertyMarker annotation = stringValueField.getAnnotation(PropertyMarker.class);
        return annotation.propertyName();
    }

    public String getMethodMarker(String methodName) throws NoSuchMethodException {
        Method stringValueField = someObject.getClass().getMethod(methodName);
        MethodMarker annotation = stringValueField.getAnnotation(MethodMarker.class);
        return annotation.methodName();
    }
}
