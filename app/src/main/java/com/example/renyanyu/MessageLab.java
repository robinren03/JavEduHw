package com.example.renyanyu;


import java.util.ArrayList;
import java.util.List;

/**
 * function: 辅助作用
 * Created by TMJ on 2020-02-20.
 */
public class MessageLab {

    public static List<Message> generateMockList() {

        List<Message> messageList = new ArrayList<>();

        Message message = new Message(1,
                R.drawable.qa2,
                "遇到困扰已久的难题了吗？",
                "这里有毕业于清北的各大名校的老师亲自为你解答！");
        messageList.add(message);


        message = new Message(2,
                R.drawable.shitilianjie,
                "苦恼文章太多，不知何处是重点？",
                "全文搜索，AI筛选知识点！");
        messageList.add(message);

        message = new Message(3,
                R.drawable.subject,
                "需要更加专业详尽的知识点？",
                "详细列举每科知识点，针对提高！！");
        messageList.add(message);



        return messageList;

    }
}


