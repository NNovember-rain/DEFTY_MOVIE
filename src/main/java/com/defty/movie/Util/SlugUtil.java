package com.defty.movie.Util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Random;
import java.util.UUID;

public class SlugUtil {
    public static String createSlug(String name,Integer id) {
        // Chuyển đổi về chữ thường
        String slug = name.toLowerCase();

        // Loại bỏ dấu tiếng Việt hoặc ký tự đặc biệt
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Thay thế các ký tự không phải chữ và số, khoảng trắng, dấu gạch ngang thành dấu gạch ngang
        slug = slug.replaceAll("[^a-z0-9\\s-]", "").replaceAll("[\\s]+", "-");

        // Loại bỏ dấu gạch ngang ở đầu và cuối
        slug = slug.replaceAll("^-+|-+$", "")+"-"+id + generateRandomString(5);

        return slug;
    }

    private static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }

        return randomString.toString();
    }

    public static void main(String[] args) {
        System.out.println(createSlug("NGuyễn vết văn",4));
    }

}
