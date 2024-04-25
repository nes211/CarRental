package pl.tdelektro.CarRental.Auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import pl.tdelektro.CarRental.Customer.CustomerFacade;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@AllArgsConstructor
public class AuthGeneration {
    JwtService jwtService;
    UserDetailsServiceImpl userDetailsService;
    CustomerFacade customerFacade;

    private static final String SECRET_KEY = "4ecb24cec525eccd07ef06c6d410ab3cb6847445d0f53ea7a26e09297b8fd4ab";
    public String generateTokenForTests(){
        String token = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("test@test.test")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60  * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)

                .compact();

        return token;
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
