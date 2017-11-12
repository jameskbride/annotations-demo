package com.jameskbride.annotations.model;

import com.jameskbride.adapter.Call;
import com.jameskbride.adapter.CallFactory;
import com.squareup.javapoet.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

public class MethodModel {
    private Element element;

    public MethodModel(Element element) {
        this.element = element;
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

        return CodeBlock.builder()
                .addStatement("$T callFactory = new $T(client)", CallFactory.class, callType)
                .addStatement("return callFactory.make()")
                .build();
    }
}
