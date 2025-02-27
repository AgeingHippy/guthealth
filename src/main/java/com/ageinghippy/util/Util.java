package com.ageinghippy.util;

public class Util {

    public static <T> T valueIfNull(T value, T valueIfNull) {
        return value != null ? value : valueIfNull;
    }
}
