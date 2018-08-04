package com.majewski.language;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@MultiLanguageService
class TestServiceImpl implements TestService {

    @Autowired
    private TestEntityRepository testEntityRepository;

    public List<TestEntity> doSomething() {
        // some more logic
        return testEntityRepository.getAll();
    }

}
