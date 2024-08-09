import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static Utils.DataGenerator.getRandomEmail;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserOrdersTest {

    private String accessToken;
    private String email = getRandomEmail();

    @Before
    public void setup() {
        // Устанавливаем базовый URI для всех запросов
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";

        // Регистрируем и логинимся для получения accessToken
        Response response = given()
                .contentType("application/json")
                .body("{ \"email\": \""+ email + "\", \"password\": \"password123\", \"name\": \"Update User\" }")
                .when()
                .post("/auth/register")
                .then()
                .body("success", equalTo(true)) // Проверяем, что поле success равно true
                .statusCode(200)
                .extract()
                .response();


        accessToken = given()
                .contentType("application/json")
                .body("{ \"email\": \""+ email +"\", \"password\": \"password123\" }")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().path("accessToken");
        // Сохраняем токен пользователя
//        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @Step("Получение заказов с авторизацией")
    public void getOrdersWithAuth() {
        given()
                .header("Authorization", accessToken)
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

    @After
    @Step("Удаление созданного пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete("/auth/user")
                    .then()
                    .statusCode(202); // Проверяем, что код ответа 202 (Accepted) при удалении пользователя
        }
    }
}
