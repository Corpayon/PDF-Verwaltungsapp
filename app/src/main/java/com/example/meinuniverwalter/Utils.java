package com.example.meinuniverwalter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = new FileInputStream(source);
        OutputStream os = new FileOutputStream(dest);
        copyFileUsingStream(is, os);
    }

    public static void copyFileUsingStream(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public static final String LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_CHARS = LOWER_CHARS.toUpperCase();
    public static final String NUMBER_CHARS = "1234567890";

    public static final String ALPHA_NUM = LOWER_CHARS + UPPER_CHARS + NUMBER_CHARS;

    public static final String SPECIAL_CHARS = "!#+-.,";


    public static String getRandomString(int length) {
        return getRandomString(ThreadLocalRandom.current(), length, ALPHA_NUM);
    }

    public static String getRandomString(Random random, int length, String alphabet) {

        char[] array = new char[length];

        int alphabetLength = alphabet.length();
        for (int i = 0; i < length; i++) {
            array[i] = alphabet.charAt(random.nextInt(alphabetLength));
        }
        return new String(array);
    }

    public static String getSecureRandomString(int length, String alphabet) {
        if (length < 12)
        {
            throw new IllegalArgumentException("Secure Random String length is too short!");
        }
        return getRandomString(new SecureRandom(), length, alphabet);
    }

    public static String getSecureRandomString(int length) {
        return getSecureRandomString(length, ALPHA_NUM + "!#.");
    }
}
