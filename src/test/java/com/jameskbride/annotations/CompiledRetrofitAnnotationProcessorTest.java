package com.jameskbride.annotations;

import com.google.common.collect.Sets;
import com.jameskbride.adapter.Call;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;

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
        assertTrue(processor.getSupportedAnnotationTypes().containsAll(
                Sets.newHashSet(
                        Base.class.getCanonicalName(),
                        GET.class.getCanonicalName()
                ))
        );
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
    public void itGeneratesAnErrorWhenTheBaseIsNotAnInterface() throws URISyntaxException {
        File libraryFile = new File(getClassLoader().getResource("NotAnInterface.java").toURI());

        List<File> files = Arrays.asList(libraryFile);
        boolean result = compile(files, processor);

        assertFalse(result);

        Optional<Diagnostic<? extends JavaFileObject>> error = diagnosticCollector.getDiagnostics()
                .stream()
                .filter(diagnostic -> diagnostic.getKind().equals(Diagnostic.Kind.ERROR))
                .findAny();
        assertEquals("Base must be applied to an interface", error.get().getMessage(Locale.US));
    }

    @Test
    public void itGeneratesAMethodForAGETAnnotation() throws MalformedURLException, ClassNotFoundException, URISyntaxException, NoSuchMethodException {
        File simpleMethodBase = new File(getClassLoader().getResource("SimpleMethodBase.java").toURI());
        File testClassFile = new File(getClassLoader().getResource("TestClass.java").toURI());

        List<File> files = Arrays.asList(simpleMethodBase, testClassFile);
        boolean result = compile(files, processor);

        assertTrue(result);

        Class goodBaseProxy = loadClasses(OUTPUT_PATH_NAME, "SimpleMethodBaseProxy");
        Method returnSomething = goodBaseProxy.getMethod("returnSomething");
        assertNotNull(returnSomething);
        assertEquals(Call.class, returnSomething.getReturnType());
    }

    protected String getInputPath() throws URISyntaxException {
        return new File(getClassLoader().getResource(".").toURI()).getAbsolutePath();
    }
}