package com.jameskbride.annotations.model;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import okhttp3.Call;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Set;

public class ProxyModel {
    private Element baseElement;
    private Set<MethodSpec> methods;

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
                .addMethods(methods)
                .build();
    }

    public String getPackage() {
        return "";
    }

    public void addMethod(Element element) {
        MethodSpec methodSpec = MethodSpec.methodBuilder(element.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(Call.class))
                .addCode(CodeBlock.builder().addStatement("return null").build())
                .build();
        methods.add(methodSpec);
    }
}
