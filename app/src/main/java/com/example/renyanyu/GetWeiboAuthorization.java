package com.example.renyanyu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;


public class GetWeiboAuthorization extends AppCompatActivity
{

    private static final String APP_KY = "771540176";//"2045436852";//;
    private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";//"http://www.sina.com";////"http://www.weibo.com";//;
    private static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    IWBAPI mWBAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSdk();

    }
    //init sdk
    private void initSdk() {
        AuthInfo authInfo = new AuthInfo(this, APP_KY, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(this);
        mWBAPI.registerApp(this, authInfo);

        startClientAuth();
    }

    /**
     * 授权操作
     */
    private void startAuth() {
        //auth
        mWBAPI.authorize(new WbAuthListener() {
            @Override
            public void onComplete(Oauth2AccessToken token) {
                Toast.makeText(GetWeiboAuthorization.this, "微博授权成功",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(UiError error) {
                System.out.println(error);
                Toast.makeText(GetWeiboAuthorization.this, "微博授权出错",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                Toast.makeText(GetWeiboAuthorization.this, "微博授权取消",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 指定通过客户端授权操作
     */
    private void startClientAuth() {
        mWBAPI.authorizeClient(new WbAuthListener() {
            @Override
            public void onComplete(Oauth2AccessToken token) {
                Toast.makeText(GetWeiboAuthorization.this, "微博授权成功",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(UiError error) {
                System.out.println(error);
                Toast.makeText(GetWeiboAuthorization.this, "微博授权出错",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                Toast.makeText(GetWeiboAuthorization.this, "微博授权取消",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 指定通过⽹⻚（H5）授权操作
     */
    private void startWebAuth() {
        mWBAPI.authorizeWeb(new WbAuthListener() {
            @Override
            public void onComplete(Oauth2AccessToken token) {
                Toast.makeText(GetWeiboAuthorization.this, "微博授权成功",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(UiError error) {
                System.out.println(error);
                Toast.makeText(GetWeiboAuthorization.this, "微博授权出错:" +
                        error.errorDetail, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                Toast.makeText(GetWeiboAuthorization.this, "微博授权取消",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mWBAPI != null) {
            mWBAPI.authorizeCallback(requestCode, resultCode, data);
        }
    }



//    private static final String APP_KEY = "771540176";
//    private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
//    private static final String SCOPE = "";
//
//    SsoHandler mSsoHandler = new SsoHandler(GetWeiboAuthorization.this);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_get_weibo_authorization);
//
//        WbSdk.install(this,new AuthInfo(this, APP_KEY, REDIRECT_URL, SCOPE));
//
//
//        Button shareButton=(Button) findViewById(R.id.shareButton);
//        shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSsoHandler.authorize(new SelfWbAuthListener());
//            }
//        });
//
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (mSsoHandler != null) {
//            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
//    }
}