package com.example.pokeApi.controllers;


import com.example.pokeApi.entities.User;
import com.example.pokeApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD

import org.springframework.http.HttpStatus;
=======
>>>>>>> 3ee2e80f9aa0951c095eb7eb861e5f4f974294ac
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {


        @Autowired
        private UserService userService;

        @PostMapping("/start")
        public ResponseEntity<User> save(@RequestBody User user){
            return ResponseEntity.ok(userService.save(user));
        }

        @Secured({"ROLE_ADMIN"})
        @GetMapping
        public ResponseEntity<List<User>> findAll(@RequestParam(required = false) String name){
            return ResponseEntity.ok(userService.findAll(name));
        }

        @Secured({"ROLE_ADMIN",  "ROLE_USER"})
        @PutMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void updateUser(@PathVariable String id, @RequestBody User user) {
                userService.update(id, user);
        }
}
