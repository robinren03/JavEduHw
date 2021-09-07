package com.example.renyanyu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

class SelfWbAuthListener implements WbAuthListener
{
    public void onSuccess(Oauth2AccessToken token)
    {
        System.out.println(token);

    }

    public void cancel()
    {
        System.out.println("cancel");
    }

    public void onFailure(WbConnectErrorMessage errorMessage)
    {
        System.out.println(errorMessage);
    }
}

public class GetWeiboAuthorization extends AppCompatActivity {

    private static final String APP_KEY = "771540176";
    private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    private static final String SCOPE = "";

    SsoHandler mSsoHandler = new SsoHandler(GetWeiboAuthorization.this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_weibo_authorization);

        WbSdk.install(this,new AuthInfo(this, APP_KEY, REDIRECT_URL, SCOPE));


        Button shareButton=(Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSsoHandler.authorize(new SelfWbAuthListener());
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}