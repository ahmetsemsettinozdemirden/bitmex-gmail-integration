package business.admin;

import business.exceptions.ClientException;
import business.exceptions.ServerException;
import models.Admin;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

public class AdminService {

    private final AdminRepository adminRepository;

    @Inject
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin signIn(String username, String password) throws ClientException, ServerException {

        Admin admin = adminRepository.getAdmin(username);

        if (admin == null)
            throw new ClientException("AdminCouldNotFound", "Admin account with username: '" + username + "' couldn't found!");

        if (!admin.getPassword().equals(password))
            throw new ClientException("PasswordDoesNotMatch", "Admin account with username: '" + username + "' couldn't found!");

        try {
            adminRepository.refreshToken(admin);
            admin.save();
        } catch (Exception e) {
            throw new ServerException("RefreshTokenError", e);
        }

        return admin;
    }

    public Admin signUp(String username, String password) throws ClientException, ServerException {

        if ("".equals(username.trim()))
            throw new ClientException("InvalidUsername", "Username can not be empty.");

        if ("".equals(password.trim()))
            throw new ClientException("InvalidPassword", "Password can not be empty.");

        Admin admin = adminRepository.getAdmin(username);

        if (admin != null)
            throw new ClientException("UserExists", "Username already exists. Please use a different username.");

        try {
            admin = adminRepository.createAdmin(username, password);
            admin.save();
        } catch (UnsupportedEncodingException e) {
            throw new ServerException("RefreshToken", e);
        }

        return admin;
    }

}
