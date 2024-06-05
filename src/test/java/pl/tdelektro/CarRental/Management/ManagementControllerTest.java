package pl.tdelektro.CarRental.Management;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.tdelektro.CarRental.Auth.AuthGenerationTest;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ManagementControllerTest {


    AuthGenerationTest authGeneration = new AuthGenerationTest();

    @Before
    public void warmup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @After
    public void clearTempData(){
        authGeneration.clearTestData();
    }


    @Test
    public void testWithoutAuth() {
        RestAssured.basePath = "/management/rent";
        given().when().get().then().log().all().statusCode(401);
    }

    @Test
    public void testWithAuth() {
        String requestJson = "{\n" +
                "    \"customerEmail\":\"tom@tdelektro.pl\",\n" +
                "    \"startDate\":\"2024-05-05 10:22:00\",\n" +
                "    \"endDate\":\"2024-05-11 10:22:00\",\n" +
                "    \"carId\":\"5\"\n" +
                "}";


        given().header("Authorization", "Barer " + authGeneration.authToken)
                .contentType(ContentType.JSON)
                .body(requestJson)
                .when()
                .post("/management/rent")
                .then()
                .statusCode(401);
    }
}
