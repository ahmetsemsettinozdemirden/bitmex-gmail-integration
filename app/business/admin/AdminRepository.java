package business.admin;

import business.jwt.JwtHelper;
import models.Admin;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

public class AdminRepository {

    private final JwtHelper jwtHelper;

    @Inject
    public AdminRepository(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    public Admin getAdmin(String username) {
        return Admin.finder.query().where().eq("username", username).findOne();
    }

    public Admin createAdmin(String username, String password) throws UnsupportedEncodingException {
        Admin admin = new Admin(username, password, null);
        refreshToken(admin);
        return admin;
    }

    // TODO: move it to Admin.class
    public void refreshToken(Admin admin) throws UnsupportedEncodingException {
        admin.setToken(jwtHelper.getSignedToken(admin.getUsername()));
    }

    public int getTotalAdminCount() {
        return Admin.finder.query().select("id").findCount();
    }

}
