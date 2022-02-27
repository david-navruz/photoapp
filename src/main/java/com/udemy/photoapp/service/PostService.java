package com.udemy.photoapp.service;

import com.udemy.photoapp.model.AppUser;
import com.udemy.photoapp.model.Post;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public interface PostService {

    public Post savePost(AppUser appUser, HashMap<String, String> request, String postImageName);

    public List<Post> postList();

    public Post getPostById(Long id);

    public List<Post> findPostByUsername(String username);

    public Post deletePost(Post post);

    public String savePostImage(MultipartFile multipartFile, String filename);


}
