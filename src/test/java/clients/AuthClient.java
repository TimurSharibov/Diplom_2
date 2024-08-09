package clients;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AuthClient {

    private static final String REGISTER_ENDPOINT = "/auth/register";
    private static final String LOGIN_ENDPOINT = "/auth/login";
    private static final String UPDATE_USER_ENDPOINT = "/auth/user";
    private static final String DELETE_USER_ENDPOINT = "/auth/user";

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

    @Step("Обновление данных пользователя")
    public Response updateUser(String accessToken, String newName) {
        return RestAssured.given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body("{ \"name\": \"" + newName + "\" }")
                .when()
                .patch(UPDATE_USER_ENDPOINT);
    }

    @Step("Обновление данных пользователя без авторизации")
    public Response updateUserWithoutAuth(String newName) {
        return RestAssured.given()
                .contentType("application/json")
                .body("{ \"name\": \"" + newName + "\" }")
                .when()
                .patch(UPDATE_USER_ENDPOINT);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return RestAssured.given()
                .header("Authorization", accessToken)
                .when()
                .delete(DELETE_USER_ENDPOINT);
    }
}
