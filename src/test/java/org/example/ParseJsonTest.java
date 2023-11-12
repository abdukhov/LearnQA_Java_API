package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class ParseJsonTest {

    @Test
    public void shouldParseJsonAndReturnThe2ndMessage() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        var secondMessage = response.get("messages[1]");
        System.out.println(secondMessage);
    }
}
