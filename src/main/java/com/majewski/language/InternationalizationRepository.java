package com.majewski.language;

import java.util.List;
import java.util.Map;

public interface InternationalizationRepository {

    String getByKey(String key);

    Map<String, String> getKeyToTranslationMap(List<String> translationKeys);
}
