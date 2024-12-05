import clients.AuthClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static utils.DataGenerator.getRandomEmail;
import static org.hamcrest.Matchers.equalTo;

public class UserCreationTest extends BaseTest {

    private String email = getRandomEmail();
    private AuthClient authClient = new AuthClient();

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUser() {
        Response response = authClient.registerUser(email, "password123", "Unique User");
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));

        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createExistingUser() {
        authClient.registerUser("existingemail@example.com", "password123", "Existing User");

        Response response = authClient.registerUser("existingemail@example.com", "password123", "Existing User");
        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя с пропущенным обязательным полем")
    public void createUserWithMissingField() {
        Response response = authClient.registerUser("missingfield@example.com", null, "userName");
        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
