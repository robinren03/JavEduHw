package com.example.renyanyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

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
        if(msg.getType() == Msg.TYPE_RECEIVED)
        {
            //如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.left_msg.setText(msg.getContent());

            //注意此处隐藏右面的消息布局用的是 View.GONE
            holder.rightLayout.setVisibility(View.GONE);
        }
        else if(msg.getType() == Msg.TYPE_SEND){
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
    String subjectInChinese="语文";
    String subjectInEnglish = "chinese";
    TreeSet<String> toBeSearchedSubjectSet = new TreeSet<>();

    String content;
    String relatedEntityUri="";
    HashMap<String,String> chineseToEnglish = new HashMap<>();
    HashMap<String,String> englishToChinese = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qa);

        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        layoutManager = new LinearLayoutManager(this);
        adapter = new MsgAdapter(msgList = getData());

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

        chineseToEnglish.put("语文","chinese");chineseToEnglish.put("数学","math");chineseToEnglish.put("英语","english");
        chineseToEnglish.put("物理","physics");chineseToEnglish.put("化学","chemistry");chineseToEnglish.put("政治","politics");
        chineseToEnglish.put("历史","history");chineseToEnglish.put("地理","geo");chineseToEnglish.put("生物","biology");

        englishToChinese.put("chinese","语文");englishToChinese.put("math","数学");englishToChinese.put("english","英语");
        englishToChinese.put("physics","物理");englishToChinese.put("chemistry","化学");englishToChinese.put("politics","政治");
        englishToChinese.put("history","历史");englishToChinese.put("geo","地理");englishToChinese.put("biology","生物");

        TextView relatedEntityTextView = (TextView) findViewById(R.id.qa_page_entity_related_text_view);
        relatedEntityTextView.setVisibility(View.GONE);
        relatedEntityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(relatedEntityUri==null)
                {
                    msgList.add(new Msg("好像您提问的时候断网了，请检查您的网络设置后重新提问。", Msg.TYPE_RECEIVED));
                }
                else if(relatedEntityUri.equals(""))
                {
                    msgList.add(new Msg("很抱歉，在"+subjectInChinese+"这个学科下好像没有与这个问题相关的实体。",
                            Msg.TYPE_RECEIVED));
                }
                else
                {



                    String url=QA.this.getString(R.string.backend_ip) + "/request/card";
                    String message="course="+ subjectInEnglish+"&uri="+relatedEntityUri;
                    String responseString= serverHttpResponse.postResponse(url,message);
                    System.out.println("relatedEntityTextView请求card接口返回的结果是:"+responseString);
                    if(responseString==null)
                    {
                        msgList.add(new Msg("好像断网了，请检查您的网络设置", Msg.TYPE_RECEIVED));
                    }
                    else
                    {
                        try
                        {
                            String answer="";
                            JSONObject jsonObject = new JSONObject(responseString);
                            JSONObject data = ((JSONObject) jsonObject.get("data"));
                            String entityName = data.getString("entity_name");
                            String entityType = data.getString("entity_type");


                            answer+=("名称："+entityName+"\n");
                            answer+=("类型："+entityType+"\n");
                            JSONArray entityFeaturesJSONArray=(JSONArray)data.get("entity_features");
                            for(int i=0;i<entityFeaturesJSONArray.length();i++)
                            {
                                JSONObject entityFeatureJSONObject = (JSONObject) entityFeaturesJSONArray.get(i);
                                String featureKey=entityFeatureJSONObject.getString("feature_key");
                                String featureValue=entityFeatureJSONObject.getString("feature_value");
                                answer+=(featureKey+":"+featureValue+"\n");
                            }
                            msgList.add(new Msg(answer, Msg.TYPE_RECEIVED));

                            adapter.notifyItemInserted(msgList.size()-1);
                            msgRecyclerView.scrollToPosition(msgList.size()-1);

                            Intent goToEntityDetailsPage = new Intent(QA.this,EntityDetails.class);
                            goToEntityDetailsPage.putExtra("entity_name",entityName);
                            goToEntityDetailsPage.putExtra("type",entityType);
                            goToEntityDetailsPage.putExtra("uri",relatedEntityUri);
                            goToEntityDetailsPage.putExtra("course",subjectInEnglish);
                            //System.out.println("真正历史记录："+entityList.get(position).name+" "+entityList.get(position).uri);
                            startActivity(goToEntityDetailsPage);
//                            finish();

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("QA page:服务器返回的信息格式不对");
                            msgList.add(new Msg("很抱歉，服务器好像出了点问题，请稍后重试", Msg.TYPE_RECEIVED));
                        }
                    }
                }
                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
            }
        });
        TextView nextAnswerTextView = (TextView) findViewById(R.id.qa_page_next_answer_text_view);
        nextAnswerTextView.setVisibility(View.GONE);
        nextAnswerTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String answer = "";
                int exceptionFlag=0;
                if(toBeSearchedSubjectSet.isEmpty())
                {
                    msgList.add(new Msg("这个问题好像没有更多答案了", Msg.TYPE_RECEIVED));
                }
                else
                {
                    if(content.equals(""))
                    {
                        msgList.add(new Msg("您还没有输入问题呢", Msg.TYPE_RECEIVED));
                    }
                    String toBeSearchedSubject = toBeSearchedSubjectSet.first();
                    String url = QA.this.getString(R.string.backend_ip) + "/request/question";
                    System.out.println("已经查到"+toBeSearchedSubject);
                    String msg = "course="+toBeSearchedSubject+"&inputQuestion="+content;
                    String responseString = serverHttpResponse.postResponse(url, msg);
                    System.out.println(responseString);
                    if(responseString==null)
                    {
                        msgList.add(new Msg("好像断网了，请检查您的网络设置", Msg.TYPE_RECEIVED));
                        exceptionFlag=1;
                        System.out.println("break because 好像断网了，请检查您的网络设置");
                    }
                    else
                    {
                        try
                        {
                            toBeSearchedSubjectSet.remove(toBeSearchedSubject);
                            JSONObject answer_json = new JSONObject(responseString);
                            JSONObject data = ((JSONArray) answer_json.get("data")).getJSONObject(0);
                            answer=data.get("value").toString();
                            System.out.println("remove"+toBeSearchedSubject+"from"+toBeSearchedSubjectSet);
                            System.out.println("answer:"+answer);
                            if(answer.equals(""))
                            {
                                msgList.add(new Msg("很抱歉，在"+englishToChinese.get(toBeSearchedSubject)
                                        +"这个学科下好像没有与这个问题相关的实体。", Msg.TYPE_RECEIVED));
                            }
                            else
                            {
                                msgList.add(new Msg(answer,Msg.TYPE_RECEIVED));
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("QA page:服务器返回的信息格式不对");
                            msgList.add(new Msg("你点击的太快了哦，服务器都反应不过来了呢。", Msg.TYPE_RECEIVED));
//                            msgList.add(new Msg("很抱歉，在"+englishToChinese.get(toBeSearchedSubject)
//                                    +"这个学科下好像没有与这个问题相关的实体。", Msg.TYPE_RECEIVED));
                            exceptionFlag=1;
                        }
                    }
                }

                System.out.println("exceptionFlag:"+exceptionFlag);
                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
            }
        });




        Spinner spinner = (Spinner) findViewById(R.id.qa_page_spinner);
        ArrayList<String> subjectList = new ArrayList<>();
        subjectList.add("语文");subjectList.add("数学");subjectList.add("英语");subjectList.add("物理");
        subjectList.add("化学");subjectList.add("政治");subjectList.add("历史");subjectList.add("地理");
        subjectList.add("生物");
        ArrayAdapter subjectStringAdapter
                = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, subjectList);
        subjectStringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(subjectStringAdapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                subjectInChinese=subjectStringAdapter.getItem(arg2).toString();
//                System.out.println(choose);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


/*       我们还需要为button建立一个监听器，我们需要将编辑框的内容发送到 RecyclerView 上：
            ①获取内容，将需要发送的消息添加到 List 当中去。
            ②调用适配器的notifyItemInserted方法，通知有新的数据加入了，赶紧将这个数据加到 RecyclerView 上面去。
            ③调用RecyclerView的scrollToPosition方法，以保证一定可以看的到最后发出的一条消息。*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                content = inputText.getText().toString();
                if(!content.equals(""))
                {
                    msgList.add(new Msg(content,Msg.TYPE_SEND));
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");//清空输入框中的内容

                    toBeSearchedSubjectSet.clear();
                    toBeSearchedSubjectSet.add("chinese");toBeSearchedSubjectSet.add("math");toBeSearchedSubjectSet.add("english");
                    toBeSearchedSubjectSet.add("physics");toBeSearchedSubjectSet.add("chemistry");toBeSearchedSubjectSet.add("politics");
                    toBeSearchedSubjectSet.add("history");toBeSearchedSubjectSet.add("geo");toBeSearchedSubjectSet.add("biology");

                    String url = QA.this.getString(R.string.backend_ip) + "/request/question";
                    String answer = "";
                    subjectInEnglish = chineseToEnglish.get(subjectInChinese);
                    System.out.println("已经查到"+subjectInEnglish);
                    String msg = "course="+subjectInEnglish+"&inputQuestion="+content;
                    String res = serverHttpResponse.postResponse(url, msg);
                    System.out.println(res);
                    if(res==null)
                    {
                        relatedEntityUri=null;
                        msgList.add(new Msg("好像断网了，请检查您的网络设置", Msg.TYPE_RECEIVED));
                    }
                    else
                    {
                        try
                        {
                            JSONObject answer_json = new JSONObject(res);
                            JSONObject data = ((JSONArray) answer_json.get("data")).getJSONObject(0);
                            try
                            {
                                answer=data.get("value").toString();
                                relatedEntityUri = data.get("subjectUri").toString();
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                                relatedEntityUri="";
                            }
                            toBeSearchedSubjectSet.remove(subjectInEnglish);
                            if(answer.equals(""))
                            {
                                msgList.add(new Msg("很抱歉，在"+subjectInChinese+"学科下没有找到这个问题的答案",
                                        Msg.TYPE_RECEIVED));
                            }
                            else
                            {
                                msgList.add(new Msg(answer,Msg.TYPE_RECEIVED));
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("QA page:服务器返回的信息格式不对");
                            msgList.add(new Msg("很抱歉，服务器好像出了点问题，请稍后重试", Msg.TYPE_RECEIVED));
                        }
                    }
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                }
            }
        });

        Button moreDetailsButton = (Button) findViewById(R.id.qa_page_more_details_button);
        moreDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView relatedEntityTextView = (TextView) findViewById(R.id.qa_page_entity_related_text_view);
                TextView nextAnswerTextView = (TextView) findViewById(R.id.qa_page_next_answer_text_view);
                if(relatedEntityTextView.getVisibility()==View.GONE)
                {
                    relatedEntityTextView.setVisibility(View.VISIBLE);
                    nextAnswerTextView.setVisibility(View.VISIBLE);
                }
                else
                {
                    relatedEntityTextView.setVisibility(View.GONE);
                    nextAnswerTextView.setVisibility(View.GONE);
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

