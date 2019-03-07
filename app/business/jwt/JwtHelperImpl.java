package business.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.typesafe.config.Config;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class JwtHelperImpl implements JwtHelper {

    private Config config;

    @Inject
    public JwtHelperImpl(Config config) {
        this.config = config;
    }

    public String getSignedToken(String username) throws UnsupportedEncodingException {
        String secret = config.getString("play.http.secret.key");
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("bitmex-api")
                .withClaim("username", username)
                .withExpiresAt(Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusYears(999L).toInstant()))
                .sign(algorithm);
    }

}
