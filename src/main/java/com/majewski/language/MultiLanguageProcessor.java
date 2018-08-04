package com.majewski.language;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class MultiLanguageProcessor {

    private final Logger log = LoggerFactory.getLogger(MultiLanguageProcessor.class);

    private final InternationalizationRepository internationalizationRepository;

    @Autowired
    public MultiLanguageProcessor(InternationalizationRepository internationalizationRepository) {
        this.internationalizationRepository = internationalizationRepository;
    }

    void translateReturnValue(Object returnValue) {
        if (returnValue instanceof Collection) {
            translateCollection((Collection) returnValue);
        } else {
            translateSingleObject(returnValue);
        }
    }

    private void translateCollection(Collection<?> collection) {
        collection.forEach(this::translateSingleObject);
    }

    private void translateSingleObject(Object referenceObject) {
        List<Field> fieldsToTranslate = getFieldsToTranslate(referenceObject);

        List<String> translationKeys = extractTranslationKeysFromFields(fieldsToTranslate, referenceObject);
        Map<String, String> keyToTranslationMap = internationalizationRepository.getKeyToTranslationMap(translationKeys);

        setTranslatedValuesToFields(fieldsToTranslate, referenceObject, keyToTranslationMap);
    }

    private List<Field> getFieldsToTranslate(Object referenceObject) {
        return Stream.of(referenceObject.getClass().getDeclaredAnnotations())
                .filter(annotation -> annotation instanceof MultiLanguage)
                .map(annotation -> ((MultiLanguage) annotation).fields())
                .flatMap(Stream::of)
                .map(fieldName -> getObjectField(referenceObject, fieldName))
                .filter(Objects::nonNull)
                .filter(field -> field.getType().equals(String.class))
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toList());
    }

    private List<String> extractTranslationKeysFromFields(List<Field> fields, Object referenceObject) {
        return fields.stream()
                .map(field -> getFieldStringValue(field, referenceObject))
                .collect(Collectors.toList());
    }

    private void setTranslatedValuesToFields(List<Field> fields, Object referenceObject, Map<String, String> keyToTranslationMap) {
        fields.forEach(field -> {
            String translationKey = getFieldStringValue(field, referenceObject);
            String translation = keyToTranslationMap.get(translationKey);
            setValueToField(field, referenceObject, translation);
        });
    }

    private Field getObjectField(Object object, String fieldName) {
        try {
            return object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private String getFieldStringValue(Field field, Object value) {
        try {
            return (String) field.get(value);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private void setValueToField(Field field, Object referenceObject, Object value) {
        try {
            field.set(referenceObject, value);
        } catch (IllegalAccessException e) {
            log.warn(e.getMessage(), e);
        }
    }
}
