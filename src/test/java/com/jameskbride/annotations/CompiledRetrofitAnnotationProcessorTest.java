package com.jameskbride.annotations;

import org.junit.Test;

import javax.lang.model.SourceVersion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompiledRetrofitAnnotationProcessorTest {

    @Test
    public void itHasASupportedVersion() {
        CompiledRetrofitAnnotationProcessor processor = new CompiledRetrofitAnnotationProcessor();

        assertEquals(SourceVersion.latest(), processor.getSupportedSourceVersion());
    }

    @Test
    public void itHasAListOfSupportedAnnotations() {
        CompiledRetrofitAnnotationProcessor processor = new CompiledRetrofitAnnotationProcessor();

        assertTrue(processor.getSupportedAnnotationTypes().contains(RetrofitBase.class.getCanonicalName()));
    }

}