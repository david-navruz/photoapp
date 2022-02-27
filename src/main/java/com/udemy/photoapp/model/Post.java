package com.udemy.photoapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    private String name;

    @Column(columnDefinition = "text")
    private String caption;

    private String username;
    private String location;
    private int likes;

    @CreationTimestamp
    private Date postedDate;
    private Long userImageId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private List<Comment> commentList;


    @JsonIgnore
    public void  setComments(Comment comment){
        if(comment != null){
            this.commentList.add(comment);
        }
    }




}
