package com.hunkd.annotation.manifest.processor;

import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

public abstract class CustomProcessor<ProcessObj, AnnotationClass> {
    protected final ProcessorFactory mProcessorFactory;

    public CustomProcessor(ProcessorFactory processorFactory) {
        mProcessorFactory = processorFactory;
    }

    public abstract ProcessObj process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, Messager messager);

    public abstract Class<AnnotationClass> getAnnotation();
}
