package com.example.renyanyu;

public class Exercise {
    public String stem;
    public String answer_a,answer_b,answer_c,answer_d;
    public String answer;
    public Exercise(String t,String a,String b,String c,String d,String e){
        stem=t;
        answer_a=a;
        answer_b=b;
        answer_c=c;
        answer_d=d;
        answer=e;
    }
    public Exercise(String t,String a){
        stem=t;
        answer=a;
        answer_a="";answer_b="";answer_c="";answer_d="";
    }
}
