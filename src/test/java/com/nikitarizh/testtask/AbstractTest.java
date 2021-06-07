package com.nikitarizh.testtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitarizh.testtask.initializer.MySQLInitializer;
import com.nikitarizh.testtask.utils.DataManipulator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = {
        MySQLInitializer.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
@AutoConfigureMockMvc
public class AbstractTest {

    protected final DataManipulator dataManipulator;

    protected String BASE_URL = "http://localhost:8080";

    @BeforeEach
    public void prepareDatabase() {
        dataManipulator.clearAllDatabases();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
