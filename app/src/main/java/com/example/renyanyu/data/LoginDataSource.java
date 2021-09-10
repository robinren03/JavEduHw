package com.example.renyanyu.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.renyanyu.AppDB;
import com.example.renyanyu.KEntity;
import com.example.renyanyu.KEntityRepository;
import com.example.renyanyu.R;
import com.example.renyanyu.data.model.LoggedInUser;
import com.example.renyanyu.ServerHttpResponse;


import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                RecoverHistoryTask recoverHistoryTask = new RecoverHistoryTask(context);
                recoverHistoryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
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

    private class RecoverHistoryTask extends AsyncTask<String, Void, Void> {
        Context c;
        public RecoverHistoryTask(Context c){
            super();
            this.c = c;
        }

        @Override
        protected Void doInBackground(@NonNull String... strings){
            String res = strings[0];
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(res);
            String username = jsonObject.getString("username");
            String filename = username+"his_ent";
            KEntityRepository kdb = new KEntityRepository(AppDB.getAppDB(c, username));
            SharedPreferences.Editor note = c.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
            com.alibaba.fastjson.JSONArray jsonArray =  jsonObject.getJSONArray("history");
            for(int i=0; i < jsonArray.size(); i++)
            {
                com.alibaba.fastjson.JSONObject his = jsonArray.getJSONObject(i);
                note.putString(his.getString("uri"),"1");
                String kuri = his.getString("uri");
                String entity_name = his.getString("name");
                String course = his.getString("course");
                String ur= c.getString(R.string.backend_ip) + "/request/card";
                String ms="course="+ course+"&uri="+kuri;
                String card= serverHttpResponse.postResponse(ur,ms);
                String url = c.getString(R.string.backend_ip) + "/request/instance";
                String msg="?course="+course+"&name="+entity_name;
                String result= serverHttpResponse.getResponse(url+msg);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date createdate = null;
                try {
                    createdate = simpleDateFormat.parse(his.getString("time"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                KEntity tem=new KEntity(kuri,entity_name,card,result,createdate);
                kdb.insertkEntity(tem);
            }
            note.commit();
            return null;
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