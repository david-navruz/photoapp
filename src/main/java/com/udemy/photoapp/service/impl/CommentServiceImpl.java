package com.udemy.photoapp.service.impl;

import com.udemy.photoapp.model.Comment;
import com.udemy.photoapp.model.Post;
import com.udemy.photoapp.repo.CommentRepo;
import com.udemy.photoapp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Transactional
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepo commentRepo;


    @Override
    public void saveComment(Post post, String username, String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUsername(username);
        comment.setPostedDate(new Date());
        post.setComments(comment);
        commentRepo.save(comment);
    }



}
