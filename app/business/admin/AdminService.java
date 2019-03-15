package business.admin;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import models.Admin;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminHelper adminHelper;

    @Inject
    public AdminService(AdminRepository adminRepository, AdminHelper adminHelper) {
        this.adminRepository = adminRepository;
        this.adminHelper = adminHelper;
    }

    public Admin signIn(String username, String password) throws ClientException, ServerException {

        Admin admin = adminRepository.getAdmin(username);

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

    public Admin signUp(String username, String password) throws ClientException, ServerException {

        if (username == null || username.equals(""))
            throw new ClientException("invalidUsername", "Username can not be null or empty.");

        if (password == null || password.equals(""))
            throw new ClientException("invalidPassword", "Password can not be null or empty.");

        Admin admin = adminRepository.getAdmin(username);

        if (admin != null)
            throw new ClientException("userExists", "Username already exists. Please use a different username.");

        try {
            admin = adminRepository.createAdmin(username, password);
            admin.save();
        } catch (UnsupportedEncodingException e) {
            throw new ServerException("refreshToken", e);
        }

        return admin;
    }

}
