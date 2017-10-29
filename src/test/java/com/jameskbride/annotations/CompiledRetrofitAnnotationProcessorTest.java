package com.jameskbride.annotations;

import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.SourceVersion;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompiledRetrofitAnnotationProcessorTest {

    private DiagnosticCollector<JavaFileObject> diagnosticCollector;
    private CompiledRetrofitAnnotationProcessor processor;
    private JavaCompiler compiler;
    private StandardJavaFileManager fileManager;
    private File temp;

    @Before
    public void setUp() throws IOException {
        compiler = ToolProvider.getSystemJavaCompiler();
        diagnosticCollector = new DiagnosticCollector<>();
        fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        processor = new CompiledRetrofitAnnotationProcessor();
        temp = Files.createTempDir();
    }

    @After
    public void tearDown() throws IOException {
        fileManager.close();
        temp.delete();
    }

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

    @Test
    public void itGeneratesAClassThatImplementsTheRetrofitBase() throws URISyntaxException, IOException {
        File libraryFile = new File(this.getClass().getClassLoader().getResource("GoodBase.java").toURI());

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits1 =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(libraryFile));
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        List<String> options = Arrays.asList( "-d", temp.getAbsolutePath() );
        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnosticCollector, options, null, compilationUnits1);
        compilerTask.setProcessors(Arrays.asList(new CompiledRetrofitAnnotationProcessor()));
        boolean success = compilerTask.call();

        fileManager.close();

        assertTrue(success);


    }
}