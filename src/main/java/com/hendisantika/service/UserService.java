package com.hendisantika.service;

import com.hendisantika.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-web-client-example
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 19/12/21
 * Time: 18.40
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    public static final int DELAY_MILLIS = 100;
    public static final int MAX_RETRY_ATTEMPTS = 3;
    private static final String USERS_URL_TEMPLATE = "/users/{id}";
    private static final String BROKEN_URL_TEMPLATE = "/broken-url/{id}";
    private final WebClient webClient;

    public Mono<User> getUserByIdAsync(final String id) {
        return webClient
                .get()
                .uri(USERS_URL_TEMPLATE, id)
                .retrieve()
                .bodyToMono(User.class);
    }

    public User getUserByIdSync(final String id) {
        return webClient
                .get()
                .uri(USERS_URL_TEMPLATE, id)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }
}
