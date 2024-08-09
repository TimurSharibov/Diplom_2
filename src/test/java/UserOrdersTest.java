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
        // ������������� ������� URI ��� ���� ��������
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";

        // ������������ � ��������� ��� ��������� accessToken
        Response response = given()
                .contentType("application/json")
                .body("{ \"email\": \""+ email + "\", \"password\": \"password123\", \"name\": \"Update User\" }")
                .when()
                .post("/auth/register")
                .then()
                .body("success", equalTo(true)) // ���������, ��� ���� success ����� true
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
        // ��������� ����� ������������
//        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @Step("��������� ������� � ������������")
    public void getOrdersWithAuth() {
        given()
                .header("Authorization", accessToken)
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

    @After
    @Step("�������� ���������� ������������")
    public void deleteUser() {
        if (accessToken != null) {
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete("/auth/user")
                    .then()
                    .statusCode(202); // ���������, ��� ��� ������ 202 (Accepted) ��� �������� ������������
        }
    }
}
