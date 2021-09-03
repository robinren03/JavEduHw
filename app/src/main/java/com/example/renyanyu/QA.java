package com.example.renyanyu;

//package com.example.renyanyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;
    private String content;
    private int type;

    public Msg(String content,int type)
    {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}


class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>
{

    private List<Msg> list;
    public MsgAdapter(List<Msg> list){
        this.list = list;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        TextView left_msg;

        LinearLayout rightLayout;
        TextView right_msg;

        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            left_msg = view.findViewById(R.id.left_msg);

            rightLayout = view.findViewById(R.id.right_layout);
            right_msg = view.findViewById(R.id.right_msg);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Msg msg;
        msg = list.get(position);
        if(msg.getType() == Msg.TYPE_RECEIVED){
            //如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.left_msg.setText(msg.getContent());

            //注意此处隐藏右面的消息布局用的是 View.GONE
            holder.rightLayout.setVisibility(View.GONE);
        }else if(msg.getType() == Msg.TYPE_SEND){
            //如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.right_msg.setText(msg.getContent());

            //同样使用View.GONE
            holder.leftLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}



public class QA extends AppCompatActivity
{
    private ServerHttpResponse serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
    private static final String TAG = "QA";
    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private Button send;
    private LinearLayoutManager layoutManager;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);

        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        layoutManager = new LinearLayoutManager(this);
        adapter = new MsgAdapter(msgList = getData());

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

/*       我们还需要为button建立一个监听器，我们需要将编辑框的内容发送到 RecyclerView 上：
            ①获取内容，将需要发送的消息添加到 List 当中去。
            ②调用适配器的notifyItemInserted方法，通知有新的数据加入了，赶紧将这个数据加到 RecyclerView 上面去。
            ③调用RecyclerView的scrollToPosition方法，以保证一定可以看的到最后发出的一条消息。*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String content = inputText.getText().toString();
                if(!content.equals(""))
                {
                    msgList.add(new Msg(content,Msg.TYPE_SEND));
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");//清空输入框中的内容
                    try
                    {
                        String url = QA.this.getString(R.string.backend_ip) + "/request/question";
                        //String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
                        //String id="84b8cd42-e823-4e5a-b908-7da345330766";
                        String[] subjects={"chinese","math","english","physics","chemistry","biology","history","geo","politics"};
                        String res;
                        String answer = "";
                        for(int i=0;i<9;i++)
                        {
                            System.out.println("已经查到"+subjects[i]);
                            String msg = "course="+subjects[i]+"&inputQuestion="+content;//+"&id="+id;
                            res = serverHttpResponse.postResponse(url, msg);
                            System.out.println(res);
                            try{
                                JSONObject answer_json = new JSONObject(res);
                                JSONObject data = ((JSONArray) answer_json.get("data")).getJSONObject(0);
                                answer=data.get("value").toString();
                            }
                            catch (Exception e)
                            {

                                e.printStackTrace();
                                continue;
                            }


                            if(!answer.equals(""))    break;
//                            try {
//
//                            }
//                            catch (Exception e)
//                            {
//                                continue;
//                            }
                        }
                        if(answer.equals(""))
                        {
                            msgList.add(new Msg("很抱歉，我也不知道这个问题的答案（︶︿︶）",Msg.TYPE_RECEIVED));
                        }
                        else
                        {
                            msgList.add(new Msg(answer,Msg.TYPE_RECEIVED));
                        }
                        adapter.notifyItemInserted(msgList.size()-1);
                        msgRecyclerView.scrollToPosition(msgList.size()-1);

                    }
                    catch (Exception e) {
                        msgList.add(new Msg("呜呜呜，服务器好像又双叒叕挂掉了，请稍后再来问这个问题吧(￢_￢)",Msg.TYPE_RECEIVED));
                        adapter.notifyItemInserted(msgList.size()-1);
                        msgRecyclerView.scrollToPosition(msgList.size()-1);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private List<Msg> getData(){
        List<Msg> list = new ArrayList<>();
        list.add(new Msg("Hello，随便问我点什么问题吧，我会尽可能帮你解答的 ^_^ ",Msg.TYPE_RECEIVED));
        return list;
    }
}

