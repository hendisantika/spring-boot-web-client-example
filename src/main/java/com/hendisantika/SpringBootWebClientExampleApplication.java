package com.hendisantika;

import com.hendisantika.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@AllArgsConstructor
public class SpringBootWebClientExampleApplication implements CommandLineRunner {
    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebClientExampleApplication.class, args);
    }

    @Override
    public void run(final String... args) {
        userService
                .getUserByIdAsync("1")
                .subscribe(user -> log.info("Get user async: {}", user));
    }
}
