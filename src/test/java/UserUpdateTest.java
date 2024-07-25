import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest {

    private String accessToken;

    @Before
    public void setup() {
        // Устанавливаем базовый URI для всех запросов
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";

        // Регистрируем и логинимся для получения accessToken
        accessToken = given()
                .contentType("application/json")
                .body("{ \"email\": \"updateuser@example.com\", \"password\": \"password123\", \"name\": \"Update User\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200)
                .extract().path("accessToken");

        accessToken = given()
                .contentType("application/json")
                .body("{ \"email\": \"updateuser@example.com\", \"password\": \"password123\" }")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().path("accessToken");
    }

    @Test
    @Step("Обновление данных пользователя с авторизацией")
    public void updateUserWithAuth() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body("{ \"name\": \"Updated Name\" }")
                .when()
                .patch("/auth/user")
                .then()
                .statusCode(200) // Проверяем, что код ответа 200
                .body("success", equalTo(true)); // Проверяем, что поле success равно true
    }

    @Test
    @Step("Обновление данных пользователя без авторизации")
    public void updateUserWithoutAuth() {
        given()
                .contentType("application/json")
                .body("{ \"name\": \"Updated Name\" }")
                .when()
                .patch("/auth/user")
                .then()
                .statusCode(401) // Проверяем, что код ответа 401 (Unauthorized)
                .body("success", equalTo(false)) // Проверяем, что поле success равно false
                .body("message", equalTo("You should be authorised")); // Проверяем сообщение об ошибке
    }
}
