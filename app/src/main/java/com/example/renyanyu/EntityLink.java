package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EntityLink extends AppCompatActivity {

    String subject="chinese";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_link);



        ListView resultsListView=(ListView)findViewById(R.id.entity_link_results_list);
        resultsListView.setVisibility(View.GONE);

        Button searchButton=(Button) findViewById(R.id.search_entity_link_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText=(EditText)findViewById(R.id.entity_link_edit_text);
                String content=editText.getText().toString();
                if(!content.equals(""))
                {
                    // region 通过RadioGroup获取所要查询的subject
                    final RadioGroup radioGroup=(RadioGroup) findViewById(R.id.select_subject_radio_group);//获取单选按钮组
                    radioGroup.addView(findViewById(R.id.select_chinese_radio_button));
                    radioGroup.check(R.id.select_chinese_radio_button);//默认选中语文按钮
                    //为单选按钮组添加事件监听，获取选择的学科
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @SuppressLint("NonConstantResourceId")
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int id) {
                            radioGroup.clearCheck();
                            radioGroup.check(id);
                            switch (id)
                            {
                                case R.id.select_math_radio_button:
                                    subject ="math";
                                    break;
                                case R.id.select_english_radio_button:
                                    subject ="english";
                                    break;
                                case R.id.select_physics_radio_button:
                                    subject ="physics";
                                    break;
                                case R.id.select_chemistry_radio_button:
                                    subject ="chemistry";
                                    break;
                                case R.id.select_politics_radio_button:
                                    subject ="politics";
                                    break;
                                case R.id.select_history_radio_button:
                                    subject ="history";
                                    break;
                                case R.id.select_geo_radio_button:
                                    subject ="geo";
                                    break;
                                case R.id.select_biology_radio_button:
                                    subject ="biology";
                                    break;
                                default:
                                    subject ="chinese";
                                    break;
                            }
                        }
                    });
                    // endregion

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

                        //配置ArrayAdapter适配器
                        ArrayAdapter<Entity> adapter=new ArrayAdapter<Entity>
                                (EntityLink.this,android.R.layout.simple_expandable_list_item_1,entityList);
                        resultsListView.setAdapter(adapter);
                        //设置选中选项监听
                        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Intent goToEntityDetailsPage = new Intent(EntityLink.this,EntityDetails.class);
                                startActivity(goToEntityDetailsPage);
                            }
                        });

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }


            }
        });
    }
}
