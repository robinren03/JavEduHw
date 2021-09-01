package com.example.renyanyu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import android.widget.*;

//import com.example.javeduhw.databinding.FragmentFirstBinding;

public class BlankFragment1 extends Fragment {

    public BlankFragment1() {
        // Required empty public constructor
    }

    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    Button qa,search,channel_change;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Toast.makeText(getContext(),"点击了：",Toast.LENGTH_LONG).show();

        View view = inflater.inflate(R.layout.fragment_blank_fragment1, container, false);

        search=view.findViewById(R.id.sear);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),SearchPage.class);
                startActivity(intent);
            }
        });
        channel_change=(Button)view.findViewById(R.id.channel);
        channel_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),Channel.class);
                startActivityForResult(intent,0);
            }
        });
        /*
        qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(getActivity(),QA.class);
                //传输消息 todo
                startActivity(intent);
            }
        });*/

        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1&&requestCode==0){
            //Toast.makeText(getContext(), "!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getActivity(),MainActivity.class);
            startActivity(i);
        }
    }
}
