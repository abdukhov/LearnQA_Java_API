package org.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;

public class PasswordGuessTest {

    @Test
    public void shouldGuessPassword() {
        var notAuthorisedMessage = "You are NOT authorized";
        List<String> top25passwords = List.of(
            "123456", "123456789", "qwerty", "password", "1234567", "12345678", "12345", "iloveyou",
            "111111", "123123", "abc123", "qwerty123", "1q2w3e4r", "admin", "qwertyuiop", "654321", "555555",
            "lovely", "7777777", "welcome", "888888", "princess", "dragon", "password1", "123qwe"
        );

        for (String item : top25passwords) {
            var params = new HashMap<String, String>();
            params.put("login", "super_admin");
            params.put("password", item);

            var cookie = RestAssured
                    .given()
                    .body(params)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .then()
                    .statusCode(SC_OK)
                    .extract()
                    .cookie("auth_cookie");

            var message = RestAssured
                    .given()
                    .cookie("auth_cookie", cookie)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .then()
                    .statusCode(SC_OK)
                    .extract()
                    .asString();

            if(!message.equals(notAuthorisedMessage)) {
                System.out.println("The correct password is: " + item);
                System.out.println("The phrase is: " + message);
                break;
            }
        }

    }
}
