package com.udemy.photoapp.service.impl;

import com.udemy.photoapp.model.AppUser;
import com.udemy.photoapp.model.Role;
import com.udemy.photoapp.model.UserRole;
import com.udemy.photoapp.repo.AppUserRepo;
import com.udemy.photoapp.repo.RoleRepo;
import com.udemy.photoapp.service.AccountService;
import com.udemy.photoapp.utils.Constants;
import com.udemy.photoapp.utils.EmailConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private EmailConstructor emailConstructor;

    @Autowired
    private JavaMailSender mailSender;


    @Transactional
    @Override
    public AppUser saveUser(String name, String username, String email) {
        // generating a random password
        String password = RandomStringUtils.randomAlphabetic(10);
        // encoding the password
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        // creating a user
        AppUser user = new AppUser();
        user.setPassword(encryptedPassword);
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        // creating roles for the user
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, accountService.findUserRoleByName("USER")));
        user.setUserRoles(userRoles);
        appUserRepo.save(user);

        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Constants.TEMP_USER.toPath());
            String fileName = user.getId() + ".png";
            Path path = Paths.get(Constants.USER_FOLDER + fileName);
            Files.write(path, bytes);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        mailSender.send(emailConstructor.constructNewUserEmail(user, password));
        return user;
    }


    @Override
    public void updateUserPassword(AppUser appUser, String newpassword) {
        String encryptedPassword = bCryptPasswordEncoder.encode(newpassword);
        appUser.setPassword(encryptedPassword);
        appUserRepo.save(appUser);
        mailSender.send(emailConstructor.constructResetPasswordEmail(appUser, newpassword));
    }


    @Override
    public AppUser findByUsername(String username) {
        return appUserRepo.findByUsername(username);
    }


    @Override
    public AppUser findByEmail(String email) {
        return appUserRepo.findByEmail(email);
    }


    @Override
    public List<AppUser> userList() {
        return appUserRepo.findAll();
    }


    @Override
    public Role findUserRoleByName(String role) {
        return roleRepo.findRoleByName(role);
    }


    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }


    @Override
    public AppUser updateUser(AppUser appUser, HashMap<String, String> request) {
        String name = request.get("name");
        // String username = request.get("username");
        String email = request.get("email");
        String bio = request.get("bio");
        appUser.setName(name);
        // appUser.setUsername(username);
        appUser.setEmail(email);
        appUser.setBio(bio);
        appUserRepo.save(appUser);
        mailSender.send(emailConstructor.constructUpdateUserProfileEmail(appUser));
        return appUser;
    }


    @Override
    public AppUser findUserById(Long id) {
        return appUserRepo.findUserById(id);
    }


    @Override
    public void deleteUser(AppUser appUser) {
        System.out.println("Deleting the user : " + appUser.getUsername());
        appUserRepo.delete(appUser);
    }


    @Override
    public void resetPassword(AppUser appUser) {
        // generating a random password
        String password = RandomStringUtils.randomAlphabetic(10);
        // encoding the password
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        appUser.setPassword(encryptedPassword);
        appUserRepo.save(appUser);
        mailSender.send(emailConstructor.constructResetPasswordEmail(appUser, password));
    }


    @Override
    public List<AppUser> getUserListByUsername(String username) {
        return appUserRepo.findByUsernameContaining(username);
    }


    @Override
    public AppUser simpleSave(AppUser appUser) {
        appUserRepo.save(appUser);
        mailSender.send(emailConstructor.constructUpdateUserProfileEmail(appUser));
        return appUser;
    }

    @Override
    public String saveUserImage(MultipartFile multipartFile, Long userImageId) {
     /*
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        MultipartFile multipartFile = multipartRequest.getFile(it.next());
        */
        byte[] bytes;
        try {
            Files.deleteIfExists(Paths.get(Constants.USER_FOLDER + "/" + userImageId + ".png"));
            bytes = multipartFile.getBytes();
            Path path = Paths.get(Constants.USER_FOLDER + userImageId + ".png");
            Files.write(path, bytes);
            return "User picture saved to server";
        } catch (IOException e) {
            System.out.println("Error occured. Photo not saved!");
            return "Error occured. Photo not saved!";
        }
    }




}
