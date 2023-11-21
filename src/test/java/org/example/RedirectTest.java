package org.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

public class RedirectTest {

    @Test
    public void shouldPrintRedirectAddress() {
        var locationHeader = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/longRedirect")
                .then()
                .extract()
                .header("Location");

        System.out.println(locationHeader);
    }
}
