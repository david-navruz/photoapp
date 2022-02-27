package com.udemy.photoapp.resource;

import com.udemy.photoapp.model.AppUser;
import com.udemy.photoapp.model.Post;
import com.udemy.photoapp.service.AccountService;
import com.udemy.photoapp.service.CommentService;
import com.udemy.photoapp.service.PostService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostResource {

    private String postImageName;

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CommentService commentService;


    @GetMapping("/list")
    public List<Post> getPostList() {
        return postService.postList();
    }

    @GetMapping("/getPostById/{postId}")
    public Post getOnePostById(@PathVariable("postId") Long id) {
        return postService.getPostById(id);
    }


    @GetMapping("/getPostByUsername/{username}")
    public ResponseEntity<?> getPostByUsername(@PathVariable("username") String username) {
        AppUser appUser = accountService.findByUsername(username);
        if (appUser == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
        try {
            List<Post> postList = postService.findPostByUsername(username);
            return new ResponseEntity<>(postList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error Occured", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/save")
    public ResponseEntity<?> savePost(@RequestBody HashMap<String, String> request) {
        String username = request.get("username");
        AppUser appUser = accountService.findByUsername(username);
        if (appUser == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
        postImageName = RandomStringUtils.randomAlphabetic(10);
        try {
            Post post = postService.savePost(appUser, request, postImageName);
            System.out.println("Post has been saved");
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An Error Occured: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }
        try {
            postService.deletePost(post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occured: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/photo/upload")
    public ResponseEntity<String> fileUpload(@RequestParam("image") MultipartFile multipartFile) {
        try {
            postService.savePostImage(multipartFile, postImageName);
            return new ResponseEntity<>("Picture has been saved", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occured", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/like")
    public ResponseEntity<?> likePost(@RequestBody HashMap<String, String> request) {
        String postId = request.get("postId");
        String username = request.get("username");
        Post post = postService.getPostById(Long.parseLong(postId));
        if (post == null) {
            return new ResponseEntity<>("Post Not Found", HttpStatus.NOT_FOUND);
        }
        AppUser appUser = accountService.findByUsername(username);
        if (appUser == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
        try {
            post.setLikes(1);
            appUser.getLikedPost().add(post);
            accountService.simpleSave(appUser);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Can't like Post! ", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/unLike")
    public ResponseEntity<?> unLikePost(@RequestBody HashMap<String, String> request) {
        String postId = request.get("postId");
        Post post = postService.getPostById(Long.parseLong(postId));
        if (post == null) {
            return new ResponseEntity<>("Post Not Found", HttpStatus.NOT_FOUND);
        }
        String username = request.get("username");
        AppUser appUser = accountService.findByUsername(username);
        if (appUser == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
        try {
            // decrease the number of likes for this post
            post.setLikes(-1);
            // remove the liked post from user
            appUser.getLikedPost().remove(post);
            accountService.simpleSave(appUser);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Can't like Post! ", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/comment/add")
    public ResponseEntity<?> addComment(@RequestBody HashMap<String, String> request) {
        String postId = request.get("postId");
        String username = request.get("username");
        String content = request.get("content");
        Post post = postService.getPostById(Long.parseLong(postId));
        if (post == null) {
            return new ResponseEntity<>("Post Not Found", HttpStatus.NOT_FOUND);
        }
        AppUser appUser = accountService.findByUsername(username);
        if (appUser == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
        try {
            commentService.saveComment(post, username, content);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Comment Not Added.", HttpStatus.BAD_REQUEST);
        }
    }


}
