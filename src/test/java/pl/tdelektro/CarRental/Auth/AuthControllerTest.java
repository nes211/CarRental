package pl.tdelektro.CarRental.Auth;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.tdelektro.CarRental.Inventory.CarDTO;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static pl.tdelektro.CarRental.Inventory.CarControllerTest.generateJwt;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AuthControllerTest {

    private static String DB_NAME = "37931948_tom";
    private static String SECRET_KEY;
    private static String JDBC_URL;
    private static String USER;
    private static String PASSWORD;
    private static String FILE_PATH = "src/test/resources/backup.sql";
    private static String customerToken;
    private static String adminToken;


    @BeforeClass
    public static void warmUp() {
        customerToken = generateJwt("test@test.test");
        adminToken = generateJwt("admin@admin.admin");

    }

    @AfterClass()
    public static void cleanData() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + adminToken)
                .get("/car/all");
        List<CarDTO> carDTOList = response.jsonPath().getList("$", CarDTO.class);

        for (int i = 0; i < carDTOList.size(); i++) {
            String testRegistration = carDTOList.get(i).getRegistration();
            String testMake = carDTOList.get(i).getMake();
            if (testRegistration.equals("") || testMake.equals("test")) {
                String carPath = "/car/" + carDTOList.get(i).getRegistration();
                RestAssured
                        .given()
                        .header("Authorization", "Bearer " + adminToken)
                        .when()
                        .delete(carPath)
                        .then()
                        .statusCode(204);
            }
        }
    }


    @Test
    public void createCustomerTest() {
        RestAssured
                .given()
                .header("Authorization", "Bearer " + customerToken)
                .log()
                .all()
                .get("/car/5")
                .then()
                .body("model", equalTo("1500 GLE"))
                .statusCode(200);
    }

    @Test
    public void logInCustomerTest() {
        Set carSet = RestAssured.given().header("Authorization", "Bearer " + customerToken)
                .log().all()
                .when()
                .get("/customer/register")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("$", Set.class);

        assertThat(carSet, notNullValue());
    }

    @Test
    public void addNewCarTest() {
        String carJson = """
                {
                "make" : "test",
                "model" : "test",
                "type" : "test",
                "registration" : "RE5PECT",
                "modelYear" : "1900",
                "odeDayCost" : "2",
                "isAvailable" : "true"
                }
                """;
        RestAssured
                .given()
                .log()
                .all()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(carJson)
                .post("/customer/login")
                .then()
                .statusCode(201);
    }
}