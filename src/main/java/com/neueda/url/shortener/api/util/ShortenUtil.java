package com.neueda.url.shortener.api.util;

public class ShortenUtil {

    public static final String ALPHABET = "Mheo9PI2qNs5Zpf80TBn7lmRbtQ4YKXHvwAEWxuzdra316OJigGLSVUCyFjkDc";
    public static final int BASE = ALPHABET.length();

    public static Long strToId(String str) {
        Long num = 0L;
        for (int i = 0; i < str.length(); i++) {
            num *= BASE + ALPHABET.indexOf(str.charAt(i));
        }
        return num;
    }

    public static String idToStr(Long num) {
        StringBuilder str = new StringBuilder();
        while (num > 0) {
            str.insert(0, ALPHABET.charAt((int) (num % BASE)));
            num /= BASE;
        }
        return str.toString();
    }

}
