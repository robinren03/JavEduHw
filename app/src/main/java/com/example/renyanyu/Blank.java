package com.example.renyanyu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Blank extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);
        System.out.println("enter!!!!!!!!!!!!!");
        TextView b=findViewById(R.id.txtww);
        Intent t=new Intent(Blank.this,EntityDetails.class);
        Intent t1=getIntent();
        String course=t1.getStringExtra("course");
        String type=t1.getStringExtra("type");
        String entity_name=t1.getStringExtra("entity_name");
        String kuri=t1.getStringExtra("uri");
        System.out.println(course+" "+type+" "+entity_name+" "+kuri+" ");
        b.setText(entity_name);
        t.putExtra("course",course);
        t.putExtra("type",type);
        t.putExtra("entity_name",entity_name);
        t.putExtra("uri",kuri);
        startActivity(t);

        finish();
    }
}
