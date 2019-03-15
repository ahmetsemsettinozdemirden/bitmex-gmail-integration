package business.admin;

import business.exceptions.ClientException;
import models.Admin;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class AdminRepository {

    private final AdminHelper adminHelper;

    @Inject
    public AdminRepository(AdminHelper adminHelper) {
        this.adminHelper = adminHelper;
    }

    public List<Admin> getAll() {
        return Admin.finder.all();
    }

    public Admin get(Long id) {
        return Admin.finder.byId(id);
    }

    public Admin get(String username) {
        return Admin.finder.query().where().eq("username", username).findOne();
    }

    public Admin create(String username, String password) throws UnsupportedEncodingException {
        Admin admin = new Admin(username, password, null);
        adminHelper.refreshToken(admin);
        return admin;
    }

    public Admin update(Long id, String username, String password) throws ClientException {

        if (username == null || username.equals(""))
            throw new ClientException("invalidUsername", "Username can not be null or empty.");

        if (password == null || password.equals(""))
            throw new ClientException("invalidPassword", "Password can not be null or empty.");

        Admin admin = get(id);

        if (admin == null)
            throw new ClientException("userNotFound", "Username does not exist.");

        admin.setUsername(username);
        admin.setPassword(password);
        admin.save();
        return admin;
    }

    public boolean delete(Long id) throws ClientException {

        Admin admin = get(id);

        if (admin == null)
            throw new ClientException("adminNotFound", "Admin could not found!");

        return admin.delete();
    }

    public int getTotalAdminCount() {
        return Admin.finder.query().findCount();
    }

}
