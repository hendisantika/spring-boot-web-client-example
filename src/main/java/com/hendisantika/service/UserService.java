package com.hendisantika.service;

import com.hendisantika.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

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

    public User getUserWithRetry(final String id) {
        return webClient
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .bodyToMono(User.class)
                .retryWhen(Retry.fixedDelay(MAX_RETRY_ATTEMPTS, Duration.ofMillis(DELAY_MILLIS)))
                .block();
    }

    public User getUserWithFallback(final String id) {
        return webClient
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .bodyToMono(User.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .onErrorResume(error -> Mono.just(new User()))
                .block();
    }

    public User getUserWithErrorHandling(final String id) {
        return webClient
                .get()
                .uri(BROKEN_URL_TEMPLATE, id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(User.class)
                .block();
    }
}
