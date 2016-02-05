package com.kiselev.faces.validators;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Validator {

    public static String fieldsAreNotBlank(Locale locale, String... fields) {
        for (String field : fields) {
            if (field.trim().equals("")) {
                return getMessage(locale, "error_blank");
            }
        }
        return null;
    }

    public static String validationUsername(Locale locale, String username) {
        username = username.trim();
        if (username.length() < 5) {
            return getMessage(locale, "error_username_min");
        }

        if (username.length() > 33) {
            return getMessage(locale, "error_username_max");
        }

        if (countOfCharacters(username, '.') > 1 ||
                !username.matches("^[a-zA-Z0-9][a-zA-Z0-9\\.]+[a-zA-Z0-9]$")) {
            return getMessage(locale, "error_incorrect_username");
        }
        return null;
    }

    public static String validationUrlName(Locale locale, String urlname) {
        urlname = urlname.trim();
        if (urlname.length() < 3) {
            return getMessage(locale, "error_urlname_min");
        }

        if (urlname.length() > 15) {
            return getMessage(locale, "error_urlname_max");
        }

        if (countOfCharacters(urlname, '_') > 1 ||
                !urlname.matches("^[a-zA-Z0-9][a-zA-Z0-9_]+[a-zA-Z0-9]$")) {
            return getMessage(locale, "error_incorrect_urlname");
        }
        return null;
    }

    public static String validationSignInId(Locale locale, Long id) {
        if (id == null) {
            return getMessage(locale, "error_incorrect_data");
        }
        return null;
    }

    public static String validationSignUpId(Locale locale, Long id) {
        if (id == null) {
            return getMessage(locale, "error_username_taken");
        }
        return null;
    }

    public static String validationPassword(Locale locale, String password) {
        if (password.trim().length() < 6) {
            return getMessage(locale, "error_password_min");
        }
        return null;
    }

    public static String passwordsAreEquals(Locale locale,
                                            String password,
                                            String secondPassword) {
        if (!password.trim().equals(secondPassword.trim())) {
            return getMessage(locale, "error_password_match");
        }
        return null;
    }

    public static String validationFullName(Locale locale,
                                            String firstName,
                                            String lastName) {
        if (firstName == null || firstName.equals("") ||
                lastName == null || lastName.equals("")){
            return getMessage(locale, "error_blank_name");
        }

        if (firstName.length() < 2 || lastName.length() < 2 ||
                firstName.length() > 35 || lastName.length() > 35) {
            return getMessage(locale, "error_length_name");
        }

        return null;
    }

    public static String validationPhoto(Locale locale, String photo) {
        if (!photo.matches("^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;" +
                "]*[-a-zA-Z0-9+&@#/%=~_|]")) {
            return getMessage(locale, "error_url");
        }

        BufferedImage image;

        try {
            URL url = new URL(photo);
            image = ImageIO.read(url);
        } catch (IOException error) {
            return getMessage(locale, "error_url");
        }

        if (image == null) {
            return getMessage(locale, "error_url");
        }

        if (image.getWidth() > 200 || image.getHeight() > 300) {
            return getMessage(locale, "error_size");
        }
        return null;
    }

    public static String unhandledError(Locale locale) {
        return getMessage(locale, "error_unhandled");
    }

    private static int countOfCharacters(String username, char
            symbol) {
        int count = 0;
        for (int index = 0; index < username.length(); index++)
            if (username.charAt(index) == symbol) {
                count++;
            }
        return count;
    }

    public static String getMessage(Locale locale, String key) {
        return ResourceBundle.getBundle("internationalization.messages", locale)
                .getString(key);
    }
}
