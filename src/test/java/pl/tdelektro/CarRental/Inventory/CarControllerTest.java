package pl.tdelektro.CarRental.Inventory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.tdelektro.CarRental.CarRentalApplication;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CarRentalApplication.class)
@Transactional
public class CarControllerTest {

    private static String SECRET_KEY;
    private static String customerToken;
    private static String adminToken;


    @Before
    public void warmUp() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/test.properties");
        try {
            FileReader fr = new FileReader("src/test/resources/test.properties");
            BufferedReader br = new BufferedReader(fr);
            SECRET_KEY = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        customerToken = generateJwt("test@test.test");
        adminToken = generateJwt("admin@admin.admin");

    }

    @After()
    public void cleanData() throws IOException {


        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + adminToken)
                .log().all()
                .when()
                .get("/car/all");
        String responseString = response.getBody().asString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<CarDTO> carList = objectMapper.readValue((JsonParser) response, objectMapper.getTypeFactory().constructCollectionType(List.class, CarDTO.class));

        for (int i = 0; i < carList.size(); i++) {
            String testRegistration = carList.get(i).getMake();
            String testMake = carList.get(i).getMake();
            if (testRegistration.equals("") || testMake.equals("test")) {
                String carPath = "/car/" + carList.get(i).getRegistration();
                RestAssured
                        .given()
                        .header("Authorization", "Bearer " + adminToken)
                        .when()
                        .delete(carPath)
                        .then()
                        .statusCode(204);

            }
        }


//        for(CarDTO car : carResponse){
//            System.out.println(car);
//            if(car.getRegistration().equals("test") || car.getRegistration().equals("")){
//                String carPath = "/car/"+car.getRegistration();
//                RestAssured
//                        .given()
//                        .header("Authorization", "Bearer "+adminToken)
//                        .when()
//                        .delete(carPath)
//                        .then()
//                        .statusCode(204);
//            }
//        }
    }


    public String generateJwt(String user) {
        String jwt = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(signInKey(), SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    private Key signInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    public void getCarWithIdTest() {
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
    public void getAvailableCarsTest() {
        Set carSet = RestAssured.given().header("Authorization", "Bearer " + customerToken)
                .log().all()
                .when()
                .get("/car")
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
                .post("/car/addNew")
                .then()
                .statusCode(201);
    }
}