package db.repository;

import business.admin.AdminHelper;
import business.exceptions.ClientException;
import db.models.Admin;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Admin repository that includes create, fetch, update and delete methods.
 */
public class AdminRepository {

    private final AdminHelper adminHelper;

    @Inject
    public AdminRepository(AdminHelper adminHelper) {
        this.adminHelper = adminHelper;
    }

    /**
     * Fetches all admin accounts.
     * @return All admin accounts.
     */
    public List<Admin> getAll() {
        return Admin.finder.all();
    }

    /**
     * Fetches admin account by id.
     * @param id Admin id.
     * @return Admin account.
     */
    public Admin get(Long id) {
        return Admin.finder.byId(id);
    }

    /**
     * Fetches admin account by username.
     * @param username Admin username.
     * @return Admin account.
     */
    public Admin get(String username) {
        return Admin.finder.query().where().eq("username", username).findOne();
    }

    /**
     * Creates admin account for given username and password..
     * @param username Admin username.
     * @param password Admin password.
     * @return Created admin.
     * @throws UnsupportedEncodingException Token generation error.
     */
    public Admin create(String username, String password) throws UnsupportedEncodingException {
        Admin admin = new Admin(username, password, null);
        adminHelper.refreshToken(admin);
        return admin;
    }

    /**
     * Updates admin account by id, username and password.
     * @param id Admin id.
     * @param username Admin username.
     * @param password Admin password.
     * @return Updated admin account.
     * @throws ClientException If username or password is invalid or admin does not exist for given id.
     */
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

    /**
     * Deletes admin account by id.
     * @param id Admin id.
     * @return Deleted admin account.
     * @throws ClientException Admin account could not found.
     */
    public boolean delete(Long id) throws ClientException {

        Admin admin = get(id);

        if (admin == null)
            throw new ClientException("adminNotFound", "Admin could not found!");

        return admin.delete();
    }

    /**
     * Finds total admin count.
     * @return Total admin count.
     */
    public int getTotalAdminCount() {
        return Admin.finder.query().findCount();
    }

}
