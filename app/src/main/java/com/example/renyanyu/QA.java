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
            //??????????????????????????????????????????????????????????????????????????????????????????
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.left_msg.setText(msg.getContent());

            //???????????????????????????????????????????????? View.GONE
            holder.rightLayout.setVisibility(View.GONE);
        }
        else if(msg.getType() == Msg.TYPE_SEND){
            //??????????????????????????????????????????????????????????????????????????????????????????
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.right_msg.setText(msg.getContent());

            //????????????View.GONE
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
    String subjectInChinese="??????";
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

        chineseToEnglish.put("??????","chinese");chineseToEnglish.put("??????","math");chineseToEnglish.put("??????","english");
        chineseToEnglish.put("??????","physics");chineseToEnglish.put("??????","chemistry");chineseToEnglish.put("??????","politics");
        chineseToEnglish.put("??????","history");chineseToEnglish.put("??????","geo");chineseToEnglish.put("??????","biology");

        englishToChinese.put("chinese","??????");englishToChinese.put("math","??????");englishToChinese.put("english","??????");
        englishToChinese.put("physics","??????");englishToChinese.put("chemistry","??????");englishToChinese.put("politics","??????");
        englishToChinese.put("history","??????");englishToChinese.put("geo","??????");englishToChinese.put("biology","??????");

        TextView relatedEntityTextView = (TextView) findViewById(R.id.qa_page_entity_related_text_view);
        relatedEntityTextView.setVisibility(View.GONE);
        relatedEntityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(relatedEntityUri==null)
                {
                    msgList.add(new Msg("?????????????????????????????????????????????????????????????????????????????????", Msg.TYPE_RECEIVED));
                }
                else if(relatedEntityUri.equals(""))
                {
                    msgList.add(new Msg("???????????????"+subjectInChinese+"????????????????????????????????????????????????????????????",
                            Msg.TYPE_RECEIVED));
                }
                else
                {



                    String url=QA.this.getString(R.string.backend_ip) + "/request/card";
                    String message="course="+ subjectInEnglish+"&uri="+relatedEntityUri;
                    String responseString= serverHttpResponse.postResponse(url,message);
                    System.out.println("relatedEntityTextView??????card????????????????????????:"+responseString);
                    if(responseString==null)
                    {
                        msgList.add(new Msg("?????????????????????????????????????????????", Msg.TYPE_RECEIVED));
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


                            answer+=("?????????"+entityName+"\n");
                            answer+=("?????????"+entityType+"\n");
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
                            //System.out.println("?????????????????????"+entityList.get(position).name+" "+entityList.get(position).uri);
                            startActivity(goToEntityDetailsPage);
//                            finish();

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("QA page:????????????????????????????????????");
                            msgList.add(new Msg("????????????????????????????????????????????????????????????", Msg.TYPE_RECEIVED));
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
                    msgList.add(new Msg("???????????????????????????????????????", Msg.TYPE_RECEIVED));
                }
                else
                {
                    if(content.equals(""))
                    {
                        msgList.add(new Msg("???????????????????????????", Msg.TYPE_RECEIVED));
                    }
                    String toBeSearchedSubject = toBeSearchedSubjectSet.first();
                    String url = QA.this.getString(R.string.backend_ip) + "/request/question";
                    System.out.println("????????????"+toBeSearchedSubject);
                    String msg = "course="+toBeSearchedSubject+"&inputQuestion="+content;
                    String responseString = serverHttpResponse.postResponse(url, msg);
                    System.out.println(responseString);
                    if(responseString==null)
                    {
                        msgList.add(new Msg("?????????????????????????????????????????????", Msg.TYPE_RECEIVED));
                        exceptionFlag=1;
                        System.out.println("break because ?????????????????????????????????????????????");
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
                                msgList.add(new Msg("???????????????"+englishToChinese.get(toBeSearchedSubject)
                                        +"????????????????????????????????????????????????????????????", Msg.TYPE_RECEIVED));
                            }
                            else
                            {
                                msgList.add(new Msg(answer,Msg.TYPE_RECEIVED));
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("QA page:????????????????????????????????????");
                            msgList.add(new Msg("???????????????????????????????????????????????????????????????", Msg.TYPE_RECEIVED));
//                            msgList.add(new Msg("???????????????"+englishToChinese.get(toBeSearchedSubject)
//                                    +"????????????????????????????????????????????????????????????", Msg.TYPE_RECEIVED));
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
        subjectList.add("??????");subjectList.add("??????");subjectList.add("??????");subjectList.add("??????");
        subjectList.add("??????");subjectList.add("??????");subjectList.add("??????");subjectList.add("??????");
        subjectList.add("??????");
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


/*       ??????????????????button?????????????????????????????????????????????????????????????????? RecyclerView ??????
            ??????????????????????????????????????????????????? List ????????????
            ?????????????????????notifyItemInserted????????????????????????????????????????????????????????????????????? RecyclerView ????????????
            ?????????RecyclerView???scrollToPosition?????????????????????????????????????????????????????????????????????*/
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
                    inputText.setText("");//???????????????????????????

                    toBeSearchedSubjectSet.clear();
                    toBeSearchedSubjectSet.add("chinese");toBeSearchedSubjectSet.add("math");toBeSearchedSubjectSet.add("english");
                    toBeSearchedSubjectSet.add("physics");toBeSearchedSubjectSet.add("chemistry");toBeSearchedSubjectSet.add("politics");
                    toBeSearchedSubjectSet.add("history");toBeSearchedSubjectSet.add("geo");toBeSearchedSubjectSet.add("biology");

                    String url = QA.this.getString(R.string.backend_ip) + "/request/question";
                    String answer = "";
                    subjectInEnglish = chineseToEnglish.get(subjectInChinese);
                    System.out.println("????????????"+subjectInEnglish);
                    String msg = "course="+subjectInEnglish+"&inputQuestion="+content;
                    String res = serverHttpResponse.postResponse(url, msg);
                    System.out.println(res);
                    if(res==null)
                    {
                        relatedEntityUri=null;
                        msgList.add(new Msg("?????????????????????????????????????????????", Msg.TYPE_RECEIVED));
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
                                msgList.add(new Msg("???????????????"+subjectInChinese+"??????????????????????????????????????????",
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
                            System.out.println("QA page:????????????????????????????????????");
                            msgList.add(new Msg("????????????????????????????????????????????????????????????", Msg.TYPE_RECEIVED));
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
        list.add(new Msg("Hello?????????????????????????????????????????????????????????????????? ^_^ ",Msg.TYPE_RECEIVED));
        return list;
    }
}

