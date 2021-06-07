package com.nikitarizh.testtask.initializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class MySQLInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final int MYSQL_PORT = 3306;
    private static final String MYSQL_DB = "task";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "root";

    private static final GenericContainer MYSQL = new GenericContainer("mysql/mysql-server:8.0.23")
            .withEnv("MYSQL_ROOT_PASSWORD", MYSQL_PASSWORD)
            .withEnv("MYSQL_DATABASE", MYSQL_DB)
            .withEnv("MYSQL_USER", MYSQL_USER)
            .withEnv("MYSQL_PASSWORD", MYSQL_PASSWORD)
            .waitingFor(Wait.forListeningPort())
            .withExposedPorts(MYSQL_PORT);

    static {
        MYSQL.start();
    }

    private static String getHost() {
        return MYSQL.getHost();
    }

    private static int getPort() {
        return MYSQL.getMappedPort(MYSQL_PORT);
    }

    private static String getUrl() {
        System.out.println("jdbc:mysql:" + UriComponentsBuilder.newInstance()
                .host(getHost())
                .port(getPort())
                .path(MYSQL_DB)
                .toUriString());
        return "jdbc:mysql:" + UriComponentsBuilder.newInstance()
                .host(getHost())
                .port(getPort())
                .path(MYSQL_DB)
                .toUriString();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applyProperties(applicationContext);
    }

    public void applyProperties(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url:" + getUrl(),
                "spring.datasource.username:" + MYSQL_USER,
                "spring.datasource.password:" + MYSQL_PASSWORD
        ).applyTo(applicationContext);
    }
}
