package com.example.renyanyu;

//package com.example.renyanyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class EntityDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_details);

        Button addToCollectionButton=findViewById(R.id.addToCollectionButton);
        Button hadAddedToCollectionButton=findViewById(R.id.hadAddedToCollectionButton);
        hadAddedToCollectionButton.setVisibility(View.GONE);

        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String type=intent.getStringExtra("type");
        String uri=intent.getStringExtra("uri");

        String severIP=EntityDetails.this.getString(R.string.backend_ip);
        SharedPreferences userInfo= EntityDetails.this.getSharedPreferences("user", 0);
        String userToken = userInfo.getString("token","");

        String haveStarredUrl =  severIP+ "/request/haveStarred";
        ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
        String message="token="+userToken+"&name="+name+"&type="+type+"&uri="+uri;
        System.out.println(message);
        String responseString = serverHttpResponse.postResponse(haveStarredUrl,message);
        System.out.println(responseString);
        if(responseString.equals("true"))
        {
            addToCollectionButton.setVisibility(View.GONE);
            hadAddedToCollectionButton.setVisibility(View.VISIBLE);
        }
        else if(responseString.equals("false"))
        {
            addToCollectionButton.setVisibility(View.VISIBLE);
            hadAddedToCollectionButton.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(EntityDetails.this,"www,好像断网了，请检查您的网络设置",Toast.LENGTH_LONG).show();
        }

        String addToHistoryUrl =  severIP+ "/request/addToHistory";
        message="token="+userToken+"&name="+name+"&type="+type+"&uri="+uri;
        System.out.println(message);
        responseString = serverHttpResponse.postResponse(addToHistoryUrl,message);
        System.out.println(responseString);


        // region 实体详情
        ListView listView=(ListView)findViewById(R.id.ListView1);
        final String[] str ={"属性名\n属性值","属性名\n属性值","属性名\n属性值"};
        //配置ArrayAdapter适配器
        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (EntityDetails.this,android.R.layout.simple_expandable_list_item_1,str);
        listView.setAdapter(adapter);
        //设置选中选项监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            }
        });
        // endregion

        Button shareButton=findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;



        addToCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EntityDetails.this,"已收藏",Toast.LENGTH_LONG).show();
                addToCollectionButton.setVisibility(View.GONE);
                hadAddedToCollectionButton.setVisibility(View.VISIBLE);

                String url = severIP + "/request/star";
                ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                String message="token="+userToken+"&name="+name+"&type="+type+"&uri="+uri;
                System.out.println(message);
                String responseString = serverHttpResponse.postResponse(url,message);
                System.out.println(responseString);
            }
        }) ;

        hadAddedToCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EntityDetails.this,"已取消收藏",Toast.LENGTH_LONG).show();
                addToCollectionButton.setVisibility(View.VISIBLE);
                hadAddedToCollectionButton.setVisibility(View.GONE);

                String url = severIP + "/request/star";
                ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                String message="token="+userToken+"&name="+name+"&type="+type+"&uri="+uri;
                System.out.println(message);
                String responseString = serverHttpResponse.postResponse(url,message);
                System.out.println(responseString);
            }
        }) ;

    }
}