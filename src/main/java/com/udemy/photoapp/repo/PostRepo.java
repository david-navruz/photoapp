package com.udemy.photoapp.repo;

import com.udemy.photoapp.model.AppUser;
import com.udemy.photoapp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {


    @Query("SELECT p FROM Post p ORDER BY p.postedDate DESC")
    public List<Post> findAll();

    @Query("SELECT p FROM Post p WHERE p.username=:username ORDER BY p.postedDate DESC")
    public List<Post> findByUsername(@Param("username") String username);

    @Query("SELECT p FROM Post p WHERE p.id=:x ORDER BY p.postedDate DESC")
    public Post findPostById(@Param("x") Long id);

    @Modifying
    @Query("DELETE Post WHERE id=:x")
    public Post deletePostById(@Param("x") Long id);


}
