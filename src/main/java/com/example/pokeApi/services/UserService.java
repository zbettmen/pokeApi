package com.inl2.Biblans.services;


import com.inl2.Biblans.entities.User;
import com.inl2.Biblans.reposotories.BookRepo;
import com.inl2.Biblans.reposotories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final BookRepo bookRepository;
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> findAll(String user){
        var users = userRepository.findAll();

        if(user!=null){
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
    public User save(User user){
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);

        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
