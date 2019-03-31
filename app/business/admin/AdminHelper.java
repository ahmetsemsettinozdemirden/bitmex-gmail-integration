package business.admin;

import business.jwt.JwtHelper;
import db.models.Admin;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

/**
 * Includes admin helper methods.
 */
public class AdminHelper {

    private final JwtHelper jwtHelper;

    @Inject
    public AdminHelper(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    /**
     * Generates a new token for admin.
     * @param admin Admin entity.
     * @throws UnsupportedEncodingException Server could not generate token.
     */
    public void refreshToken(Admin admin) throws UnsupportedEncodingException {
        admin.setToken(jwtHelper.getSignedToken(admin.getUsername()));
    }

}
