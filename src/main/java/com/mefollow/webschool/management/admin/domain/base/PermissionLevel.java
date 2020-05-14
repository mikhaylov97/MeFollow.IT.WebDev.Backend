package com.mefollow.webschool.management.admin.domain.base;

public enum PermissionLevel {
    FIRST_LEVEL(0),
    SECOND_LEVEL(1);

    private int level;

    PermissionLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isUpperOrEquals(PermissionLevel level) {
        return this.level <= level.level;
    }
}
