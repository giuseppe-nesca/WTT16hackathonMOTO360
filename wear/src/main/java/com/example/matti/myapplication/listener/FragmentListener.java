package com.example.matti.myapplication.listener;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.wearable.view.DelayedConfirmationView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matti.myapplication.R;
import com.example.matti.myapplication.gui.activity.GridActivity;
import com.example.matti.myapplication.io.Constants;


/**
 * Created by Davide on 13/11/2016.
 */

public class FragmentListener implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private int myPosition;
    private Activity mActivity;

    public FragmentListener(Context context, int position, Activity activity){
        mContext = context;
        myPosition = position;
        mActivity = activity;
    }

    @Override
    public boolean onLongClick(View view) {
        if(myPosition == 1) {
            Toast.makeText(mContext, "Azione predefinita: CHIAMA " + Constants.NUMBER, Toast.LENGTH_LONG).show();
        }else if(myPosition == 2){
            Toast.makeText(mContext, "Azione predefinita: INVIA MESSAGGIO A " + Constants.NUMBER, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        //View view1 = mActivity.getLayoutInflater().inflate(R.layout.delayed_layout, null);
        View view1;
        TextView textView;
        final ImageView imageView;
        final DelayedConfirmationView mDelayedView;

        if(myPosition == 1){
            view1 = view.getRootView();
            imageView = (ImageView) view1.findViewById(R.id.image_fragment);

            mDelayedView = (DelayedConfirmationView) view1.findViewById(R.id.delayed_confirm);
            final TextView countdownView = (TextView)view1.findViewById(R.id.textdelayed);
            CountDownTimer timer = new CountDownTimer(11000, 1000) {
                int newCountDown = 10;

                public void onTick(long millisUntilFinished) {
                    newCountDown = newCountDown -1;

                    countdownView.setText(String.valueOf(newCountDown));
                }

                public void onFinish() {
                    countdownView.setVisibility(View.GONE);
                    mDelayedView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                    ((GridActivity) mActivity).sendMessageToPhone("svenimento");
                }

            }.start();

            TextViewListener textViewListener = new TextViewListener(mContext, myPosition, timer);
            countdownView.setOnClickListener(textViewListener);

            Log.d("PREMUTO", "PREMUTO");

            imageView.setVisibility(View.GONE);
//        textView.setVisibility(View.GONE);

            countdownView.setVisibility(View.VISIBLE);
            final int countdown = 10;
            countdownView.setText(String.valueOf(countdown));

            mDelayedView.setVisibility(View.VISIBLE);

            // Two seconds to cancel the action
            mDelayedView.setTotalTimeMs(10000);
            // Start the timer
            mDelayedView.start();

        }else{
            view1 = view.getRootView();
            imageView = (ImageView) view1.findViewById(R.id.image_fragment2);

            mDelayedView = (DelayedConfirmationView) view1.findViewById(R.id.delayed_confirm2);
            Log.d("PREMUTO", "PREMUTO");

            final TextView countdownView = (TextView)view1.findViewById(R.id.textdelayed2);

            CountDownTimer timer = new CountDownTimer(11000, 1000) {
                int newCountDown = 10;

                public void onTick(long millisUntilFinished) {
                    newCountDown = newCountDown -1;

                    countdownView.setText(String.valueOf(newCountDown));
                }

                public void onFinish() {
                    countdownView.setVisibility(View.GONE);
                    mDelayedView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                    ((GridActivity) mActivity).sendMessageToPhone("caduta");
                }

            }.start();

            TextViewListener textViewListener = new TextViewListener(mContext, myPosition, timer);
            countdownView.setOnClickListener(textViewListener);

            imageView.setVisibility(View.GONE);

            countdownView.setVisibility(View.VISIBLE);
            final int countdown = 10;
            countdownView.setText(String.valueOf(countdown));

            mDelayedView.setVisibility(View.VISIBLE);

            // Two seconds to cancel the action
            mDelayedView.setTotalTimeMs(10000);
            // Start the timer
            mDelayedView.start();

        }

    }
}
