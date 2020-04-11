package com.mefollow.webschool.management.user.domain.account;

import java.util.Arrays;
import java.util.Locale;

public enum Language {
    ENGLISH("en"),
    RUSSIAN("ru");

    private final String abbreviation;

    Language(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static Language from(String language) {
        if (Arrays.stream(Language.values()).noneMatch(lang -> lang.name().equals(language))) return null;
        return Language.valueOf(language);
    }

    public Locale locale() {
        return Locale.forLanguageTag(abbreviation);
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
