import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserCreationTest {

    @Before
    public void setup() {
        // ������������� ������� URI ��� ���� ��������
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
    }

    @Test
    @Step("�������� ����������� ������������")
    public void createUniqueUser() {
        given()
                .contentType("application/json")
                .body("{ \"email\": \"uniqueemail@example.com\", \"password\": \"password123\", \"name\": \"Unique User\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200) // ���������, ��� ��� ������ 200
                .body("success", equalTo(true)); // ���������, ��� ���� success ����� true
    }

    @Test
    @Step("�������� ������������, ������� ��� ���������������")
    public void createExistingUser() {
        // ������� ������������
        given()
                .contentType("application/json")
                .body("{ \"email\": \"existingemail@example.com\", \"password\": \"password123\", \"name\": \"Existing User\" }")
                .when()
                .post("/auth/register");

        // �������� ������� ���� �� ������������ ��� ���
        given()
                .contentType("application/json")
                .body("{ \"email\": \"existingemail@example.com\", \"password\": \"password123\", \"name\": \"Existing User\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(403) // ���������, ��� ��� ������ 403 (Forbidden)
                .body("success", equalTo(false)) // ���������, ��� ���� success ����� false
                .body("message", equalTo("User already exists")); // ��������� ��������� �� ������
    }

    @Test
    @Step("�������� ������������ � ����������� ������������ �����")
    public void createUserWithMissingField() {
        given()
                .contentType("application/json")
                .body("{ \"email\": \"missingfield@example.com\", \"password\": \"password123\" }")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(403) // ���������, ��� ��� ������ 403 (Forbidden)
                .body("success", equalTo(false)) // ���������, ��� ���� success ����� false
                .body("message", equalTo("Email, password and name are required fields")); // ��������� ��������� �� ������
    }
}
