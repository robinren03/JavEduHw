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

        Intent intent=getIntent();
        String uri=intent.getStringExtra("uri");

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
//                Intent goToHistoryPage = new Intent(History.this,History.class);
//                startActivity(goToHistoryPage);
            }
        });
        Button shareButton=findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;
        Button addToCollectionButton=findViewById(R.id.addToCollectionButton);
        Button hadAddedToCollectionButton=findViewById(R.id.hadAddedToCollectionButton);
        hadAddedToCollectionButton.setVisibility(View.GONE);

        addToCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EntityDetails.this,"已收藏",Toast.LENGTH_LONG).show();
                addToCollectionButton.setVisibility(View.GONE);
                hadAddedToCollectionButton.setVisibility(View.VISIBLE);

                String url = EntityDetails.this.getString(R.string.backend_ip) + "/request/star";
                SharedPreferences userInfo= EntityDetails.this.getSharedPreferences("user", 0);
                String userToken = userInfo.getString("token","");
                ServerHttpResponse serverHttpResponse=ServerHttpResponse.getServerHttpResponse();
                String message="course=chinese&"+"name="+uri+"&token="+userToken;
                System.out.println(message);
//                url=url+"?"+message;
                String responseString = serverHttpResponse.postResponse(url,message);
                System.out.println(responseString);


            }
        }) ;

        hadAddedToCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EntityDetails.this,"已取消收藏",Toast.LENGTH_LONG).show();
//                addToCollectionButton.setPointerIcon(R.id.shareButton);
                //  shareButton.setIm
                addToCollectionButton.setVisibility(View.VISIBLE);
                hadAddedToCollectionButton.setVisibility(View.GONE);
            }
        }) ;

    }
}