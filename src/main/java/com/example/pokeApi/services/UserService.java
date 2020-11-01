package com.example.pokeApi.services;



import com.example.pokeApi.entities.User;
import com.example.pokeApi.repositories.PokemonRepository;
import com.example.pokeApi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class UserService {
    private final PokemonRepository bookRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> findAll(String user) {
        var users = userRepository.findAll();

        if (user != null) {
            users = users.stream()
                    .filter(b -> b.getUsername()
                            .toLowerCase()
                            .contains(user.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return users;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @PutMapping
    public User save(User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);

        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }






    public void update(String id, User user){
        var isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().equals("ROLE_ADMIN"));
        var isCurrentUser = SecurityContextHolder.getContext().getAuthentication()
                .getName().toLowerCase().equals(user.getUsername().toLowerCase());
        if (!isAdmin && !isCurrentUser){
            log.warn("Attempt to update another user made.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can only update yourself");
        }
        if (!userRepository.existsById(id)){
            log.error("Couldn't find the user you were looking for.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("Could not find user with id %s",id));
        }
        if (user.getPassword().length() <= 16){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setUserId(id);
        userRepository.save(user);
    }

}
