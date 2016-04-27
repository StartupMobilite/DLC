package com.app.dlc.dlc.model;

/**
 * Created by Guillaume on 04/04/2016.
 */
public class LoggedInUser {
    public static String id;
    public static String token;
    public static String type;
    public static String email;

    public static String getId() {
        return id;
    }

    public static String getToken() {
        return token;
    }

    public static String getType() {
        return type;
    }
    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        LoggedInUser.email = email;
    }
}
