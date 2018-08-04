package com.majewski.language;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
class TestEntityTranslationalRepository implements InternationalizationRepository {

    @Override
    public String getByKey(String key) {
        return key.toUpperCase();
    }

    @Override
    public Map<String, String> getKeyToTranslationMap(List<String> translationKeys) {
        return translationKeys.stream()
                .collect(Collectors.toMap(key -> key, String::toUpperCase));
    }
}
