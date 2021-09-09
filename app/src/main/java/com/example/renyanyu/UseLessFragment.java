package com.example.renyanyu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class UseLessFragment extends Fragment{
    LocalBroadcastManager mLocalBroadcastManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        System.out.println("Ijdkfs");
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        View rootView = inflater.inflate(R.layout.useless_fragment, container, false);
        Button continueButton = (Button) rootView.findViewById(R.id.useless_page_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mLocalBroadcastManager.sendBroadcast(new Intent("goToPrevQuestion"));
            }
        });
        Button waitToSubmitButton = (Button) rootView.findViewById(R.id.useless_page_wait_to_submit_button);
        waitToSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mLocalBroadcastManager.sendBroadcast(new Intent("goToNextQuestion"));
            }
        });

        return rootView;
    }


}