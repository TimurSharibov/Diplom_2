import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected String accessToken;

    @Before
    @Step("Устанавливаем базовый URI для всех запросов")
    public void setup() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
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
                    .statusCode(202);
            accessToken = null;
        }
    }
}
