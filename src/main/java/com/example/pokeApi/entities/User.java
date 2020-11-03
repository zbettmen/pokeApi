package com.example.pokeApi.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;



import java.util.List;

@Builder
@AllArgsConstructor
@Slf4j
@Data
public class User  {
    @Id
    private String userId;
    private String username;
    private String password;
    private List<String> acl;
}
