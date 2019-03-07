package business.jwt;

import java.io.UnsupportedEncodingException;

public interface JwtHelper {
    String getSignedToken(String username) throws UnsupportedEncodingException;
}
