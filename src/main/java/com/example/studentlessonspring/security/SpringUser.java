package com.example.studentlessonspring.security;

import com.example.studentlessonspring.entity.User;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
public class SpringUser extends org.springframework.security.core.userdetails.User {
    private  User user;

    public SpringUser(User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getUserType().name()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
