package com.defty.movie.utils;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Random;

@Component
public class SlugUtil {


    // Create Slug
    public String createSlug(String name,Integer id) {
        //Chuyển chuỗi thành dạng không dấu
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        String withoutAccent = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String slug = withoutAccent
                .toLowerCase() // Chuyển tất cả các chữ cái thành chữ thường
                .trim() // Xóa các khoảng trắng ở đầu và cuối
                .replaceAll("[\\s\\W-]+", "-") // Thay thế khoảng trắng, ký tự đặc biệt thành dấu gạch ngang
                .replaceAll("^-+|-+$", ""); // Xóa dấu gạch ngang ở đầu và cuối

        return slug+"-"+id + generateRandomString(5);

    }

    //Create random
    private String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }

        return randomString.toString();
    }

}
