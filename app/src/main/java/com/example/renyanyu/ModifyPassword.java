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
import com.example.renyanyu.data.Result;
import com.example.renyanyu.data.model.LoggedInUser;

import org.json.JSONObject;

import java.io.IOException;

public class ModifyPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modify_password);

        Button confirmButton =(Button) findViewById(R.id.modify_password_page_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText oldPasswordEditText = findViewById(R.id.modify_password_page_input_old_password_edit_text);
                String oldPassword = oldPasswordEditText.getText().toString();
                EditText newPasswordEditText = findViewById(R.id.modify_password_page_input_new_password_edit_text);
                String newPassword = newPasswordEditText.getText().toString();
                EditText confirmNewPasswordEditText = findViewById(R.id.modify_password_page_confirm_new_password_edit_text);
                String confirmNewPassword = confirmNewPasswordEditText.getText().toString();
                if(oldPassword.length()<6||oldPassword.length()>15)
                {
                    oldPasswordEditText.setError("旧密码的长度应为6至15位");
                    Toast.makeText(ModifyPassword.this, "旧密码的长度应为6至15位", Toast.LENGTH_SHORT).show();
                }
                else if(newPassword.length()<6||newPassword.length()>15)
                {
                    newPasswordEditText.setError("新密码的长度应为6至15位");
                    Toast.makeText(ModifyPassword.this, "新密码的长度应为6至15位", Toast.LENGTH_SHORT).show();
                }
                else if(!newPassword.equals(confirmNewPassword))
                {
                    confirmNewPasswordEditText.setError("请确认两次输入的新密码内容一致");
                    Toast.makeText(ModifyPassword.this, "请确认两次输入的新密码内容一致", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences userInfo= ModifyPassword.this.getSharedPreferences("user", 0);
                    String userToken = userInfo.getString("token","");
                    String md5OldPassword = MD5Util.string2MD5(oldPassword);
                    String md5NewPassword = MD5Util.string2MD5(newPassword);
                    String url = ModifyPassword.this.getString(R.string.backend_ip) + "/user/update";
                    String msg = "mode=modifyPassword"+ "&password=" + md5NewPassword+"&old_password="+md5OldPassword
                            +"&token="+userToken;
                    ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                    String responseString = serverHttpResponse.postResponse(url, msg);
                    if(responseString==null)
                    {
                        Toast.makeText(ModifyPassword.this, "好像断网了？请检查网络连接后重试", Toast.LENGTH_SHORT).show();
                        System.out.println("修改密码页面：没有网络连接");
                    }
                    else if(responseString.equals("invalid token"))
                    {
                        //TODO:弹出“登录过期或已在别的设备上登录”的对话框
                        System.out.println("修改密码页面：登录过期或已在别的设备上登录");
                    }
                    else if(responseString.equals("wrong password"))
                    {
                        oldPasswordEditText.setError("登录密码错误");
                        Toast.makeText(ModifyPassword.this, "登录密码错误", Toast.LENGTH_SHORT).show();
                    }
                    else if(responseString.equals("successfully modified password"))
                    {
                        Toast.makeText(ModifyPassword.this, "密码修改成功^_^", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button backButton =(Button) findViewById(R.id.modify_password_page_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}