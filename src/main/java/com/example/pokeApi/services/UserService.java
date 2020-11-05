package com.example.pokeApi.services;
import com.example.pokeApi.entities.User;
import com.example.pokeApi.repositories.PokemonRepository;
import com.example.pokeApi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    @Cacheable(value = "userCache")
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


    @CachePut(value = "userCache", key = "#result.id")
    public User save(User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);

        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @CacheEvict(value = "userCache", key = "#id")
    public void delete(String id) {
        if(!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, // 404 -> Not found
                    String.format("Could not find the user by id %s.", id));
        }
        userRepository.deleteById(id);
    }



    @CachePut(value = "userCache", key = "#id")
    public void update(String id, User user) {
        var isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().equals("ROLE_ADMIN"));
        var isCurrentUser = SecurityContextHolder.getContext().getAuthentication()
                .getName().toLowerCase().equals(user.getUsername().toLowerCase());
        if (!isAdmin && !isCurrentUser) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can only update your own details. Admin can update all users.");
        }
        if (!userRepository.existsById(id)) {
            log.error(String.format("Could not find the user by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, // 404 -> Not found
                    String.format("Could not find the user by id %s.", id));
        }
        user.setUserId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        userRepository.save(user);

    }
}
