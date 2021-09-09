package com.example.renyanyu;

/**
 * function:
 * Created by TMJ on 2020-02-20.
 */
public class Message {

    private int id;//在整个布局里算第几个Message
    private int imgResId;//Image图片的id
    private String title;//标题
    private String  content;//内容

    //无参构造函数
    public Message() {

    }

    //有参构造函数
    public Message(int id, int imgResId, String title, String content) {
        this.id = id;
        this.imgResId = imgResId;
        this.title = title;
        this.content = content;
    }

    //Getter and Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


