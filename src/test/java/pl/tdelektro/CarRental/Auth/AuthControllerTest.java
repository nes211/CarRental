package pl.tdelektro.CarRental.Auth;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {

    private static final String userName = "testuser@test.test";

    @Before
    public void warmUp() {

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;

        String newUserString = """
                {
                "name":"testuser@test.test",
                "emailAddress":"testuser@test.test",
                "password":"test"
                }
                """;

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(newUserString)
                .log()
                .all()
                .post("/auth/register")
                .then()
                .contentType(ContentType.JSON)
                .body("token", notNullValue())
                .statusCode(200);
    }

    @After()
    public void cleanData() {

        RestAssured
                .given()
                .delete("/auth/deleteUser/" + userName);
        RestAssured
                .given()
                .delete("/auth/deleteUser/2testuser@test.test");
    }

    @Test
    @Order(1)
    public void registerCustomerTest() {

        String newUserString = """
                {
                "name":"2testuser@test.test",
                "emailAddress":"2testuser@test.test",
                "password":"test"
                }
                """;

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(newUserString)
                .log()
                .all()
                .post("/auth/register")
                .then()
                .contentType(ContentType.JSON)
                .body("token", notNullValue())
                .statusCode(200)
                .extract()
                .body()
                .jsonPath();

    }

    @Test
    @Order(2)
    public void authenticateTest() {
        String userString = """
                {
                "emailAddress":"testuser@test.test",
                "password" : "test"
                }
                """;

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(userString)
                .when()
                .post("/auth/authenticate")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.getString("token");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @Order(3)
    public void authenticateFailedTest() {
        String userString = """
                {
                "emailAddress":"testuser2@test.test",
                "password" : "test"
                }
                """;

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(userString)
                .when()
                .post("/auth/authenticate")
                .then()
                .statusCode(401)
                .extract().response();

        assertFalse(response.getStatusCode()==200);
    }

    @Test
    @Order(4)
    public void deleteCustomer() {

        String path = "/auth/deleteUser/" + userName;
        RestAssured.given()
                .delete(path)
                .then()
                .statusCode(204);
    }

}
