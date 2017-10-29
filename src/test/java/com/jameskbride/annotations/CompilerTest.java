package com.jameskbride.annotations;

import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

public abstract class CompilerTest {
    protected static String OUTPUT_PATH_NAME;
    protected DiagnosticCollector<JavaFileObject> diagnosticCollector;
    protected CompiledRetrofitAnnotationProcessor processor;
    protected JavaCompiler compiler;
    protected StandardJavaFileManager fileManager;
    protected String inputPath;
    protected File outputPath;

    @Before
    public void setUp() throws IOException, URISyntaxException {
        compiler = ToolProvider.getSystemJavaCompiler();
        diagnosticCollector = new DiagnosticCollector<>();
        fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        outputPath = new File(OUTPUT_PATH_NAME);
        outputPath.delete();
        Files.createParentDirs(new File(OUTPUT_PATH_NAME + "/this_segment_is_ignored"));
    }

    @After
    public void tearDown() throws IOException {
        printDiagnostics(diagnosticCollector);
        fileManager.close();
    }

    protected boolean compile(List<File> files, CompiledRetrofitAnnotationProcessor processor) {
        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(files);
        List<String> options = Arrays.asList("-s", OUTPUT_PATH_NAME);
        JavaCompiler.CompilationTask compilerTask =
                compiler.getTask(null, fileManager, diagnosticCollector, options, null, compilationUnits);
        compilerTask.setProcessors(Arrays.asList(processor));
        return compilerTask.call();
    }

    protected void printDiagnostics(DiagnosticCollector<JavaFileObject> diagnosticCollector) {
        diagnosticCollector.getDiagnostics()
                .stream()
                .filter(diagnostic -> diagnostic.getKind().equals(Diagnostic.Kind.ERROR) ||
                        diagnostic.getKind().equals(Diagnostic.Kind.WARNING))
                .forEach(diagnostic ->
                    System.out.println(diagnostic));
    }

    protected ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    //This code unceremoniously lifted from: https://stackoverflow.com/questions/6219829/method-to-dynamically-load-java-class-files
    protected Class loadClasses(String basePath, String fullyQualifiedClassName) throws MalformedURLException, ClassNotFoundException {
        File file = new File(basePath);
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};

        ClassLoader urlClassLoader = new URLClassLoader(urls);

        Class clazz = urlClassLoader.loadClass(fullyQualifiedClassName);

        return clazz;
    }
}
