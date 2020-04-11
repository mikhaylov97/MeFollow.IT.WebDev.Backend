package com.mefollow.webschool.sandbox.domain;

public enum SandboxResourceType {
    JS("js"),
    CSS("css"),
    HTML("html");

    private final String extension;

    SandboxResourceType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static SandboxResourceType getSandboxResourceType(final String extension) {
        if (isJavaScript(extension)) return JS;
        if (isCssStyle(extension)) return CSS;
        if (isHtmlTemplate(extension)) return HTML;

        return null;
    }

    public static boolean isJavaScript(final String extension) {
        return JS.extension.equalsIgnoreCase(extension);
    }

    public static boolean isCssStyle(final String extension) {
        return CSS.extension.equalsIgnoreCase(extension);
    }

    public static boolean isHtmlTemplate(final String extension) {
        return HTML.extension.equalsIgnoreCase(extension);
    }
}
