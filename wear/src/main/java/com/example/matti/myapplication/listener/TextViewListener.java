package com.example.matti.myapplication.listener;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matti.myapplication.R;


/**
 * Created by Davide on 13/11/2016.
 */

public class TextViewListener implements View.OnClickListener {

    private Context mContext;
    private int myPosition;
    private CountDownTimer mCountDownTimer;

    public TextViewListener(Context context, int position, CountDownTimer timer){
        mContext = context;
        myPosition = position;
        mCountDownTimer = timer;
    }

    @Override
    public void onClick(View view) {
        mCountDownTimer.cancel();
        View rootView = view.getRootView();
        ImageView imageView;
        TextView countdownView;
        DelayedConfirmationView mDelayedView;
        if(myPosition == 1) {
            imageView = (ImageView) rootView.findViewById(R.id.image_fragment);

            mDelayedView = (DelayedConfirmationView) rootView.findViewById(R.id.delayed_confirm);
            countdownView = (TextView) rootView.findViewById(R.id.textdelayed);
        }else{
            imageView = (ImageView) rootView.findViewById(R.id.image_fragment2);

            mDelayedView = (DelayedConfirmationView) rootView.findViewById(R.id.delayed_confirm2);
            countdownView = (TextView) rootView.findViewById(R.id.textdelayed2);
        }

        imageView.setVisibility(View.VISIBLE);
        mDelayedView.setVisibility(View.GONE);
        countdownView.setText("10");
        countdownView.setVisibility(View.GONE);

    }
}
