package app.learn.integrations.jwt;

import app.learn.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

public class JwtIntegration {
    private static final SecretKey secretKey;

    static {
        secretKey = Jwts.SIG.HS256.key().build();
    }

    public static String createJwt(Long userId, UserRole role) {

        return Jwts.builder().header().type("JWT")
            .and().subject(String.valueOf(userId))
            .expiration(Timestamp.from(Instant.now().plus(Duration.ofHours(2))))
            .claim("role", role.name()).signWith(secretKey).compact();

    }

    public static Jws<Claims> verifyJwt(String jws) throws JwtException {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jws);

    }
}
