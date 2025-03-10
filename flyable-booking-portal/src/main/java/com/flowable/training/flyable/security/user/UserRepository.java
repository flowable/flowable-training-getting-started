package com.flowable.training.flyable.security.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final Map<String, FlyableUserDetails> users = new HashMap<>();

    public UserRepository(PasswordEncoder passwordEncoder) {

        UserBootstrapper userBootstrapper = new UserBootstrapper(passwordEncoder);
        this.users.putAll(userBootstrapper.createUsers());
    }

    public Optional<FlyableUserDetails> getUser(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public Collection<FlyableUserDetails> getUsers() {
        return users.values().stream().toList();
    }

    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    public void addUser(FlyableUserDetails user) {
        users.put(user.getUserId(), user);
    }
}
