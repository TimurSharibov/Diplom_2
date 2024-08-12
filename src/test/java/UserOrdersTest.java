import clients.AuthClient;
import clients.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static utils.DataGenerator.getRandomEmail;
import static org.hamcrest.Matchers.equalTo;

public class UserOrdersTest extends BaseTest {

    private String accessToken;
    private OrdersClient ordersClient;
    private AuthClient authClient;
    private String email = getRandomEmail();

    @Before
    @DisplayName("Устанавливаем базовый URI для всех запросов и Регистрируем и логинимся для получения accessToken")
    public void setup() {
        super.setup();
        ordersClient = new OrdersClient();
        authClient = new AuthClient();
        accessToken = authClient.registerUser(email, "password123", "Update User")
                .then()
                .extract()
                .path("accessToken");
        accessToken = authClient.loginUser(email, "password123")
                .then()
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Получение заказов с авторизацией")
    public void getOrdersWithAuth() {
        ordersClient.getOrders(accessToken)
                .then()
                .statusCode(200) // Проверяем, что код ответа 200
                .body("success", equalTo(true)); // Проверяем, что поле success равно true
    }

    @Test
    @DisplayName("Получение заказов без авторизации")
    public void getOrdersWithoutAuth() {
        ordersClient.getOrdersWithoutAuth()
                .then()
                .statusCode(401) // Проверяем, что код ответа 401 (Unauthorized)
                .body("success", equalTo(false)) // Проверяем, что поле success равно false
                .body("message", equalTo("You should be authorised")); // Проверяем сообщение об ошибке
    }


}
