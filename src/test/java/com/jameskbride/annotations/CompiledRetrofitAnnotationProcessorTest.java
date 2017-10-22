package com.jameskbride.annotations;

import org.junit.Test;

import javax.lang.model.SourceVersion;

import static org.junit.Assert.assertEquals;

public class CompiledRetrofitAnnotationProcessorTest {

    @Test
    public void itHasASupportedVersion() {
        CompiledRetrofitAnnotationProcessor processor = new CompiledRetrofitAnnotationProcessor();

        assertEquals(SourceVersion.latest(), processor.getSupportedSourceVersion());
    }

}