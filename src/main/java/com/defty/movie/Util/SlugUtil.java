package com.defty.movie.Util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
public class SlugUtil {
    public static String createSlug(String name) {
        // Chuyển đổi về chữ thường
        String slug = name.toLowerCase();

        // Loại bỏ dấu tiếng Việt hoặc ký tự đặc biệt
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Thay thế các ký tự không phải chữ và số, khoảng trắng, dấu gạch ngang thành dấu gạch ngang
        slug = slug.replaceAll("[^a-z0-9\\s-]", "").replaceAll("[\\s]+", "-");

        // Loại bỏ dấu gạch ngang ở đầu và cuối
        slug = slug.replaceAll("^-+|-+$", "");

        return slug;
    }

}
