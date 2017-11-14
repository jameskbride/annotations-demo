package com.jameskbride.example.runtime;

import com.jameskbride.example.model.SomeObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SomeObjectProcessorTest {

    @Test
    public void itCanReadTheClassMarker() {
        SomeObject someObject = new SomeObject("some string", 1);

        SomeObjectProcessor someObjectProcessor = new SomeObjectProcessor(someObject);

        assertEquals("This is SomeObject", someObjectProcessor.getClassMarkerValue());
    }

    @Test
    public void itCanReadThePropertyMarker() throws NoSuchFieldException {
        SomeObject someObject = new SomeObject("some string", 1);

        SomeObjectProcessor someObjectProcessor = new SomeObjectProcessor(someObject);

        assertEquals("The stringValue", someObjectProcessor.getPropertyMarker("stringValue"));
        assertEquals("The integerValue", someObjectProcessor.getPropertyMarker("integerValue"));
    }

    @Test
    public void itCanReadTheMethodMarker() throws NoSuchFieldException, NoSuchMethodException {
        SomeObject someObject = new SomeObject("some string", 1);

        SomeObjectProcessor someObjectProcessor = new SomeObjectProcessor(someObject);

        assertEquals("Method 1", someObjectProcessor.getMethodMarker("getStringValue"));
        assertEquals("Method 2", someObjectProcessor.getMethodMarker("getIntegerValue"));
    }
}
