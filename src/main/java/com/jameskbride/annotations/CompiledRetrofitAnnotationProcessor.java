package com.jameskbride.annotations;

import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import com.jameskbride.annotations.model.ProxyModel;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoService(AbstractProcessor.class)
public class CompiledRetrofitAnnotationProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, ProxyModel> proxyMap = handleRetrofitBase(roundEnv);
        handleGET(roundEnv, proxyMap);
        writeJavaClasses(proxyMap);

        return true;
    }

    private void writeJavaClasses(Map<String, ProxyModel> proxyMap) {
        proxyMap.entrySet().stream().forEach(proxyModelEntry -> {
            ProxyModel proxyModel = proxyModelEntry.getValue();
            TypeSpec proxyType = proxyModel.buildTypeSpec();
            JavaFile javaFile = JavaFile.builder(proxyModel.getPackage(), proxyType).build();

            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleGET(RoundEnvironment roundEnv, Map<String, ProxyModel> proxyMap) {
        Set<? extends Element> getMethods = roundEnv.getElementsAnnotatedWith(GET.class);
        getMethods.stream().forEach(getMethod -> {
            Element enclosingElement = getMethod.getEnclosingElement();
            ProxyModel proxyModel = proxyMap.get(enclosingElement.getSimpleName().toString());
            if (proxyModel != null) {
                proxyModel.addMethod(getMethod);
            }
        });
    }

    private Map<String, ProxyModel> handleRetrofitBase(RoundEnvironment roundEnv) {
        Set<? extends Element> retrofitBaseTypes = roundEnv.getElementsAnnotatedWith(RetrofitBase.class);
        Map<String, ProxyModel> proxyMap = new HashMap<>();
        retrofitBaseTypes.stream().forEach(element -> {
            if (!element.getKind().isInterface()) {
                messager.printMessage(Diagnostic.Kind.ERROR, "RetrofitBase must be applied to an interface");
            }
            ProxyModel proxyModel = new ProxyModel(element);
            proxyMap.put(element.getSimpleName().toString(), proxyModel);
        });
        return proxyMap;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Sets.newHashSet(
                RetrofitBase.class.getCanonicalName(),
                GET.class.getCanonicalName()
        );
    }
}
