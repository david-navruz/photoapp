package com.udemy.photoapp.service;

import com.udemy.photoapp.model.AppUser;
import com.udemy.photoapp.model.Role;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public interface AccountService {

    public AppUser saveUser(String name, String username, String email);

    public AppUser findByUsername(String username);

    public AppUser findByEmail(String email);

    public List<AppUser> userList();

    public Role findUserRoleByName(String role);

    public Role saveRole(Role role);

    public void updateUserPassword(AppUser appUser, String newpassword);

    public AppUser updateUser(AppUser user, HashMap<String, String> request);

    public AppUser findUserById(Long id);

    public void deleteUser(AppUser appUser);

    public void resetPassword(AppUser appUser);

    public List<AppUser> getUserListByUsername(String username);

    public AppUser simpleSave(AppUser appUser);

    public String saveUserImage(MultipartFile multipartFile, Long userImageId);


}
