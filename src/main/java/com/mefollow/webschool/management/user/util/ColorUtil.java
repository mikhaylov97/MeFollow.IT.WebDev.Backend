package com.mefollow.webschool.management.user.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

public class ColorUtil {

    private static String[] colors = {"#D32F2F", "#689F38", "#3444AC", "#F57C00", "#008172", "#902699", "#FBC02D", "#0288D1"};

    public static String randomColor() {
        return colors[new Random().nextInt(colors.length)];
    }

    public static String randomColorExclude(Collection<String> excludeColors) {
        List<String> allColors = new LinkedList<>(asList(colors));
        allColors.removeIf(color -> excludeColors.stream().anyMatch(color::equals));

        if (allColors.size() == 0) return randomColor();
        return allColors.toArray(new String[]{})[new Random().nextInt(allColors.size())];
    }
}
