package com.example.renyanyu;

public class Exercise {
    public String stem;
    public String answer_a,answer_b,answer_c,answer_d;
    public String answer;
    String id,entity_name;
    public Exercise(String t,String a,String b,String c,String d,String e,String f,String g){
        stem=t;
        answer_a=a;
        answer_b=b;
        answer_c=c;
        answer_d=d;
        answer=e;
        id=f;
        entity_name=g;
    }
    public Exercise(String t,String a,String b,String c){
        stem=t;
        answer=a;
        answer_a="";answer_b="";answer_c="";answer_d="";
        id=b;
        entity_name=c;
    }
}
