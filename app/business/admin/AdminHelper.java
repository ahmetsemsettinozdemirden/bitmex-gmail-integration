package business.admin;

import business.jwt.JwtHelper;
import models.Admin;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

public class AdminHelper {

    private final JwtHelper jwtHelper;

    @Inject
    public AdminHelper(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    public void refreshToken(Admin admin) throws UnsupportedEncodingException {
        admin.setToken(jwtHelper.getSignedToken(admin.getUsername()));
    }

}
