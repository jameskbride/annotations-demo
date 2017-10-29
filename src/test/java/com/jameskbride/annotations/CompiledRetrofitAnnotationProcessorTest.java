package com.jameskbride.annotations;

import org.junit.Before;
import org.junit.Test;

import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        boolean result = compile(files, processor);

        assertTrue(result);

        Class goodBase = loadClasses(inputPath, "GoodBase");
        Class goodBaseProxy = loadClasses(OUTPUT_PATH_NAME, "GoodBaseProxy");
        assertTrue(goodBase.isAssignableFrom(goodBaseProxy));
    }

    @Test
    public void itGeneratesAClassThatHasTheSameScopeModifierAsTheInterface() throws URISyntaxException, MalformedURLException, ClassNotFoundException {
        File libraryFile = new File(getClassLoader().getResource("GoodBase.java").toURI());

        List<File> files = Arrays.asList(libraryFile);
        boolean result = compile(files, processor);

        assertTrue(result);

        Class goodBaseProxy = loadClasses(OUTPUT_PATH_NAME, "GoodBaseProxy");
        assertEquals(Modifier.PUBLIC, goodBaseProxy.getModifiers());
    }

    @Test
    public void itGeneratesAnErrorWhenTheRetrofitBaseIsNotAnInterface() throws URISyntaxException {
        File libraryFile = new File(getClassLoader().getResource("NotAnInterface.java").toURI());

        List<File> files = Arrays.asList(libraryFile);
        boolean result = compile(files, processor);

        assertFalse(result);

        Optional<Diagnostic<? extends JavaFileObject>> error = diagnosticCollector.getDiagnostics()
                .stream()
                .filter(diagnostic -> diagnostic.getKind().equals(Diagnostic.Kind.ERROR))
                .findAny();
        assertEquals("interface expected here", error.get().getMessage(Locale.US));
    }

    protected String getInputPath() throws URISyntaxException {
        return new File(getClassLoader().getResource(".").toURI()).getAbsolutePath();
    }
}