import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {

    @Before
    public void setup() {
        // Устанавливаем базовый URI для всех запросов
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
    }

    @Test
    @Step("Логин с корректными учетными данными")
    public void loginWithValidCredentials() {
        // Регистрация пользователя
        given()
                .contentType("application/json")
                .body("{ \"email\": \"loginuser@example.com\", \"password\": \"password123\", \"name\": \"Login User\" }")
                .when()
                .post("/auth/register");

        // Логин с корректными учетными данными
        given()
                .contentType("application/json")
                .body("{ \"email\": \"loginuser@example.com\", \"password\": \"password123\" }")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200) // Проверяем, что код ответа 200
                .body("success", equalTo(true)); // Проверяем, что поле success равно true
    }

    @Test
    @Step("Логин с некорректными учетными данными")
    public void loginWithInvalidCredentials() {
        given()
                .contentType("application/json")
                .body("{ \"email\": \"invaliduser@example.com\", \"password\": \"wrongpassword\" }")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401) // Проверяем, что код ответа 401 (Unauthorized)
                .body("success", equalTo(false)) // Проверяем, что поле success равно false
                .body("message", equalTo("email or password are incorrect")); // Проверяем сообщение об ошибке
    }
}
