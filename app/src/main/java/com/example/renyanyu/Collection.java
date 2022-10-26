package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Collection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collection);

        TextView guideInfoTextView=(TextView) findViewById(R.id.collection_page_guide_info);
        guideInfoTextView.setVisibility(View.GONE);
        ListView listView=(ListView)findViewById(R.id.collection_page_entity_list);

        // region 获取responseString
        String url = Collection.this.getString(R.string.backend_ip) + "/user/collection";
        SharedPreferences userInfo= Collection.this.getSharedPreferences("user", 0);
        String userToken = userInfo.getString("token","");
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken;
        url=url+"?"+message;
        String responseString = serverHttpResponse.getResponse(url);
        System.out.println(responseString);
        // endregion

        if(responseString==null)
        {
            guideInfoTextView.setText("www，好像断网了，请检查您的网络设置");
            guideInfoTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else if(responseString.equals("[]"))
        {
            guideInfoTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else
        {
            try
            {
                JSONArray collectionArray = new JSONArray(responseString);
                System.out.println(collectionArray.get(0));

                class Entity
                {
                    Entity(String _name,String _type, String _uri,String _course)
                    {
                        name=_name;
                        type=_type;
                        uri=_uri;
                        course = _course;
                    }
                    String name;
                    String type;
                    String uri;
                    String course;
                }

                ArrayList<Entity>entityList=new ArrayList<>();
                for(int i=0;i<collectionArray.length();i++)
                {
                    JSONObject entity_json=(JSONObject)collectionArray.get(i);
                    String name =entity_json.get("name").toString();
                    String type =entity_json.get("type").toString();
                    String uri = entity_json.get("uri").toString();
                    String course = entity_json.get("course").toString();
                    entityList.add(new Entity(name,type,uri,course));
                }

                final String[] entityAbstractInfoList =new String[entityList.size()];
                for(int i=0;i<entityList.size();i++)
                {
                    entityAbstractInfoList[i]="实体类型："+entityList.get(i).type+"\n实体名称："+entityList.get(i).name;
                }
                //配置ArrayAdapter适配器
                ArrayAdapter<String> adapter=new ArrayAdapter<String>
                        (Collection.this,android.R.layout.simple_expandable_list_item_1,entityAbstractInfoList);
                listView.setAdapter(adapter);
                //设置选中选项监听
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Intent goToEntityDetailsPage = new Intent(Collection.this,EntityDetails.class);
                        goToEntityDetailsPage.putExtra("entity_name",entityList.get(position).name);
                        goToEntityDetailsPage.putExtra("type",entityList.get(position).type);
                        goToEntityDetailsPage.putExtra("uri",entityList.get(position).uri);
                        goToEntityDetailsPage.putExtra("course",entityList.get(position).course);
                        startActivity(goToEntityDetailsPage);
                    }
                });

            }
            catch (JSONException e)
            {
                guideInfoTextView.setText("抱歉，服务器好像出了点问题，请稍后重试");
                guideInfoTextView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView guideInfoTextView=(TextView) findViewById(R.id.collection_page_guide_info);
        guideInfoTextView.setVisibility(View.GONE);
        ListView listView=(ListView)findViewById(R.id.collection_page_entity_list);

        // region 获取responseString
        String url = Collection.this.getString(R.string.backend_ip) + "/user/collection";
        SharedPreferences userInfo= Collection.this.getSharedPreferences("user", 0);
        String userToken = userInfo.getString("token","");
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken;
        url=url+"?"+message;
        String responseString = serverHttpResponse.getResponse(url);
        System.out.println(responseString);
        // endregion

        if(responseString==null)
        {
            guideInfoTextView.setText("www，好像断网了，请检查您的网络设置");
            guideInfoTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else if(responseString.equals("[]"))
        {
            guideInfoTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else
        {
            try
            {
                JSONArray collectionArray = new JSONArray(responseString);
                System.out.println(collectionArray.get(0));

                class Entity
                {
                    Entity(String _name,String _type, String _uri,String _course)
                    {
                        name=_name;
                        type=_type;
                        uri=_uri;
                        course = _course;
                    }
                    String name;
                    String type;
                    String uri;
                    String course;
                }

                ArrayList<Entity>entityList=new ArrayList<>();
                for(int i=0;i<collectionArray.length();i++)
                {
                    JSONObject entity_json=(JSONObject)collectionArray.get(i);
                    String name =entity_json.get("name").toString();
                    String type =entity_json.get("type").toString();
                    String uri = entity_json.get("uri").toString();
                    String course = entity_json.get("course").toString();
                    entityList.add(new Entity(name,type,uri,course));
                }

                final String[] entityAbstractInfoList =new String[entityList.size()];
                for(int i=0;i<entityList.size();i++)
                {
                    entityAbstractInfoList[i]="实体类型："+entityList.get(i).type+"\n实体名称："+entityList.get(i).name;
                }
                //配置ArrayAdapter适配器
                ArrayAdapter<String> adapter=new ArrayAdapter<String>
                        (Collection.this,android.R.layout.simple_expandable_list_item_1,entityAbstractInfoList);
                listView.setAdapter(adapter);
                //设置选中选项监听
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Intent goToEntityDetailsPage = new Intent(Collection.this,EntityDetails.class);
                        goToEntityDetailsPage.putExtra("entity_name",entityList.get(position).name);
                        goToEntityDetailsPage.putExtra("type",entityList.get(position).type);
                        goToEntityDetailsPage.putExtra("uri",entityList.get(position).uri);
                        goToEntityDetailsPage.putExtra("course",entityList.get(position).course);
                        startActivity(goToEntityDetailsPage);
                    }
                });

            }
            catch (JSONException e)
            {
                guideInfoTextView.setText("抱歉，服务器好像出了点问题，请稍后重试");
                guideInfoTextView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }
    }
}
