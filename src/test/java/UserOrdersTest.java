import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserOrdersTest {

    private String accessToken;

    @Before
    public void setup() {
        // Устанавливаем базовый URI для всех запросов
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";

        // Регистрируем и логинимся для получения accessToken
        accessToken = given()
                .contentType("application/json")
                .body("{ \"email\": \"ordersuser@example.com\", \"password\": \"password123\", \"name\": \"Orders User\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200)
                .extract().path("accessToken");

        accessToken = given()
                .contentType("application/json")
                .body("{ \"email\": \"ordersuser@example.com\", \"password\": \"password123\" }")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().path("accessToken");
    }

    @Test
    @Step("Получение заказов с авторизацией")
    public void getOrdersWithAuth() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/orders")
                .then()
                .statusCode(200) // Проверяем, что код ответа 200
                .body("success", equalTo(true)); // Проверяем, что поле success равно true
    }

    @Test
    @Step("Получение заказов без авторизации")
    public void getOrdersWithoutAuth() {
        given()
                .when()
                .get("/orders")
                .then()
                .statusCode(401) // Проверяем, что код ответа 401 (Unauthorized)
                .body("success", equalTo(false)) // Проверяем, что поле success равно false
                .body("message", equalTo("You should be authorised")); // Проверяем сообщение об ошибке
    }
}
