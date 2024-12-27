package com.defty.movie.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieUtil {
    public static void create(HttpServletResponse response, String name, String value, boolean httpOnly, boolean secure, int maxAge, String path) {
        log.info("COOKIE | Creating cookie: name={}, value={}, httpOnly={}, secure={}, maxAge={}, path={}", name, value, httpOnly, secure, maxAge, path);
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        response.addCookie(cookie);
        log.info("COOKIE | Cookie created: {}", cookie);
    }

    public static void clear(HttpServletResponse response, String name) {
        log.info("COOKIE | Clearing cookie: name={}", name);
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        log.info("COOKIE | Cookie cleared: {}", cookie);
    }

    public static String getValue(HttpServletRequest request, String name) {
        log.info("COOKIE | Getting value of cookie: name={}", name);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    log.info("COOKIE | Cookie found: name={}, value={}", name, cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
