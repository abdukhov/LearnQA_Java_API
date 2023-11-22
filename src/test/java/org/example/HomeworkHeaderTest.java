package org.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.containsString;

public class HomeworkHeaderTest {

    @Test
    public void shouldReturnCorrectHomeworkHeaders() {
        RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .then()
                .statusCode(SC_OK)
                .header("x-secret-homework-header", containsString("Some secret value"))
                .header("content-type", containsString("application/json"))
                .header("content-length", containsString("15"))
                .header("server", containsString("Apache"))
                .header("cache-control", containsString("max-age=0"));

    }
}
