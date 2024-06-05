package pl.tdelektro.CarRental.Auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import static io.restassured.RestAssured.given;


public class AuthGenerationTest {

    public String authToken;
    @Value("${token.secret.key}")
    private String SECRET_KEY;

    @Before
    public void warmup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @After
    public void clearTestData() {

    }

    public String testTokenWithRandomData() {
        this.authToken = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("test@test.test")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        return authToken;
    }

    private Key getSignInKey() {

        if(SECRET_KEY == null){

            try {
                BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/test.properties"));
                SECRET_KEY = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    public void getRentCar() {
        RestAssured.baseURI = "http://localhost:8080";

        String requestJson =
                """
                {
                    "customerEmail":"test@test.test",
                    "startDate":"2024-05-05 10:22:00",
                    "endDate":"2024-05-11 10:22:00",
                    "carId":"5"
                }
                """;

        authToken = testTokenWithRandomData();
        given().header("Authorization", "Barer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestJson)
                .when()
                .get("car/5")
                .then()
                .log()
                .all()
                .statusCode(401);
    }
}
