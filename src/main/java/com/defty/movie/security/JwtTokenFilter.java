package com.defty.movie.security;


import com.defty.movie.entity.Account;
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
import org.springframework.web.filter.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{
    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException {
        try {
            if(isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String username = jwtTokenUtil.extractUsername(token);
            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                Account accountDetails = (Account) userDetailsService.loadUserByUsername(username);
                if(jwtTokenUtil.validateToken(token, accountDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    accountDetails,
                                    null,
                                    accountDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/admin/account/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/admin/account/refresh-token", apiPrefix), "POST"),
                Pair.of(String.format("%s/admin/account/check-account", apiPrefix), "GET"),

                // Swagger
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/v3/api-docs/.*", "GET"),
                Pair.of("/swagger-resources", "GET"),
                Pair.of("/swagger-resources/.*", "GET"),
                Pair.of("/configuration/ui", "GET"),
                Pair.of("/configuration/security", "GET"),
                Pair.of("/swagger-ui/.*", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/swagger-ui/index.html", "GET")

        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        for (Pair<String, String> bypassToken : bypassTokens) {
            String pattern = bypassToken.getFirst();
            if (Pattern.matches(pattern, requestPath) && requestMethod.equalsIgnoreCase(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
