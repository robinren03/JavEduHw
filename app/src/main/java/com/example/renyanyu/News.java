package com.example.renyanyu;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class News implements Serializable {
    @NonNull
    @PrimaryKey
    public String title; // 标题

    public String content; //内容
    public News(String a,String b){
        super();
        title=a;
        title=b;
    }

    public News(){
        super();
    }
}
