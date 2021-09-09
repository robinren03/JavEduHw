package com.example.renyanyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.android.material.navigation.NavigationView;

public class ManageAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manage_account);

        NavigationView navigationView=(NavigationView)findViewById(R.id.manage_account_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch (id)
                {
                    case R.id.item_modify_display_name:
//                        Intent goToManageAccountPage = new Intent(ManageAccount.this,ManageAccount.class);
//                        startActivity(goToManageAccountPage);
                        break;
                    case R.id.item_modify_password:
//                        Intent goToCollectionPage = new Intent(ManageAccount.this,Collection.class);
//                        startActivity(goToCollectionPage);
                        break;

                    default:
                        break;
                }
                return false;
            }
        });


    }
}