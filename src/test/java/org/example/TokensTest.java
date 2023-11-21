 package org.example;

import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import static java.lang.Thread.sleep;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;

 public class TokensTest {

    @Test
    public void shouldUseTokens() {
        var getTokenResponse = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .then();

        getTokenResponse.assertThat().statusCode(SC_OK);

        var token = getTokenResponse.extract().path("token").toString();
        var seconds = (Integer)getTokenResponse.extract().path("seconds");

        try {
            sleep(seconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var getResultResponse = RestAssured
                .given()
                .param("token", token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .then();

        var result = getResultResponse.assertThat().statusCode(SC_OK).extract().path("result");
        assertThat("\"result\" field should not be null", result != null);
    }
}
