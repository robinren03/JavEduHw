package com.example.renyanyu.data;

import android.content.Context;

import com.example.renyanyu.R;
import com.example.renyanyu.data.model.LoggedInUser;
import com.example.renyanyu.ServerHttpResponse;


import org.json.JSONObject;

import java.io.IOException;
// This file is brand new
/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private ServerHttpResponse serverHttpResponse;

    public LoginDataSource() {
        this.serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
    }

    public Result<LoggedInUser> login(Context context, String username, String password) {

        try {
            String md5Password = MD5Util.string2MD5(password);
            String url = context.getString(R.string.backend_ip) + "/user/login";
            String msg = "username=" + username + "&password=" + md5Password;

            String res = serverHttpResponse.postResponse(url, msg);

            if(res==null){
                throw new Exception();
            }

            System.out.println(res);
            JSONObject json = new JSONObject(res);

            if(json.get("error").toString().equals("0")) {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                json.getString("Token").toString(),
                                json.getString("displayName").toString());
                return new Result.Success<>(fakeUser);
            }
            else return new Result.Error(new IOException(json.getString("errormsg"), new IOException("Hello")));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public Result<LoggedInUser> register(Context context, String username, String displayname, String password) {
        try {
            String md5Password = MD5Util.string2MD5(password);
            String url = context.getString(R.string.backend_ip) + "/user/register";
            String msg = "username=" + username + "&password=" + md5Password
                    + "&displayname=" + displayname;

            String res = serverHttpResponse.postResponse(url, msg);

            if(res == null) {
                throw new Exception();
            }

            if(res.equals("success")) {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                "",
                                ""
                        );
                return new Result.Success<>(fakeUser);

            }
            return new Result.Error(new IOException("Used phone number", new IOException("Hello")));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error register", e));
        }
    }
}