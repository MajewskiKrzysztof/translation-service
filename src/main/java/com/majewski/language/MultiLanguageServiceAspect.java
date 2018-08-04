package com.majewski.language;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
class MultiLanguageServiceAspect {

    private final MultiLanguageProcessor multilanguageProcessor;

    @Autowired
    public MultiLanguageServiceAspect(MultiLanguageProcessor multilanguageProcessor) {
        this.multilanguageProcessor = multilanguageProcessor;
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    @AfterReturning(value = "publicMethod() && @within(multiLanguage)", returning = "returnValue")
    public void multiLanguageServicePublicMethod(MultiLanguageService multiLanguage, Object returnValue) {
        multilanguageProcessor.translateReturnValue(returnValue);
    }

}
