package com.kiselev.faces.validators;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Validator {

    public static String fieldsAreNotBlank(String... fields) {
        for (String field : fields) {
            if (field.equals("")) {
                return "This fields cannot be blank";
            }
        }
        return null;
    }

    public static String validationUsername(String username) {
        if (username.length() < 5) {
            return "Username must contains at least 5 characters";
        }

        if (username.length() > 33) {
            return "Username must not be longer than 33 characters";
        }

        if (countOfCharacters(username, '.') > 1 ||
                !username.matches("^[a-zA-Z0-9][a-zA-Z0-9\\.]+[a-zA-Z0-9]$")) {
            return "Username can contains letters, digits and one dot";
        }
        return null;
    }

    public static String validationSignInId(Long id) {
        if (id == null) {
            return "Incorrect username or password";
        }
        return null;
    }

    public static String validationSignUpId(Long id) {
        if (id == null) {
            return "This username is already taken";
        }
        return null;
    }

    public static String validationPassword(String password) {
        if (password.length() < 6) {
            return "Password must contains at least 6 characters";
        }
        return null;
    }

    public static String passwordsAreEquals(
            String password, String secondPassword) {
        if (!password.equals(secondPassword)) {
            return "Passwords do not match";
        }
        return null;
    }

    public static String validationFullName(String firstName, String lastName) {
        if (firstName.length() < 2 || lastName.length() < 2 ||
                firstName.length() > 35 || lastName.length() > 35) {
            return "Use your full and real name";
        }

        if (!firstName.matches("^[\\u00c0-\\u01ffA-ZА-Я]" +
                "[\\u00c0-\\u01ffa-zA-Zа-яА-Я'\\-]+$") ||
                !lastName.matches("^[\\u00c0-\\u01ffA-ZА-Я]" +
                        "[\\u00c0-\\u01ffa-zA-Zа-яА-Я'\\-]+$")) {
            return "Use your real name";
        }
        return null;
    }

    public static String validationPhoto(String photo) {
        if (!photo.matches("^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;" +
                "]*[-a-zA-Z0-9+&@#/%=~_|]")) {
            return "Invalid url";
        }

        BufferedImage image;

        try {
            URL url = new URL(photo);
            image = ImageIO.read(url);
        } catch (IOException error) {
            return "Invalid url";
        }

        if (image == null) {
            return "Invalid url";
        }

        if (image.getWidth() > 200 || image.getHeight() > 300) {
            return "It must be less than 200x300";
        }
        return null;
    }

    public static String unhandledError() {
        return "Unhandled error";
    }

    private static int countOfCharacters(String username, char symbol) {
        int count = 0;
        for (int index = 0; index < username.length(); index++)
            if (username.charAt(index) == symbol) {
                count++;
            }
        return count;
    }
}
