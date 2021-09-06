package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView guideInfoTextView=(TextView) findViewById(R.id.history_page_guide_info);
        guideInfoTextView.setVisibility(View.GONE);
        ListView listView=(ListView)findViewById(R.id.history_page_entity_list);

        // region 获取responseString
        String url = History.this.getString(R.string.backend_ip) + "/user/history";
        SharedPreferences userInfo= History.this.getSharedPreferences("user", 0);
        String userToken = userInfo.getString("token","");
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken;
        url=url+"?"+message;
        String responseString = serverHttpResponse.getResponse(url);
        System.out.println(responseString);
        // endregion

        if(responseString.equals("[]"))
        {
            guideInfoTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else {
            try
            {
                JSONArray collectionArray = new JSONArray(responseString);
                System.out.println(collectionArray.get(0));

                class Entity
                {
                    Entity(String _name,String _type, String _uri,String _time)
                    {
                        name=_name;
                        type=_type;
                        uri=_uri;
                        time=_time;
                    }
                    String name;
                    String type;
                    String uri;
                    String time;
                }

                ArrayList<Entity> entityList=new ArrayList<>();
                for(int i=0;i<collectionArray.length();i++)
                {
                    JSONObject entity_json=(JSONObject)collectionArray.get(i);
                    String name =entity_json.get("name").toString();
                    String type =entity_json.get("type").toString();
                    String uri = entity_json.get("uri").toString();
                    String time = entity_json.get("time").toString();
                    entityList.add(new Entity(name,type,uri,time));
                }

                final String[] entityAbstractInfoList =new String[entityList.size()];
                for(int i=0;i<entityList.size();i++)
                {
                    Entity entity=entityList.get(i);
                    entityAbstractInfoList[i]="实体类型："+entity.type
                            +"\n实体名称："+entity.name
                            +"\n浏览时间："+entity.time;
                }
                //配置ArrayAdapter适配器
                ArrayAdapter<String> adapter=new ArrayAdapter<String>
                        (History.this,android.R.layout.simple_expandable_list_item_1,entityAbstractInfoList);
                listView.setAdapter(adapter);
                //设置选中选项监听
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Intent goToEntityDetailsPage = new Intent(History.this,EntityDetails.class);
                        goToEntityDetailsPage.putExtra("name",entityList.get(position).name);
                        goToEntityDetailsPage.putExtra("type",entityList.get(position).type);
                        goToEntityDetailsPage.putExtra("uri",entityList.get(position).uri);
                        startActivity(goToEntityDetailsPage);
                    }
                });

            }
            catch (JSONException e)
            {
                guideInfoTextView.setText("www，好像断网了，请检查您的网络设置");
                guideInfoTextView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }

    }
}
