package org.swt.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Language {
    public static String getTranslatedText(String key) {
        Locale currentLocale = Locale.getDefault(); // Get the user's preferred locale

        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n.messages", currentLocale);

        try {
            return resourceBundle.getString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
