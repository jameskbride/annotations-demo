package com.jameskbride.annotations.model;

import com.jameskbride.adapter.Call;
import com.jameskbride.adapter.CallFactory;
import com.jameskbride.annotations.GET;
import com.squareup.javapoet.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

public class MethodModel {
    private Element element;
    private String baseUrl;

    public MethodModel(Element element, String baseUrl) {
        this.element = element;
        this.baseUrl = baseUrl;
    }

    public MethodSpec build() {
        TypeName returnTypeParam = buildReturnTypeParam();
        TypeName callType = buildCallType(returnTypeParam);
        return MethodSpec.methodBuilder(element.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .returns(callType)
                .addCode(generateRequestMethod(returnTypeParam))
                .build();
    }

    private ParameterizedTypeName buildCallType(TypeName returnTypeParam) {
        return ParameterizedTypeName.get(ClassName.get(Call.class), returnTypeParam);
    }

    private TypeName buildReturnTypeParam() {
        ExecutableType method = (ExecutableType)element.asType();
        TypeMirror returnTypeParam = ((DeclaredType)method.getReturnType()).getTypeArguments().get(0);
        return ClassName.get(returnTypeParam);
    }

    private CodeBlock generateRequestMethod(TypeName returnTypeParam) {
        TypeName callType = ParameterizedTypeName.get(ClassName.get(CallFactory.class), returnTypeParam);
        String path = element.getAnnotation(GET.class).value();

        return CodeBlock.builder()
                .addStatement("$T callFactory = new $T(client, $T.class)",
                        CallFactory.class,
                        callType,
                        ClassName.bestGuess(returnTypeParam.toString()))
                .addStatement("return callFactory.make(\"$L\", \"$L\")", baseUrl, path)
                .build();
    }
}
