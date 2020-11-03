package com.example.pokeApi.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;


import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@Slf4j
@Data
public class User  {

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
