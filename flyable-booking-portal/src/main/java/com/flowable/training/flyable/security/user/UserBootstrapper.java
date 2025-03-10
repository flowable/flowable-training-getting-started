package com.flowable.training.flyable.security.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.datafaker.Faker;

public class UserBootstrapper {

    private final PasswordEncoder passwordEncoder;

    @Value("${flyable.faker-seed:12345}")
    private int seed;

    public UserBootstrapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, FlyableUserDetails> createUsers() {
        Map<String, FlyableUserDetails> users = new HashMap<>();
        Faker faker = new Faker(new Random(seed));

        // Create users
        for (int i = 1; i <= 15; i++) {
            String username = "user_" + i;
            String password = passwordEncoder.encode("training");

            FlyableUserDetails user = new FlyableUserDetails();
            user.setUserId(username);
            user.setPassword(password);
            user.setRole("USER");
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setAvatar("https://i.pravatar.cc/150?img=" + i);
            users.put(username, user);
        }

        // Create an admin user
        String adminUserName = "admin";
        String password = passwordEncoder.encode("test");
        FlyableUserDetails admin = new FlyableUserDetails();
        admin.setUserId(adminUserName);
        admin.setPassword(password);
        admin.setRole("ADMIN");
        admin.setFirstName("Admin");
        admin.setLastName("McAdminson");
        users.put(adminUserName, admin);

        return users;
    }

}
