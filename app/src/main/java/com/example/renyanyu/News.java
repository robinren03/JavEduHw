package com.example.renyanyu;

class News {
    public String title,uri; // 标题
    public String content,course; //内容
    public boolean father;
    public News(String a,String b){
        super();
        title=a;
        content=b;
    }
    public News(String a){
        title=a;
    }
    public News(String a,String b,String c){
        title=a;
        content=b;
        uri=c;
    }
    public News(String a,String b,String c,String d){
        title=a;
        content=b;
        uri=c;
        course=d;
    }
    public News(String a,String b,String c,String d,boolean e){
        title=a;
        content=b;
        uri=c;
        course=d;
        father=e;
    }
}
