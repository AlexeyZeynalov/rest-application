package com.service.impl;

import com.model.User;
import com.repositories.UserRepository;
import com.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException(String.format("User with id '%d' not found", id));
        }
    }

    @Override
    public User updateUser(long id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with id '%d' not found", id))
        );

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());

        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public void deleteUser(long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UsernameNotFoundException(String.format("User with id '%d' not found", id));
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}

