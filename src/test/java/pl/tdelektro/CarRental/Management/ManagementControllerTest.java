package pl.tdelektro.CarRental.Management;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.tdelektro.CarRental.Customer.CustomerFacade;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManagementControllerTest {

    @Autowired
    private CustomerFacade customerFacade;
    @Autowired
    PasswordEncoder passwordEncoder;
    private String authToken;


    private static final String SECRET_KEY = "4ecb24cec525eccd07ef06c6d410ab3cb6847445d0f53ea7a26e09297b8fd4ab";

    @Before
    public void warmup() {
        customerFacade.addNewCustomerWithData(
                "test@test.test",
                "test@test.test",
                passwordEncoder.encode("test"),
                "USER");
        authToken = Jwts.builder()
                .setSubject("test@test.test")
                .setClaims(new HashMap<>())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println(authToken);
        RestAssured.baseURI = "http://localhost:8080";
    }

//    @After
//    public void clearTestData() {
//        customerFacade.deleteCustomer((Customer) customerFacade.findCustomerForUserDetails("test@test.test"));
//    }


    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    public void serverIsWorking() {
        RestAssured.basePath = "management/rent";
        given().when().get().then().log().all().statusCode(401);
    }

    @Test
    public void getRentCar() {
        //RestAssured.baseURI = "http://localhost:8080";
        given().header("Authorization", "Barer " + authToken)
                .when()
                .get("management/rent")
                .then()
                .statusCode(200);
    }
}
