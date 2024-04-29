package com.dance.mo.Config;

import com.dance.mo.Entities.Role;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class Initialize implements CommandLineRunner {

    private static final int MAX_FAKE_USERS = 50; // Limit to 50 fake users

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if not already present
        createAdminUserIfNeeded();

        // Generate fake users
        generateFakeUsers();
    }

    private void createAdminUserIfNeeded() {
        User adminUser = userRepository.getUserByEmail("admin@admin.com");
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setFirstName("admin");
            adminUser.setLastName("admin");
            adminUser.setEmail("admin@admin.com");
            adminUser.setBan(0);
            adminUser.setPhoneNumber(56467563);
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setRole(Role.ADMIN);
            adminUser.setEnabled(true);
            userRepository.save(adminUser);
        }
    }

    private void generateFakeUsers() {
        List<String> fakeEmails = generateFakeEmails(MAX_FAKE_USERS);

        for (String email : fakeEmails) {
            if (userRepository.getUserByEmail(email) == null) {
                User user = new User();
                user.setFirstName("User");
                user.setLastName(email.split("@")[0]); // Use part of email as last name
                user.setEmail(email);
                user.setPhoneNumber(12345678); // Set a constant phone number
                user.setPassword(passwordEncoder.encode("ahmed")); // Set a constant password
                user.setBan(0);
                user.setRole(generateRandomRole()); // Set a random role
                user.setEnabled(true);
                userRepository.save(user);

                if (userRepository.count() >= MAX_FAKE_USERS) {
                    break;
                }
            }
        }
    }

    private List<String> generateFakeEmails(int numUsers) {
        List<String> fakeEmails = new ArrayList<>();
        for (int i = 1; i <= numUsers; i++) {
            fakeEmails.add("user" + i + "@example.com");
        }
        return fakeEmails;
    }

    private Role generateRandomRole() {
        Role[] roles = Role.values();
        return roles[new Random().nextInt(roles.length)]; // Get a random role from Role enum
    }
}
