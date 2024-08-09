import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import modeles.User;
import org.junit.Before;
import org.junit.Test;

import static Utils.DataGenerator.getRandomEmail;
import static org.hamcrest.Matchers.equalTo;

public class OrderCreationTest extends BaseTest {

    private String email = getRandomEmail();
    private OrdersClient ordersClient = new OrdersClient();
    private AuthClient authClient = new AuthClient();

    @Before
    @Override
    @DisplayName("Регистрируем и логинимся для получения accessToken")
    public void setup() {
        super.setup();
        Response registerResponse = authClient.registerUser(email, "password123", "Order User");
        registerResponse.then().statusCode(200);

        Response loginResponse = authClient.loginUser(email, "password123");
        accessToken = loginResponse.then().statusCode(200).extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuth() {
        String validIngredientId = "61c0c5a71d1f82001bdaaa72";
        String orderBody = "{ \"ingredients\": [\"" + validIngredientId + "\"] }";

        Response response = ordersClient.createOrder(accessToken, orderBody);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        String validIngredientId = "61c0c5a71d1f82001bdaaa72";
        String orderBody = "{ \"ingredients\": [\"" + validIngredientId + "\"] }";

        if (accessToken != null) {
            super.deleteUser(); // Удаляем пользователя для теста без авторизации
        }

        Response response = ordersClient.createOrder(null, orderBody);

        response.then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Создание заказа с невалидным хешем ингредиентов")
    public void createOrderWithInvalidIngredient() {
        String invalidIngredientId = "invalidingredientid";
        String orderBody = "{ \"ingredients\": [\"" + invalidIngredientId + "\"] }";

        Response response = ordersClient.createOrder(accessToken, orderBody);

        response.then()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithNoIngredients() {
        String orderBody = "{}";

        Response response = ordersClient.createOrder(accessToken, orderBody);

        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }
}
