package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Collection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        TextView guideInfoTextView=(TextView) findViewById(R.id.collection_page_guide_info);
        guideInfoTextView.setVisibility(View.GONE);

        // region 获取resultsArray
        String url = Collection.this.getString(R.string.backend_ip) + "/user/collection";
        SharedPreferences userInfo= Collection.this.getSharedPreferences("user", 0);
        String userToken = userInfo.getString("token","");
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken;
        url=url+"?"+message;
        String responseString = serverHttpResponse.getResponse(url);
        System.out.println(responseString);
        if(responseString==null)
        {
            guideInfoTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            try {
                JSONArray collectionArray = new JSONArray(responseString);
                System.out.println(collectionArray.get(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        JSONObject answer_json = new JSONObject(responseString);
//        JSONArray resultsArray = (JSONArray) ((JSONObject) answer_json.get("data")).get("results");
        // endregion

        ListView listView=(ListView)findViewById(R.id.collection_page_entity_list);
        final String[] str ={"实体类型\n实体名称","实体类型\n实体名称","实体类型\n实体名称"};
        //配置ArrayAdapter适配器
        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (Collection.this,android.R.layout.simple_expandable_list_item_1,str);
        listView.setAdapter(adapter);
        //设置选中选项监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent goToEntityDetailsPage = new Intent(Collection.this,EntityDetails.class);
                startActivity(goToEntityDetailsPage);
            }
        });
    }
}
