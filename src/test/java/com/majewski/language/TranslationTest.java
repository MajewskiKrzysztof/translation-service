package com.majewski.language;

import static org.assertj.core.api.Assertions.assertThat;

import com.majewski.DemoApplication;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class TranslationTest {

    @Autowired
    private TestService testService;

    @Autowired
    private TestEntityRepository testEntityRepository;

    @Autowired
    private TestEntityTranslationalRepository translationRepository;

    @Test
    public void whenCallMethodInMultiLanguageService_thenReturnTranslatedValues() {
        // given
        List<TestEntity> testEntities = testEntityRepository.getAll();

        // when
        List<TestEntity> translatedEntities = testService.doSomething();

        // then
        Map<String, TestEntity> translatedEntitiesIdMap = translatedEntities.stream()
                .collect(Collectors.toMap(TestEntity::getId, testEntity -> testEntity));

        testEntities.forEach(testEntity -> {
            TestEntity translatedEntity = translatedEntitiesIdMap.get(testEntity.getId());

            // translated values
            assertThat(testEntity.getName()).isNotEqualTo(translatedEntity.getName());
            assertThat(translationRepository.getByKey(testEntity.getName())).isEqualTo(translatedEntity.getName());
            assertThat(testEntity.getMessage()).isNotEqualTo(translatedEntity.getMessage());
            assertThat(translationRepository.getByKey(testEntity.getMessage())).isEqualTo(translatedEntity.getMessage());

            // not translatedValues
            assertThat(testEntity.getNotTranslated()).isEqualTo(translatedEntity.getNotTranslated());
            assertThat(testEntity.getValueOtherThatString()).isEqualTo(translatedEntity.getValueOtherThatString());
        });
    }

}
