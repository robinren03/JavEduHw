package com.example.renyanyu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
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

    static int hadLogin=0;
    static int refresh=0;
    public BlankFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank_fragment3, container, false);

        //得到navigationView
        NavigationView navigationView=(NavigationView)view.findViewById(R.id.view_my_info);

        //navigationView的header部分
        View headerView = navigationView.getHeaderView(0);
        TextView userNameText=(TextView) headerView .findViewById(R.id.user_name_text);
        TextView displayNameText=(TextView) headerView .findViewById(R.id.displayNameText);
        Button loginButton = headerView.findViewById(R.id.loginButton);

        SharedPreferences userInfo= getActivity().getSharedPreferences("user", 0);
        String userName = userInfo.getString("username","");
        if(userName.equals(""))
        {
            userNameText.setVisibility(View.GONE);
            displayNameText.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            Menu menuItemListView=navigationView.getMenu();
            MenuItem favoriteItem=menuItemListView.findItem(R.id.item_favorite);
            MenuItem logoutItem=menuItemListView.findItem(R.id.item_logout);
            favoriteItem.setVisible(false);
            logoutItem.setVisible(false);
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
            userNameText.setVisibility(View.VISIBLE);
            displayNameText.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            Menu menuItemListView=navigationView.getMenu();
            MenuItem favoriteItem=menuItemListView.findItem(R.id.item_favorite);
            MenuItem logoutItem=menuItemListView.findItem(R.id.item_logout);
            favoriteItem.setVisible(true);
            logoutItem.setVisible(true);
            String displayName = userInfo.getString("displayName","");
            userNameText.setText(userName);
            displayNameText.setText(displayName);
        }

        //navigationView的itemList部分
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch (id)
                {
                    case R.id.item_account:
                        Intent goToManageAccountPage = new Intent(getActivity(),ManageAccount.class);
                        startActivity(goToManageAccountPage);
                        break;
                    case R.id.item_favorite:
                        Intent goToCollectionPage = new Intent(getActivity(),Collection.class);
                        startActivity(goToCollectionPage);
                        break;
                    case R.id.item_history:
                        System.out.println("goToHistoryPage");
                        Intent goToHistoryPage = new Intent(getActivity(),History.class);
                        startActivity(goToHistoryPage);
                        break;
                    case R.id.item_wrong_exercise_set:
                        Intent goToWrongExerciseSetPage = new Intent(getActivity(),WrongExerciseSet.class);
                        startActivity(goToWrongExerciseSetPage);
                        break;
                    case R.id.item_do_exam:
                        Intent goToDoExamPage = new Intent(getActivity(), Quiz.class);
                        startActivity(goToDoExamPage);
                        break;
                    case R.id.item_logout:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                        dialog.setTitle("退出登录");//设置标题
                        dialog.setMessage("确定要退出登录吗？");//设置信息具体内容
                        dialog.setCancelable(false);//设置是否可取消
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override//确认退出登录
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                //删除SharedPreferences中储存的用户名等信息
                                SharedPreferences userInfo = getActivity().getSharedPreferences("user", 0);
                                SharedPreferences.Editor editor = userInfo.edit();
                                editor.clear();
                                editor.apply();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.my_info_fragment, new BlankFragment3(), null)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消退出，就什么也没有发生
                            }
                        });
                        dialog.show();
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


        //得到navigationView
        NavigationView navigationView=(NavigationView)getActivity().findViewById(R.id.view_my_info);

        //navigationView的header部分
        View headerView = navigationView.getHeaderView(0);
        TextView userNameText=(TextView) headerView .findViewById(R.id.user_name_text);
        TextView displayNameText=(TextView) headerView .findViewById(R.id.displayNameText);
        Button loginButton = headerView.findViewById(R.id.loginButton);

        SharedPreferences userInfo= getActivity().getSharedPreferences("user", 0);
        String userName = userInfo.getString("username","");
        if(userName.equals(""))
        {
            if(hadLogin==1)
            {
                refresh=1;
            }
            else
            {
                refresh=0;
            }
            hadLogin=0;
            userNameText.setVisibility(View.GONE);
            displayNameText.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            Menu menuItemListView=navigationView.getMenu();
            MenuItem favoriteItem=menuItemListView.findItem(R.id.item_favorite);
            MenuItem logoutItem=menuItemListView.findItem(R.id.item_logout);
            favoriteItem.setVisible(false);
            logoutItem.setVisible(false);
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
            if(hadLogin==1)
            {
                refresh=0;
            }
            else
            {
                refresh=1;
            }
            hadLogin=1;
            userNameText.setVisibility(View.VISIBLE);
            displayNameText.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            Menu menuItemListView=navigationView.getMenu();
            MenuItem favoriteItem=menuItemListView.findItem(R.id.item_favorite);
            MenuItem logoutItem=menuItemListView.findItem(R.id.item_logout);
            favoriteItem.setVisible(true);
            logoutItem.setVisible(true);
            String displayName = userInfo.getString("displayName","");
            userNameText.setText(userName);
            displayNameText.setText(displayName);
        }

        //navigationView的itemList部分
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch (id)
                {
                    case R.id.item_favorite:
                        Intent goToCollectionPage = new Intent(getActivity(),Collection.class);
                        startActivity(goToCollectionPage);
                        break;
                    case R.id.item_history:
                        System.out.println("goToHistoryPage");
                        Intent goToHistoryPage = new Intent(getActivity(),History.class);
                        startActivity(goToHistoryPage);
                        break;
                    case R.id.item_logout:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                        dialog.setTitle("退出登录");//设置标题
                        dialog.setMessage("确定要退出登录吗？");//设置信息具体内容
                        dialog.setCancelable(false);//设置是否可取消
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override//确认退出登录
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                //删除SharedPreferences中储存的用户名等信息
                                SharedPreferences userInfo = getActivity().getSharedPreferences("user", 0);
                                SharedPreferences.Editor editor = userInfo.edit();
                                editor.clear();
                                editor.apply();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.my_info_fragment, new BlankFragment2(), null)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消退出，就什么也没有发生
                            }
                        });
                        dialog.show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        if(refresh==1)
        {
            refresh=0;
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.my_info_fragment, new BlankFragment3(), null)
                    .addToBackStack(null)
                    .commit();
        }

    }
}
