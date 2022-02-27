package com.udemy.photoapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private long userRoleId;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    public UserRole() {
    }

    public UserRole(long userRoleId, AppUser appUser, Role role) {
        this.userRoleId = userRoleId;
        this.appUser = appUser;
        this.role = role;
    }

    public UserRole(AppUser appUser, Role role) {
        this.appUser = appUser;
        this.role = role;
    }


}
