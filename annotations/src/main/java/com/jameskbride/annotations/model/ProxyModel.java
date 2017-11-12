package com.jameskbride.annotations.model;

import com.jameskbride.annotations.Base;
import com.squareup.javapoet.*;
import okhttp3.OkHttpClient;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProxyModel {
    private Element baseElement;
    private Set<MethodModel> methods;
    private List<Validation> validations;
    private Elements elementUtils;

    public ProxyModel(Element element, Elements elementUtils) {
        this.elementUtils = elementUtils;
        validations = new ArrayList<>();
        methods = new HashSet<>();
        baseElement = element;
    }

    private String getProxyClassName() {
        return baseElement.getSimpleName().toString() + "Proxy";
    }

    private TypeName getTypeName() {
        return TypeName.get(baseElement.asType());
    }

    private Modifier[] getModifiers() {
        return baseElement.getModifiers()
                .stream()
                .filter(modifier -> !modifier.equals(Modifier.ABSTRACT)).toArray(size -> new Modifier[size]);
    }

    public TypeSpec buildTypeSpec() {
        return TypeSpec.classBuilder(getProxyClassName())
                .addSuperinterface(getTypeName())
                .addModifiers(getModifiers())
                .addField(generateOkHttpClientProperty())
                .addMethods(methods.stream().map(method -> method.build()).collect(Collectors.toSet()))
                .build();
    }

    private FieldSpec generateOkHttpClientProperty() {
        return FieldSpec.builder(OkHttpClient.class, "client", Modifier.PRIVATE, Modifier.FINAL)
            .initializer("new $T()", ClassName.get(OkHttpClient.class)).build();
    }

    public PackageElement getPackage() {
        return elementUtils.getPackageOf(baseElement);
    }

    public String getBaseUrl() {
        return baseElement.getAnnotation(Base.class).value();
    }

    public void addMethod(Element element, String baseUrl) {
        MethodModel methodModel = new MethodModel(element, baseUrl);
        methods.add(methodModel);
    }

    public List<Validation> validate() {
        if (!baseElement.getKind().isInterface()) {
            validations.add(new Validation(Diagnostic.Kind.ERROR, "Base must be applied to an interface"));
        }

        List<Validation> methodValidations = getMethodValidations();
        validations.addAll(methodValidations);
        return validations;
    }

    private List<Validation> getMethodValidations() {
        return methods.stream()
                    .map(entry -> entry.validate())
                    .reduce(new ArrayList<>(), (accum, validationList) -> {accum.addAll(validationList); return accum;});
    }
}
