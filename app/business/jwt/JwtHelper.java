package business.jwt;

import java.io.UnsupportedEncodingException;

public interface JwtHelper {
    String getSignedToken(Long id) throws UnsupportedEncodingException;
}
