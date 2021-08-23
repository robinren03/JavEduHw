package com.example.javeduhw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MyInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        NavigationView navigationView=(NavigationView)findViewById(R.id.view_my_info);
//        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //mDrawerLa
                int id=item.getItemId();
                switch (id)
                {
                    case R.id.item_favorite:
                        Intent goToCollectionPage = new Intent(MyInfo.this,Collection.class);
                        startActivity(goToCollectionPage);
                        break;
                    case R.id.item_history:
                        Intent goToHistoryPage = new Intent(MyInfo.this,History.class);
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
    }
}