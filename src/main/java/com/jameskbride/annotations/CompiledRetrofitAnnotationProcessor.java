package com.jameskbride.annotations;

import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import com.jameskbride.annotations.model.ProxyModel;
import com.jameskbride.annotations.model.Validation;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

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
        List<Validation> validations = getValidations(proxyMap);
        printCompileMessages(validations);
        if (errorsPresent(validations)) {
            return true;
        }

        handleGET(roundEnv, proxyMap);
        writeJavaClasses(proxyMap);

        return true;
    }

    private void printCompileMessages(List<Validation> validations) {
        validations.stream().forEach(validation -> messager.printMessage(validation.getKind(), validation.getMessage()));
    }

    private List<Validation> getValidations(Map<String, ProxyModel> proxyMap) {
        List<Validation> validations = proxyMap.entrySet().stream()
                .map(entry -> entry.getValue().validate())
                .reduce(new ArrayList<>(), (accum, validationList) -> {accum.addAll(validationList); return accum;});

        return validations;
    }

    private boolean errorsPresent(List<Validation> validations) {
        return validations.stream()
                .filter(validation -> validation.getKind().equals(Diagnostic.Kind.ERROR))
                .findAny()
                .isPresent();
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
        Set<? extends Element> retrofitBaseTypes = roundEnv.getElementsAnnotatedWith(Base.class);
        Map<String, ProxyModel> proxyMap = new HashMap<>();
        retrofitBaseTypes.stream().forEach(element -> {
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
                Base.class.getCanonicalName(),
                GET.class.getCanonicalName()
        );
    }
}
