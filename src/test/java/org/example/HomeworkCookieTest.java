package org.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.containsString;

public class HomeworkCookieTest {

    @Test
    public void shouldReturnCorrectHomeworkCookie() {
        RestAssured
            .get("https://playground.learnqa.ru/api/homework_cookie")
            .then()
            .statusCode(SC_OK)
            .cookie("HomeWork", containsString("hw_value"));
    }
}
