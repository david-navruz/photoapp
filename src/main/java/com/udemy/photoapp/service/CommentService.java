package com.udemy.photoapp.service;

import com.udemy.photoapp.model.Comment;
import com.udemy.photoapp.model.Post;

public interface CommentService {

    public void saveComment(Post post, String username, String content);

}
