package com.example.pokeApi.controllers;

;
import com.example.pokeApi.entities.User;
import com.example.pokeApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    //find all users OR one user by username
    @GetMapping //?username=
    @Secured("ROLE_USER")
    public ResponseEntity<List<User>> findAllUsers(@RequestParam(required = false) String username){
        var users = userService.findAll(username);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<User> findUserById(@PathVariable String id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/username/{username}")
    public User findUserByUsername(@PathVariable String username){
        return userService.findByUsername(username);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<User> saveUser(@Validated @RequestBody User user){//@Validated checks the @NOT_EMPTY and such in User Entity
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable String id, @RequestBody User user){
        userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT) //204
    public void deleteUser(@PathVariable String id){
        userService.delete(id);
    }

}
