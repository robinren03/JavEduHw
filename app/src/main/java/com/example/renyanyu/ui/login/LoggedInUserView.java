package com.example.renyanyu.ui.login;

/**
 * Class exposing authenticated user details
 */
class LoggedInUserView {
    private String displayName;

    private String token;

    LoggedInUserView(String displayName, String token)
    {
        this.displayName = displayName;
        this.token = token;
    }

    String getDisplayName() {
        return displayName;
    }

    String getToken() {
        return token;
    }
}