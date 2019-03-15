package business.admin;

import models.Admin;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

public class AdminRepository {

    private final AdminHelper adminHelper;

    @Inject
    public AdminRepository(AdminHelper adminHelper) {
        this.adminHelper = adminHelper;
    }

    public Admin getAdmin(String username) {
        return Admin.finder.query().where().eq("username", username).findOne();
    }

    public Admin createAdmin(String username, String password) throws UnsupportedEncodingException {
        Admin admin = new Admin(username, password, null);
        adminHelper.refreshToken(admin);
        return admin;
    }

    public int getTotalAdminCount() {
        return Admin.finder.query().findCount();
    }

}
