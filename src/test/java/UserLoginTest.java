import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest extends BaseTest {

    private AuthClient authClient;

    @Before
    @Step("Инициализация клиента авторизации")
    public void setup() {
        super.setup();
        authClient = new AuthClient();
    }

    @Test
    @DisplayName("Логин с корректными учетными данными")
    public void loginWithValidCredentials() {
        // Регистрация пользователя
        authClient.registerUser("loginuser@example.com", "password123", "Login User");

        // Логин с корректными учетными данными
        authClient.loginUser("loginuser@example.com", "password123")
                .then()
                .statusCode(200) // Проверяем, что код ответа 200
                .body("success", equalTo(true)); // Проверяем, что поле success равно true
    }

    @Test
    @DisplayName("Логин с некорректными учетными данными")
    public void loginWithInvalidCredentials() {
        authClient.loginUser("invaliduser@example.com", "wrongpassword")
                .then()
                .statusCode(401) // Проверяем, что код ответа 401 (Unauthorized)
                .body("success", equalTo(false)) // Проверяем, что поле success равно false
                .body("message", equalTo("email or password are incorrect")); // Проверяем сообщение об ошибке
    }
}
