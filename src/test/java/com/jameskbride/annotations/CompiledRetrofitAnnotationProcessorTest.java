package com.jameskbride.annotations;

import org.junit.Before;
import org.junit.Test;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompiledRetrofitAnnotationProcessorTest extends CompilerTest {

    @Before
    public void setUp() throws IOException, URISyntaxException {
        OUTPUT_PATH_NAME = "build/generated";
        inputPath = getInputPath();
        processor = new CompiledRetrofitAnnotationProcessor();
        super.setUp();
    }

    @Test
    public void itHasASupportedVersion() {
        assertEquals(SourceVersion.latest(), processor.getSupportedSourceVersion());
    }

    @Test
    public void itHasAListOfSupportedAnnotations() {
        assertTrue(processor.getSupportedAnnotationTypes().contains(RetrofitBase.class.getCanonicalName()));
    }

    @Test
    public void itGeneratesAClassThatImplementsTheRetrofitBase() throws URISyntaxException, IOException, ClassNotFoundException {
        File libraryFile = new File(getClassLoader().getResource("GoodBase.java").toURI());

        List<File> files = Arrays.asList(libraryFile);
        boolean success = compile(files, processor);

        assertTrue(success);

        Class goodBase = loadClasses(inputPath, "GoodBase");
        Class goodBaseProxy = loadClasses(OUTPUT_PATH_NAME, "GoodBaseProxy");
        assertTrue(goodBase.isAssignableFrom(goodBaseProxy));
    }

    protected String getInputPath() throws URISyntaxException {
        return new File(getClassLoader().getResource(".").toURI()).getAbsolutePath();
    }
}