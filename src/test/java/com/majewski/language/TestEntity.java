package com.majewski.language;

import java.util.UUID;
import lombok.Data;

@MultiLanguage(fields = {"name", "message"})
@Data
class TestEntity implements Cloneable {

    private final String id;

    private final String name;

    private final String message;

    private final String notTranslated;

    private final double valueOtherThatString;

    TestEntity(String name, String message, String notTranslated) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.message = message;
        this.notTranslated = notTranslated;
        this.valueOtherThatString = Math.random();
    }

    private TestEntity(String id, String name, String message, String notTranslated, double valueOtherThatString) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.notTranslated = notTranslated;
        this.valueOtherThatString = valueOtherThatString;
    }

    String getId() {
        return id;
    }

    @Override
    protected TestEntity clone() {
        return new TestEntity(this.id, this.name, this.message, this.notTranslated, this.valueOtherThatString);
    }
}
