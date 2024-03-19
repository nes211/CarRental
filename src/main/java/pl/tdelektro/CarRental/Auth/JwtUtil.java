package pl.tdelektro.CarRental.Auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

class JwtUtil {
    private static final String SECRET = "my-secret-key";
    private static final long EXPIRATION_TIME = 3_600; // 1 hour

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
}