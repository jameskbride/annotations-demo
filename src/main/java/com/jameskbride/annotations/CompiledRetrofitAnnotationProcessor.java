package com.jameskbride.annotations;

import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

@AutoService(AbstractProcessor.class)
public class CompiledRetrofitAnnotationProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> retrofitBaseTypes = roundEnv.getElementsAnnotatedWith(RetrofitBase.class);
        for (Element element : retrofitBaseTypes) {

            TypeSpec proxyType = TypeSpec.classBuilder(element.getSimpleName().toString() + "Proxy")
                    .addSuperinterface(TypeName.get(element.asType()))
                    .addModifiers(getModifiers(element))
                    .build();

            JavaFile javaFile = JavaFile.builder("", proxyType).build();

            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private Modifier[] getModifiers(Element element) {
        return element.getModifiers()
                .stream()
                .filter(modifier -> !modifier.equals(Modifier.ABSTRACT)).toArray(size -> new Modifier[size]);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Sets.newHashSet(RetrofitBase.class.getCanonicalName());
    }
}
