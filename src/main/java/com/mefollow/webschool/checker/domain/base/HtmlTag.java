package com.mefollow.webschool.checker.domain.base;

public enum HtmlTag {
    HEAD("head"),
    META("meta"),
    TITLE("title"),
    LINK("link"),
    BODY("body"),
    HEADER("header"),
    FOOTER("footer"),
    DIV("div"),
    BUTTON("button"),
    P("p"),
    NAV("nav");

    private String value;

    HtmlTag(String value) {
        this.value = value;
    }

    public static HtmlTag getByValue(String value) {
        for (HtmlTag tag : HtmlTag.values()) {
            if (tag.getValue().equalsIgnoreCase(value)) return tag;
        }

        return null;
    }

    public String getValue() {
        return value;
    }
}
