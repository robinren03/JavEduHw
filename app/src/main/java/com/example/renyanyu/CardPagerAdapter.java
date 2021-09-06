package com.example.renyanyu;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<Exercise> mData;
    private float mBaseElevation;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(Exercise item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.exercise_card, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(Exercise item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        titleTextView.setText(item.stem);
        TextView A_t=(TextView) view.findViewById(R.id.Atxt);
        TextView B_t=(TextView) view.findViewById(R.id.Btxt);
        TextView C_t=(TextView) view.findViewById(R.id.Ctxt);
        TextView D_t=(TextView) view.findViewById(R.id.Dtxt);
        TextView answer=(TextView) view.findViewById(R.id.answer);
        A_t.setText(item.answer_a);
        B_t.setText(item.answer_b);
        C_t.setText(item.answer_c);
        D_t.setText(item.answer_d);
        answer.setText(item.answer);
        answer.setVisibility(View.GONE);
        if(item.answer_a.length()==0){
            LinearLayout A=(LinearLayout) view.findViewById(R.id.linA);
            LinearLayout B=(LinearLayout) view.findViewById(R.id.linB);
            LinearLayout C=(LinearLayout) view.findViewById(R.id.linC);
            LinearLayout D=(LinearLayout) view.findViewById(R.id.linD);
            A.setVisibility(View.GONE);
            B.setVisibility(View.GONE);
            C.setVisibility(View.GONE);
            D.setVisibility(View.GONE);
        }
        Button daan=(Button) view.findViewById(R.id.Daan);
        daan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer.setVisibility(View.VISIBLE);
            }
        });
    }

}

