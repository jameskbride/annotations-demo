package com.jameskbride.annotations.model;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import okhttp3.Call;
import okhttp3.Request;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class MethodModel {
    private Element element;

    public MethodModel(Element element) {
        this.element = element;
    }

    public MethodSpec build() {
        return MethodSpec.methodBuilder(element.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(Call.class))
                .addCode(generateRequestMethod())
                .build();
    }

    private CodeBlock generateRequestMethod() {
        return CodeBlock.builder()
                .addStatement("$T request = new Request.Builder()", Request.Builder.class)
                .addStatement("return client.newCall(request.build())")
                .build();
    }
}
