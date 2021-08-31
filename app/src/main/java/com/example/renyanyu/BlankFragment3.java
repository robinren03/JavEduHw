package com.example.renyanyu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;

public class BlankFragment3 extends Fragment {

    public BlankFragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank_fragment3, container, false);
        NavigationView navigationView=(NavigationView)view.findViewById(R.id.view_my_info);
//        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //mDrawerLa
                int id=item.getItemId();
                switch (id)
                {
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
}
