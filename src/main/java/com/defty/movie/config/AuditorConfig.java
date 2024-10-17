package com.defty.movie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditorConfig {
    @Bean
    public AuditorAware<String> auditorProvider() { // auditorProvider sẽ trả về tên người dùng hiện tại, và Spring Data JPA sẽ tự động điền vào các trường createdBy và modifiedBy
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
                return Optional.of(auth.getName());
            }
            return Optional.empty();
        };
    }

}
