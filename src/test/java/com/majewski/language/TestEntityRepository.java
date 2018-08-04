package com.majewski.language;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
class TestEntityRepository {

    private final List<TestEntity> allEntities = Arrays.asList(
            new TestEntity("name.key", "message.key", "not.translated"),
            new TestEntity("another.name.key", "another.message.key", "another.not.translated")
    );

    List<TestEntity> getAll() {
        return allEntities.stream().map(TestEntity::clone).collect(Collectors.toList());
    }
}
