package business.admin;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import db.models.Admin;
import db.repository.AdminRepository;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

/**
 * Admin authentication service.
 */
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminHelper adminHelper;

    @Inject
    public AdminService(AdminRepository adminRepository, AdminHelper adminHelper) {
        this.adminRepository = adminRepository;
        this.adminHelper = adminHelper;
    }

    /**
     * Validates admin account with username and password.
     * @param username Admin's username.
     * @param password Admin's password.
     * @return Validated admin.
     * @throws ClientException Username or password is invalid.
     * @throws ServerException Server could not generate token.
     */
    public Admin signIn(String username, String password) throws ClientException, ServerException {

        Admin admin = adminRepository.get(username);

        if (admin == null)
            throw new ClientException("adminCouldNotFound", "Admin account with username: '" + username + "' couldn't found!");

        if (!admin.getPassword().equals(password))
            throw new ClientException("passwordDoesNotMatch", "Admin account with username: '" + username + "' couldn't found!");

        try {
            adminHelper.refreshToken(admin);
            admin.save();
        } catch (Exception e) {
            throw new ServerException("refreshTokenError", e);
        }

        return admin;
    }

    /**
     * Creates a new admin account.
     * @param username Admin's username.
     * @param password Admin's password.
     * @return Created admin.
     * @throws ClientException Username or password is invalid or username already exists.
     * @throws ServerException Server could not generate token.
     */
    public Admin signUp(String username, String password) throws ClientException, ServerException {

        if (username == null || username.equals(""))
            throw new ClientException("invalidUsername", "Username can not be null or empty.");

        if (password == null || password.equals(""))
            throw new ClientException("invalidPassword", "Password can not be null or empty.");

        Admin admin = adminRepository.get(username);

        if (admin != null)
            throw new ClientException("userExists", "Username already exists. Please use a different username.");

        try {
            admin = adminRepository.create(username, password);
            admin.save();
        } catch (UnsupportedEncodingException e) {
            throw new ServerException("refreshToken", e);
        }

        return admin;
    }

}
