package com.session16_tmdt.config;
import com.session16_tmdt.model.User;
import com.session16_tmdt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class DataSeeder implements CommandLineRunner {
    private final UserService userService;
    public DataSeeder(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void run(String... args) {
        if (userService.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@gmail.com");
            admin.setFullName("Admin");
            admin.setRole("ADMIN");
            userService.save(admin);

        }
    }
}