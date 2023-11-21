package org.example;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_CONTINUE;
import static org.apache.http.HttpStatus.SC_OK;

public class LongRedirectTest {

    @Test
    public void shouldPrintLongRedirectAddress() {
        var statusCode = 0;
        var locationHeader = "";
        var startUrl = "https://playground.learnqa.ru/api/long_redirect";
        var count = 0;

        while(statusCode != SC_OK) {
            var response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(startUrl)
                    .then();

            statusCode = response.extract().statusCode();
            locationHeader = response.extract().header("Location");
            startUrl = statusCode != SC_OK ? startUrl = locationHeader : startUrl;
            count++;
        }

        System.out.println("Number of redirects: " + count + "\nLast status code: " + statusCode + "\nURL of the last redirect: " + startUrl);
    }
}
