package com.minisocial.minisocialapi.common;

import java.util.Objects;
public class utils {

    public static boolean isNull(Object p) {
        return p == null;
    }

    public static boolean isNull(String p) {
        return Objects.equals(p, "") || p == null;
    }
}
