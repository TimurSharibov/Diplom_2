package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {

    private static final String REGISTER_ENDPOINT = "/auth/register";
    private static final String LOGIN_ENDPOINT = "/auth/login";

    @Step("Регистрация пользователя")
    public Response registerUser(String email, String password, String name) {
        return RestAssured.given()
                .contentType("application/json")
                .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"" + name + "\" }")
                .when()
                .post(REGISTER_ENDPOINT);
    }

    @Step("Логин пользователя")
    public Response loginUser(String email, String password) {
        return RestAssured.given()
                .contentType("application/json")
                .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }")
                .when()
                .post(LOGIN_ENDPOINT);
    }
}
