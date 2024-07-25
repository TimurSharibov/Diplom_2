import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserCreationTest {

    @Before
    public void setup() {
        // Устанавливаем базовый URI для всех запросов
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
    }

    @Test
    @Step("Создание уникального пользователя")
    public void createUniqueUser() {
        given()
                .contentType("application/json")
                .body("{ \"email\": \"uniqueemail@example.com\", \"password\": \"password123\", \"name\": \"Unique User\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200) // Проверяем, что код ответа 200
                .body("success", equalTo(true)); // Проверяем, что поле success равно true
    }

    @Test
    @Step("Создание пользователя, который уже зарегистрирован")
    public void createExistingUser() {
        // Создаем пользователя
        given()
                .contentType("application/json")
                .body("{ \"email\": \"existingemail@example.com\", \"password\": \"password123\", \"name\": \"Existing User\" }")
                .when()
                .post("/auth/register");

        // Пытаемся создать того же пользователя еще раз
        given()
                .contentType("application/json")
                .body("{ \"email\": \"existingemail@example.com\", \"password\": \"password123\", \"name\": \"Existing User\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(403) // Проверяем, что код ответа 403 (Forbidden)
                .body("success", equalTo(false)) // Проверяем, что поле success равно false
                .body("message", equalTo("User already exists")); // Проверяем сообщение об ошибке
    }

    @Test
    @Step("Создание пользователя с пропущенным обязательным полем")
    public void createUserWithMissingField() {
        given()
                .contentType("application/json")
                .body("{ \"email\": \"missingfield@example.com\", \"password\": \"password123\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(403) // Проверяем, что код ответа 403 (Forbidden)
                .body("success", equalTo(false)) // Проверяем, что поле success равно false
                .body("message", equalTo("Email, password and name are required fields")); // Проверяем сообщение об ошибке
    }
}
