import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class OrderCreationTest {

    private String accessToken;
    private String validIngredientId = "61c0c5a71d1f82001bdaaa72"; // Замените на реальный ID ингредиента
    private String invalidIngredientId = "invalidingredientid"; // Неверный ID ингредиента

    @Before
    public void setup() {
        // Устанавливаем базовый URI для всех запросов
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";

        // Регистрация пользователя
        given()
                .contentType("application/json")
                .body("{ \"email\": \"orderuser77@example.com\", \"password\": \"password123\", \"name\": \"Order User\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200);

        // Логин для получения accessToken
        accessToken = given()
                .contentType("application/json")
                .body("{ \"email\": \"orderuser77@example.com\", \"password\": \"password123\" }")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().path("accessToken");

        // Вывод токена для проверки
        System.out.println("Access Token: " + accessToken);
    }

    @Test
    @Step("Создание заказа с авторизацией")
    public void createOrderWithAuth() {
        Response response = given()
                .header("Authorization", "Bearer " + accessToken) // Передача токена авторизации
                .contentType("application/json")
                .body("{ \"ingredients\": [\"" + validIngredientId + "\"] }") // Корректное тело запроса
                .log().all() // Логирование запроса
                .when()
                .post("/orders")
                .then()
                .log().all() // Логирование ответа
                .extract().response();

        // Вывод тела ответа для диагностики
        System.out.println("Response Body: " + response.getBody().asString());

        // Проверка статуса и тела ответа
        response.then()
                .statusCode(200) // Ожидаем код ответа 200
                .body("success", equalTo(true)); // Ожидаем, что поле success равно true
    }

    @Test
    @Step("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        Response response = given()
                .contentType("application/json")
                .body("{ \"ingredients\": [\"" + validIngredientId + "\"] }")
                .log().all() // Логирование запроса
                .when()
                .post("/orders")
                .then()
                .log().all() // Логирование ответа
                .extract().response();

        // Вывод тела ответа для диагностики
        System.out.println("Response Body: " + response.getBody().asString());

        // Проверка статуса и тела ответа
        response.then()
                .statusCode(401); // Ожидаем код ответа 401 (Unauthorized)
    }

    @Test
    @Step("Создание заказа с невалидным хешем ингредиентов")
    public void createOrderWithInvalidIngredient() {
        Response response = given()
                .header("Authorization", "Bearer " + accessToken) // Передача токена авторизации
                .contentType("application/json")
                .body("{ \"ingredients\": [\"" + invalidIngredientId + "\"] }")
                .log().all() // Логирование запроса
                .when()
                .post("/orders")
                .then()
                .log().all() // Логирование ответа
                .extract().response();

        // Вывод тела ответа для диагностики
        System.out.println("Response Body: " + response.getBody().asString());

        // Проверка статуса и тела ответа
        response.then()
                .statusCode(500); // Ожидаем код ответа 500 (Internal Server Error)
    }

    @Test
    @Step("Создание заказа без ингредиентов")
    public void createOrderWithNoIngredients() {
        Response response = given()
                .header("Authorization", "Bearer " + accessToken) // Передача токена авторизации
                .contentType("application/json")
                .body("{}") // Пустое тело запроса
                .log().all() // Логирование запроса
                .when()
                .post("/orders")
                .then()
                .log().all() // Логирование ответа
                .extract().response();

        // Вывод тела ответа для диагностики
        System.out.println("Response Body: " + response.getBody().asString());

        // Проверка статуса и тела ответа
        response.then()
                .statusCode(400) // Ожидаем код ответа 400 (Bad Request)
                .body("success", equalTo(false)) // Ожидаем, что поле success равно false
                .body("message", equalTo("Ingredient ids must be provided")); // Ожидаем сообщение об ошибке
    }
}
