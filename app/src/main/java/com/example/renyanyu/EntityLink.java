package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;



public class EntityLink extends AppCompatActivity {

    String subject="chinese";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_link);

        ListView resultsListView=(ListView)findViewById(R.id.entity_link_results_list);
        resultsListView.setVisibility(View.GONE);
        Button reSearchButton=(Button) findViewById(R.id.reSearch_entity_link_button);
        reSearchButton.setVisibility(View.GONE);
        TextView resultsTextView=(TextView)findViewById(R.id.entity_link_page_results_text);
        resultsTextView.setVisibility(View.GONE);

        // region 通过RadioGroup获取所要查询的subject
        RadioGroup radioGroup1=(RadioGroup) findViewById(R.id.select_subject_radio_group_1);//获取单选按钮组
        RadioGroup radioGroup2=(RadioGroup) findViewById(R.id.select_subject_radio_group_2);//获取单选按钮组
        radioGroup1.check(R.id.select_chinese_radio_button);//默认选中语文按钮
        //为单选按钮组添加事件监听，获取选择的学科
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id)
                {
                    case R.id.select_chinese_radio_button:
                        if(((RadioButton) findViewById(id)).isChecked())
                        {
                            subject ="chinese";
                            radioGroup2.clearCheck();
                        }
                        break;
                    case R.id.select_math_radio_button:
                        if(((RadioButton) findViewById(id)).isChecked())
                        {
                            subject ="math";
                            radioGroup2.clearCheck();
                        }
                        break;
                    case R.id.select_english_radio_button:
                        if(((RadioButton) findViewById(id)).isChecked())
                        {
                            subject ="english";
                            radioGroup2.clearCheck();
                        }
                        break;
                    case R.id.select_physics_radio_button:
                        if(((RadioButton) findViewById(id)).isChecked())
                        {
                            subject ="physics";
                            radioGroup2.clearCheck();
                        }
                        break;
                    case R.id.select_chemistry_radio_button:
                        if(((RadioButton) findViewById(id)).isChecked())
                        {
                            subject ="chemistry";
                            radioGroup2.clearCheck();
                        }
                        break;
                    default:
                        break;
                }
                System.out.println("Group1-subject:"+subject);
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id)
                {
                    case R.id.select_politics_radio_button:
                        if(((RadioButton) findViewById(id)).isChecked())
                        {
                            subject ="politics";
                            radioGroup1.clearCheck();
                        }
                        break;
                    case R.id.select_history_radio_button:
                        if(((RadioButton) findViewById(id)).isChecked())
                        {
                            subject ="history";
                            radioGroup1.clearCheck();
                        }
                        break;
                    case R.id.select_geo_radio_button:
                        if(((RadioButton) findViewById(id)).isChecked())
                        {
                            subject ="geo";
                            radioGroup1.clearCheck();
                        }
                        break;
                    case R.id.select_biology_radio_button:
                        if(((RadioButton) findViewById(id)).isChecked())
                        {
                            subject ="biology";
                            radioGroup1.clearCheck();
                        }
                        break;
                    default:
                        break;
                }
                System.out.println("Group2-subject:"+subject);
            }
        });
        // endregion

        Button searchButton=(Button) findViewById(R.id.search_entity_link_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText=(EditText)findViewById(R.id.entity_link_edit_text);
                String content=editText.getText().toString();
                if(!content.equals(""))
                {
                    try
                    {
                        // region 获取resultsArray
                        String url = EntityLink.this.getString(R.string.backend_ip) + "/request/link";
                        String msg = "course="+subject+"&context="+content;
                        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                        String responseString = serverHttpResponse.postResponse(url, msg);
                        System.out.println(responseString);
                        JSONObject answer_json = new JSONObject(responseString);
                        JSONArray resultsArray = (JSONArray) ((JSONObject) answer_json.get("data")).get("results");
                        // endregion

                        // region 构建类StartAndEndIndexOfEntity用于保存实体出现的index，类Entity用于保存实体信息
                        class StartAndEndIndexOfEntity
                        {
                            StartAndEndIndexOfEntity(int start,int end)
                            {
                                startIndex=start;
                                endIndex=end;
                            }
                            int startIndex=0;
                            int endIndex=0;
                        }
                        class Entity
                        {
                            Entity(String _name,String _type, String _uri)
                            {
                                name=_name;
                                type=_type;
                                uri=_uri;
                            }
                            String name;
                            String type;
                            String uri;
                        }
                        //endregion

                        ArrayList<StartAndEndIndexOfEntity> startAndEndIndexOfEntities=new ArrayList<>();
                        ArrayList<Entity> entityList=new ArrayList<>();
                        for(int i=0;i<resultsArray.length();i++)
                        {
                            JSONObject entity_json=resultsArray.getJSONObject(i);
                            startAndEndIndexOfEntities.add(new StartAndEndIndexOfEntity(
                                    (Integer) entity_json.get("start_index"),(Integer) entity_json.get("end_index")));
                            entityList.add(new Entity((String) entity_json.get("entity"),
                                    (String) entity_json.get("entity_type"), (String) entity_json.get("entity_url")));
                        }

                        final String[] entityAbstractInfoList =new String[resultsArray.length()];
                        for(int i=0;i<resultsArray.length();i++)
                        {
                            entityAbstractInfoList[i]="实体类型："+entityList.get(i).type+"\n实体名称："+entityList.get(i).name;
                        }
                        //配置ArrayAdapter适配器
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                                (EntityLink.this,android.R.layout.simple_expandable_list_item_1,entityAbstractInfoList);
                        resultsListView.setAdapter(adapter);
                        //设置选中选项监听
                        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {

//                                String url = EntityLink.this.getString(R.string.backend_ip) + "/request/instance";
//                                String msg="?course="+subject+"&name="+entityList.get(position).name;
//                                String res= serverHttpResponse.getResponse(url+msg);
//
//                                String ur=EntityLink.this.getString(R.string.backend_ip) + "/request/card";
//                                String ms="course="+ subject+"&uri="+entityList.get(position).uri;
//                                String re= serverHttpResponse.postResponse(ur,ms);

                                Intent goToEntityDetailsPage = new Intent(EntityLink.this,EntityDetails.class);
                                //goToEntityDetailsPage.putExtra("result",res);
                                //goToEntityDetailsPage.putExtra("card",re);
                                goToEntityDetailsPage.putExtra("entity_name",entityList.get(position).name);
                                Toast.makeText(EntityLink.this, "name:"+entityList.get(position).name, Toast.LENGTH_SHORT).show();
                                goToEntityDetailsPage.putExtra("type",entityList.get(position).type);
                                goToEntityDetailsPage.putExtra("uri",entityList.get(position).uri);

                                startActivity(goToEntityDetailsPage);
                            }
                        });

                        SpannableStringBuilder spannable = new SpannableStringBuilder(content);
                        for(int i=0;i<startAndEndIndexOfEntities.size();i++)
                        {
                            StartAndEndIndexOfEntity indexRange=startAndEndIndexOfEntities.get(i);
                            spannable.setSpan(new ForegroundColorSpan(Color.RED),indexRange.startIndex,indexRange.endIndex+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        ((TextView)findViewById(R.id.entity_link_page_results_text)).setText(spannable);


                        resultsTextView.setVisibility(View.VISIBLE);
                        resultsListView.setVisibility(View.VISIBLE);
                        reSearchButton.setVisibility(View.VISIBLE);
                        searchButton.setVisibility(View.GONE);
                        radioGroup1.setVisibility(View.GONE);
                        radioGroup2.setVisibility(View.GONE);
                        editText.setVisibility(View.GONE);


                        String subjectInChinese="";
                        switch (subject)
                        {
                            case "chinese":
                                subjectInChinese="语文";
                                break;
                            case "math":
                                subjectInChinese="数学";
                                break;
                            case "english":
                                subjectInChinese="英语";
                                break;
                            case "physics":
                                subjectInChinese="物理";
                                break;
                            case "chemistry":
                                subjectInChinese="化学";
                                break;
                            case "politics":
                                subjectInChinese="政治";
                                break;
                            case "history":
                                subjectInChinese="历史";
                                break;
                            case "geo":
                                subjectInChinese="地理";
                                break;
                            case "biology":
                                subjectInChinese="生物";
                                break;
                            default:
                                break;
                        }

                        TextView guideInfoText =(TextView)findViewById(R.id.entity_link_page_guide_info);
                        if(entityList.isEmpty())
                            guideInfoText.setText("很抱歉，我们在"+subjectInChinese+"这个学科下没有找到这段文本的相关实体。");
                        else
                            guideInfoText.setText("这是我们在"+subjectInChinese+"这个学科下找到的文本中包含的实体:");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        reSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultsListView.setVisibility(View.GONE);
                reSearchButton.setVisibility(View.GONE);
                searchButton.setVisibility(View.VISIBLE);
                radioGroup1.setVisibility(View.VISIBLE);
                radioGroup2.setVisibility(View.VISIBLE);
                ((EditText)findViewById(R.id.entity_link_edit_text)).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.entity_link_page_guide_info)).setText("请选择要查询的学科:");
                resultsTextView.setVisibility(View.GONE);
            }
        });
    }
}
