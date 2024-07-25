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
        // ������������� ������� URI ��� ���� ��������
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";

        // ������������ � ��������� ��� ��������� accessToken
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
    @Step("��������� ������� � ������������")
    public void getOrdersWithAuth() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/orders")
                .then()
                .statusCode(200) // ���������, ��� ��� ������ 200
                .body("success", equalTo(true)); // ���������, ��� ���� success ����� true
    }

    @Test
    @Step("��������� ������� ��� �����������")
    public void getOrdersWithoutAuth() {
        given()
                .when()
                .get("/orders")
                .then()
                .statusCode(401) // ���������, ��� ��� ������ 401 (Unauthorized)
                .body("success", equalTo(false)) // ���������, ��� ���� success ����� false
                .body("message", equalTo("You should be authorised")); // ��������� ��������� �� ������
    }
}
