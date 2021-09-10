package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class QuizException extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quiz_exception);

        TextView guideInfoTextView = findViewById(R.id.quiz_exception_page_guide_info_text_view);
        Intent intent=getIntent();
        String exceptionName=intent.getStringExtra("exceptionName");
        if(exceptionName.equals("Network Connection Exception"))
        {
            guideInfoTextView.setText("好像断网了，请检查您的网络设置");
        }
        else if(exceptionName.equals("Server Exception"))
        {
            guideInfoTextView.setText("抱歉，服务器好像出了点问题，请稍后重试");
        }
        else if(exceptionName.equals("No History Exception"))
        {
            guideInfoTextView.setText("学习了您关注的实体再来吧");
        }
        else
        {
            System.out.println("QuizException Page:unknown Exception");
        }

        TextView backTextView = (TextView) findViewById(R.id.quiz_exception_page_back_button);
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}