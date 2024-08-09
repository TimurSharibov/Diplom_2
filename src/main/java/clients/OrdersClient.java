package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrdersClient {

    private static final String ORDERS_ENDPOINT = "/orders";

    @Step("Создание заказа")
    public Response createOrder(String accessToken, String body) {
        return RestAssured.given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(body)
                .when()
                .post(ORDERS_ENDPOINT);
    }

    @Step("Получение заказов")
    public Response getOrders(String accessToken) {
        return RestAssured.given()
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS_ENDPOINT);
    }
}
