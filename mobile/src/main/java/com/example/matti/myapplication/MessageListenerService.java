package com.example.matti.myapplication;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import de.greenrobot.event.EventBus;

public class MessageListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {

        //Log.d("messaggio inviato","");

        /*String payload = messageEvent.getPath();
        if(payload.getBytes()[0] == 1 || payload.getBytes()[0] == 2) {
            Log.d("Aaaa","");
            EventBus.getDefault().post(payload.getBytes());
        }
        else
        {
            Log.d("DDDDDDDD","");
            EventBus.getDefault().post(messageEvent.getPath());
        }*/
        EventBus.getDefault().post(messageEvent.getPath());

//        Handler mHandler = new Handler(getMainLooper());
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        });
//




    }



    /*private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }*/
}