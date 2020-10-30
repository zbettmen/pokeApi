package com.example.pokeApi.entities;


import lombok.Data;
import org.springframework.data.annotation.Id;


import java.util.List;

@Data
public class User {

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    @Id
    private String userId;
    private String username;
    private String password;
    private List<String> acl;
}
