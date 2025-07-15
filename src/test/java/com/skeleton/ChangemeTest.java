package com.skeleton;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;
import org.junit.jupiter.api.TestInstance;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChangemeTest {

    // @Inject
    // EmbeddedApplication<?> application;

    @Test
    void testItWorks() {
        Assertions.assertTrue(2 == 2);
    }

}
