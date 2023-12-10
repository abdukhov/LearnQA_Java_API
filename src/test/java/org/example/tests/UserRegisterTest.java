package org.example.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import org.example.lib.Assertions;
import org.example.lib.BaseTestCase;
import org.example.lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;

import org.example.lib.ApiCoreRequests;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {
        var email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        var responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, SC_BAD_REQUEST);
        Assertions.assertResponseTextEquals(responseCreateAuth, String.format("Users with email '%s' already exists", email));
    }

    @Test
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = new HashMap<>();
        userData = DataGenerator.getRegistrationData(userData);

        var responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, SC_OK);
        Assertions.assertJsonHasKey(responseCreateAuth, "id");
    }

    /*
        Ex15: Создание пользователя с некорректным email - без символа @
    */
    @Test
    @Description("This test checks registration with an invalid email address")
    @DisplayName("Test create user with invalid email")
    public void testCreateUserWithInvalidEmail() {
        var email = "vinkotovexample.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        var responseCreateAuth = apiCoreRequests.registerUser(
                "https://playground.learnqa.ru/api/user/",
                userData
        );

        Assertions.assertResponseCodeEquals(responseCreateAuth, SC_BAD_REQUEST);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    /*
        Ex15: Создание пользователя без указания одного из полей - с помощью @ParameterizedTest необходимо проверить,
        что отсутствие любого параметра не дает зарегистрировать пользователя
    */
    @Description("This test checks user registration with a missed parameter")
    @DisplayName("Test create user with missed param")
    @ParameterizedTest
    @ArgumentsSource(InvalisRegistrationDataArgumentsProvider.class)
    public void testCreateUserWithMissedParam(Map<String, String> userData, String expectedMessage) {
        var responseCreateAuth = apiCoreRequests.registerUser(
                "https://playground.learnqa.ru/api/user/",
                userData
        );

        Assertions.assertResponseCodeEquals(responseCreateAuth, SC_BAD_REQUEST);
        Assertions.assertResponseTextEquals(responseCreateAuth, expectedMessage);
    }

    static class InvalisRegistrationDataArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(
                            Map.of(
                                    "password", "12345678",
                                    "username", "unknown",
                                    "firstName", "John",
                                    "lastName", "Doe"
                            ),
                            "The following required params are missed: email"
                    ),
                    Arguments.of(
                            Map.of(
                                    "email", "john@doe.org",
                                    "username", "unknown",
                                    "firstName", "John",
                                    "lastName", "Doe"
                            ),
                            "The following required params are missed: password"
                    ),
                    Arguments.of(
                            Map.of(
                                    "email", "john@doe.org",
                                    "password", "12345678",
                                    "firstName", "John",
                                    "lastName", "Doe"
                            ),
                            "The following required params are missed: username"
                    ),
                    Arguments.of(
                            Map.of(
                                    "email", "john@doe.org",
                                    "password", "12345678",
                                    "username", "unknown",
                                    "lastName", "Doe"
                            ),
                            "The following required params are missed: firstName"
                    ),
                    Arguments.of(
                            Map.of(
                                    "email", "john@doe.org",
                                    "password", "12345678",
                                    "username", "unknown",
                                    "firstName", "John"
                            ),
                            "The following required params are missed: lastName"
                    )
            );
        }
    }

    /*
        Ex15: Создание пользователя с очень коротким именем в один символ
    */
    @Test
    @Description("This test checks user registration with a very short username")
    @DisplayName("Test create user with very short name")
    public void testCreateUserWithVeryShortName() {
        var username = "u";
        Map<String, String> userData = new HashMap<>();
        userData.put("username", "u");
        userData = DataGenerator.getRegistrationData(userData);

        var responseCreateAuth = apiCoreRequests.registerUser(
                "https://playground.learnqa.ru/api/user/",
                userData
        );

        Assertions.assertResponseCodeEquals(responseCreateAuth, SC_BAD_REQUEST);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }

    /*
        Ex15: Создание пользователя с очень длинным именем - длиннее 250 символов
    */
    @Test
    @Description("This test checks user registration with a very long username")
    @DisplayName("Test create user with very long name")
    public void testCreateUserWithVeryLongName() {
        var username = "u";
        Map<String, String> userData = new HashMap<>();
        userData.put(
                "username",
                "Hi! I’m Eugen and I am an engineer with a passion for Java, Spring, REST, TDD, and more recently producing great video. I spend most of my time teaching, writing, curating this weekly review and wearing many other hats here on the Baeldung team. Right now I’m working on the courses on Baeldung."
        );
        userData = DataGenerator.getRegistrationData(userData);

        var responseCreateAuth = apiCoreRequests.registerUser(
                "https://playground.learnqa.ru/api/user/",
                userData
        );

        Assertions.assertResponseCodeEquals(responseCreateAuth, SC_BAD_REQUEST);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }
}
