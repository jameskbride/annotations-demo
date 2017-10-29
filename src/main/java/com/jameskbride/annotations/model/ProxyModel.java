package com.jameskbride.annotations.model;

import com.squareup.javapoet.*;
import okhttp3.OkHttpClient;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProxyModel {
    private Element baseElement;
    private Set<MethodModel> methods;

    public ProxyModel(Element element) {
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

    public String getPackage() {
        return "";
    }

    public void addMethod(Element element) {
        MethodModel methodModel = new MethodModel(element);
        methods.add(methodModel);
    }
}
