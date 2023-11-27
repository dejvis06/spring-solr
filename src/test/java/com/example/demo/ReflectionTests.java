package com.example.demo;

import com.example.demo.repositories.TechProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReflectionTests {

    @Autowired
    TechProductRepository techProductRepository;

    @Test
    void test() {
        techProductRepository.findAll(null);
    }
}
