package com.defty.movie.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieUtil {
    public static void create(HttpServletResponse response, String name, String value, boolean httpOnly, boolean secure, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        response.addCookie(cookie);
        log.info("COOKIE | Creating cookie: name={}, value={}, httpOnly={}, secure={}, maxAge={}, path={}", name, value, httpOnly, secure, maxAge, path);
    }

    public static void clear(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        log.info("COOKIE | Cookie cleared: {}, name: {}", cookie, name);
    }

    public static String getValue(HttpServletRequest request, String name) {
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
