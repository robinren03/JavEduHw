package com.example.renyanyu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import android.widget.*;

import com.google.gson.Gson;

public class SearchPage extends AppCompatActivity {

    TextView sub1,sub2,sub3,sub4,sub5,sub6,sub7,sub8,sub9,t1,t2,t3,t4,t5,t6,t7,t8,t9;
    Intent t,t_return;
    boolean[] sub=new boolean[]{false,false,false,false,false,false,false,false,false};
    int num,current;
    Button confirm,revoke;
    String subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_choose);
        sub1=findViewById(R.id.sub1);
        sub2=findViewById(R.id.sub2);
        sub3=findViewById(R.id.sub3);
        sub4=findViewById(R.id.sub4);
        sub5=findViewById(R.id.sub5);
        sub6=findViewById(R.id.sub6);
        sub7=findViewById(R.id.sub7);
        sub8=findViewById(R.id.sub8);
        sub9=findViewById(R.id.sub9);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        t4=findViewById(R.id.t4);
        t5=findViewById(R.id.t5);
        t6=findViewById(R.id.t6);
        t7=findViewById(R.id.t7);
        t8=findViewById(R.id.t8);
        t9=findViewById(R.id.t9);
        revoke=findViewById(R.id.revoke);
        confirm=findViewById(R.id.confirm);
        num=1;
        current=0;
        t_return=new Intent(SearchPage.this,MainActivity.class);
        t=getIntent();
        subject=t.getStringExtra("subject");
        initbool();

        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sub[0]){
                    t1.setText(""+num);
                    current=1;
                    num++;
                }
                else{
                    Toast.makeText(SearchPage.this, "您没有选择此科目", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sub[1]){
                    t2.setText(""+num);
                    current=2;
                    num++;
                }
                else{
                    Toast.makeText(SearchPage.this, "您没有选择此科目", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sub[2]){
                    t3.setText(""+num);
                    current=3;
                    num++;
                }
                else{
                    Toast.makeText(SearchPage.this, "您没有选择此科目", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sub[3]){
                    t4.setText(""+num);
                    current=4;
                    num++;
                }
                else{
                    Toast.makeText(SearchPage.this, "您没有选择此科目", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sub[4]){
                    t5.setText(""+num);
                    current=5;
                    num++;
                }
                else{
                    Toast.makeText(SearchPage.this, "您没有选择此科目", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sub[5]){
                    t6.setText(""+num);
                    current=6;
                    num++;
                }
                else{
                    Toast.makeText(SearchPage.this, "您没有选择此科目", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sub[6]){
                    t7.setText(""+num);
                    current=7;
                    num++;
                }
                else{
                    Toast.makeText(SearchPage.this, "您没有选择此科目", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sub[7]){
                    t8.setText(""+num);
                    current=8;
                    num++;
                }
                else{
                    Toast.makeText(SearchPage.this, "您没有选择此科目", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sub9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sub[8]){
                    t9.setText(""+num);
                    current=9;
                    num++;
                }
                else{
                    Toast.makeText(SearchPage.this, "您没有选择此科目", Toast.LENGTH_SHORT).show();
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("lenght: "+subject+" "+num);
                if(num-subject.length()!=1)
                {
                    Toast.makeText(SearchPage.this, "您还没有完成排序", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    return_order();
                    startActivity(t_return);
                }

            }
        });
        revoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num==1)
                {
                    Toast.makeText(SearchPage.this, "您还未开始排序操作", Toast.LENGTH_SHORT).show();
                }
                num--;

                revoke_last();
                getcurrent();
            }
        });
    }

    public void initbool(){
        for(int i=0;i<9;i++){
            if(subject.contains(""+i)){
                sub[i]=true;
            }
        }
    }

    public void return_order(){
        if(sub[0])t_return.putExtra("0",t1.getText());
        if(sub[1])t_return.putExtra("1",t2.getText());
        if(sub[2])t_return.putExtra("2",t3.getText());
        if(sub[3])t_return.putExtra("3",t4.getText());
        if(sub[4])t_return.putExtra("4",t5.getText());
        if(sub[5])t_return.putExtra("5",t6.getText());
        if(sub[6])t_return.putExtra("6",t7.getText());
        if(sub[7])t_return.putExtra("7",t8.getText());
        if(sub[8])t_return.putExtra("8",t9.getText());
    }

    public void revoke_last(){
        if(current==1)t1.setText("");
        if(current==2)t2.setText("");
        if(current==3)t3.setText("");
        if(current==4)t4.setText("");
        if(current==5)t5.setText("");
        if(current==6)t6.setText("");
        if(current==7)t7.setText("");
        if(current==8)t8.setText("");
        if(current==9)t9.setText("");
    }

    public void getcurrent(){
        int last_num=num-1;
        System.out.println("num:"+last_num+" order:"+t2.getText()+" shengwu"+t6.getText());
        if(t1.getText().equals(""+last_num))current=1;
        if(t2.getText().equals(""+last_num))current=2;
        if(t3.getText().equals(""+last_num))current=3;
        if(t4.getText().equals(""+last_num))current=4;
        if(t5.getText().equals(""+last_num))current=5;
        if(t6.getText().equals(""+last_num))current=6;
        if(t7.getText().equals(""+last_num))current=7;
        if(t8.getText().equals(""+last_num))current=8;
        if(t9.getText().equals(""+last_num))current=9;
    }

}