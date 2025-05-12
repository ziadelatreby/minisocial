package com.minisocial.minisocialapi.common;

import com.minisocial.minisocialapi.enums.USER_ROLE;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
public class utils {

    public static boolean isNull(Object p) {
        return p == null;
    }

    public static boolean isNull(String p) {
        return Objects.equals(p, "") || p == null;
    }

    public static String getEnumVal(Object e) {
        System.out.println("e: " + e);
        return e.toString().toLowerCase(Locale.ROOT);
    }

    public static boolean isValidRole(String role) {
        return Arrays.stream(USER_ROLE.values()).anyMatch(r -> r.name().toLowerCase(Locale.ROOT).equals(role));
    }
}
