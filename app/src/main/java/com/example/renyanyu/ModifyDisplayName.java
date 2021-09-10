package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.renyanyu.data.MD5Util;

public class ModifyDisplayName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modify_display_name);

        Button confirmButton =(Button) findViewById(R.id.modify_display_name_page_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newDisplayNameEditText = findViewById(R.id.modify_display_name_page_input_new_display_name_edit_text);
                String newDisplayName = newDisplayNameEditText.getText().toString();
                if(newDisplayName.length()<1||newDisplayName.length()>20)
                {
                    newDisplayNameEditText.setError("用户名的长度应为1至20位");
                    Toast.makeText(ModifyDisplayName.this, "用户名的长度应为1至20位", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences userInfo= ModifyDisplayName.this.getSharedPreferences("user", 0);
                    String userToken = userInfo.getString("token","");

                    String url = ModifyDisplayName.this.getString(R.string.backend_ip) + "/user/update";
                    String msg = "displayName="+newDisplayName +"&token="+userToken + "&mode=modifyDisplayName";
                    ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                    String responseString = serverHttpResponse.postResponse(url, msg);
                    if(responseString==null)
                    {
                        Toast.makeText(ModifyDisplayName.this, "好像断网了？请检查网络连接后重试", Toast.LENGTH_SHORT).show();
                        System.out.println("修改用户名页面：没有网络连接");
                    }
                    else if(responseString.equals("invalid token"))
                    {
                        //TODO:弹出“登录过期或已在别的设备上登录”的对话框
                        System.out.println("修改用户名页面：登录过期或已在别的设备上登录");
                    }
                    else if(responseString.equals("successfully modified display name"))
                    {
                        Toast.makeText(ModifyDisplayName.this, "用户名修改成功^_^", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = userInfo.edit();
                        editor.putString("displayName", newDisplayName);
                        editor.apply();
                    }
                }
            }
        });

        Button backButton =(Button) findViewById(R.id.modify_display_name_page_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}