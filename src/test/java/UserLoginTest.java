import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {

    @Before
    public void setup() {
        // ������������� ������� URI ��� ���� ��������
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
    }

    @Test
    @Step("����� � ����������� �������� �������")
    public void loginWithValidCredentials() {
        // ����������� ������������
        given()
                .contentType("application/json")
                .body("{ \"email\": \"loginuser@example.com\", \"password\": \"password123\", \"name\": \"Login User\" }")
                .when()
                .post("/auth/register");

        // ����� � ����������� �������� �������
        given()
                .contentType("application/json")
                .body("{ \"email\": \"loginuser@example.com\", \"password\": \"password123\" }")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200) // ���������, ��� ��� ������ 200
                .body("success", equalTo(true)); // ���������, ��� ���� success ����� true
    }

    @Test
    @Step("����� � ������������� �������� �������")
    public void loginWithInvalidCredentials() {
        given()
                .contentType("application/json")
                .body("{ \"email\": \"invaliduser@example.com\", \"password\": \"wrongpassword\" }")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401) // ���������, ��� ��� ������ 401 (Unauthorized)
                .body("success", equalTo(false)) // ���������, ��� ���� success ����� false
                .body("message", equalTo("email or password are incorrect")); // ��������� ��������� �� ������
    }
}
