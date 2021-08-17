package com.example.javeduhw.ui.login.data;

import android.util.Log;

import com.example.javeduhw.ui.login.data.model.LoggedInUser;

import java.io.IOException;

import com.example.javeduhw.ui.login.MD5Util;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            String mdPassword = MD5Util.string2MD5(password);
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            System.out.println("Got Exception");
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<LoggedInUser> register(String username, String password) {

        try {
            String mdPassword = MD5Util.string2MD5(password);
            // TODO: handle loggedInUser authentication
            return new Result.Success<>(1);
        } catch (Exception e) {
            System.out.println("Got Exception");
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}