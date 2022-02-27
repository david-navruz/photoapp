package com.udemy.photoapp.repo;

import com.udemy.photoapp.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {




}
