package com.example.renyanyu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renyanyu.ui.login.*;

//import com.example.renyanyu.data.Result;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class BlankFragment3 extends Fragment {

    public BlankFragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank_fragment3, container, false);
        NavigationView navigationView=(NavigationView)view.findViewById(R.id.view_my_info);
        //navigationView的header部分
        View headerView = navigationView.getHeaderView(0);
        TextView userNameText=(TextView) headerView .findViewById(R.id.user_name_text);
        TextView displayNameText=(TextView) headerView .findViewById(R.id.displayNameText);
        Button loginButton = headerView.findViewById(R.id.loginButton);

        SharedPreferences userInfo= getActivity().getSharedPreferences("user", 0);
        String userName = userInfo.getString("name","");					//获取用户名
        if(userName.equals(""))
        {
            userNameText.setVisibility(View.GONE);
            displayNameText.setVisibility(View.GONE);
            loginButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    Intent goToLoginPage = new Intent(getActivity(),LoginActivity.class);
                    startActivity(goToLoginPage);
                }
            }) ;
        }
        else
        {
            loginButton.setVisibility(View.GONE);
        }

        //navigationView的itemList部分
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch (id)
                {
                    case R.id.loginButton:
                        Intent goToLoginPage = new Intent(getActivity(),LoginActivity.class);
                        startActivity(goToLoginPage);
                        break;
                    case R.id.item_favorite:
                        Intent goToCollectionPage = new Intent(getActivity(),Collection.class);
                        startActivity(goToCollectionPage);
                        break;
                    case R.id.item_history:
                        Intent goToHistoryPage = new Intent(getActivity(),History.class);
                        startActivity(goToHistoryPage);
                        break;
                    case R.id.item_logout:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
