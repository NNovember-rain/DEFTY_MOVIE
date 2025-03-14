package com.defty.movie.security;

import com.defty.movie.entity.Account;
import com.defty.movie.entity.User;
import com.defty.movie.repository.IAccountRepository;
import com.defty.movie.repository.IUserRepository;
import com.defty.movie.utils.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {
        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String token = CookieUtil.getValue(request, "access_token");
            if (token == null || token.isEmpty()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: No access token");
                return;
            }
            final String username = jwtTokenUtil.extractUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Optional<Account> account = accountRepository.findByUsername(username);
                if (account.isPresent()) {
                    Account accountDetails = (Account) userDetailsService.loadUserByUsername(username);
                    if (jwtTokenUtil.validateToken(token, accountDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        accountDetails,
                                        null,
                                        accountDetails.getAuthorities()
                                );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } else {
                    Optional<User> user = userRepository.findByUsername(username);
                    if (user.isPresent()) {
                        User userDetails = user.get();
                        if (jwtTokenUtil.validateToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            null
                                    );
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid token");
        }
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/admin/auth/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/admin/auth/refresh-token", apiPrefix), "POST"),
                Pair.of(String.format("%s/admin/auth/check-account", apiPrefix), "GET"),
                Pair.of(String.format("%s/user/auth/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/user/auth/refresh-token", apiPrefix), "POST"),
                Pair.of(String.format("%s/user/auth/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/user/auth/logout", apiPrefix), "POST"),
                Pair.of(String.format("%s/user/auth/check-account", apiPrefix), "GET"),
                Pair.of(String.format("%s/user/accessible/.*", apiPrefix), "GET"),

                // Swagger
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/v3/api-docs/.*", "GET"),
                Pair.of("/swagger-resources", "GET"),
                Pair.of("/swagger-resources/.*", "GET"),
                Pair.of("/configuration/ui", "GET"),
                Pair.of("/configuration/security", "GET"),
                Pair.of("/swagger-ui/.*", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/swagger-ui/index.html", "GET"),

                Pair.of("/movie.*", "*")
        );
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        for (Pair<String, String> bypassToken : bypassTokens) {
            String pattern = bypassToken.getFirst();
            String method = bypassToken.getSecond();

            if (Pattern.matches(pattern, requestPath) && (method.equals("*") || requestMethod.equalsIgnoreCase(method))) {
                return true;
            }
        }
        return false;
    }
}
