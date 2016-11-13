package com.example.matti.myapplication;

import android.os.Bundle;
import android.content.Context;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.view.KeyEvent;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.util.concurrent.TimeUnit;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    private static final long CONNECTION_TIME_OUT_MS = 1000;
    private static final String MESSAGE = "Hello Wear!";
    private GoogleApiClient client;
    private String nodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setAmbientEnabled(); enable if ambient mode is needed

        initApi();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);
        updateDisplay(); // comment this if ambient mode is needed
    }

    private void initApi() {
        Log.d("###à","1");
        client = getGoogleApiClient(this);
        Log.d("###à","2");
        retrieveDeviceNode();
    }


    /**
     * Returns a GoogleApiClient that can access the Wear API.
     * @param context
     * @return A GoogleApiClient that can make calls to the Wear API
     */
    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    /**
     * Connects to the GoogleApiClient and retrieves the connected device's Node ID. If there are
     * multiple connected devices, the first Node ID is returned.
     */
    private void retrieveDeviceNode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("###à","aaa");
                ConnectionResult cr = client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                Log.d("#############", cr.toString());
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                Log.d("#############", result.toString());
                List<Node> nodes = result.getNodes();
                Log.d("#############","" + nodes.size());
                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                }
                client.disconnect();
            }
        }).start();
    }

    /**
     * Sends a message to the connected mobile device, telling it to show a Toast.
     */
    private void sendToast() {
        if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(client, nodeId, MESSAGE, null);
                    client.disconnect();
                }
            }).start();
        }
        else
            Toast.makeText(getApplicationContext(), "No node here!", Toast.LENGTH_SHORT).show();

    }

    /*  enable if ambient mode is on
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }
    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }
    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }
    */
    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }

    @Override /* KeyEvent.Callback, called when a gesture is detected by android wear os */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_NAVIGATE_NEXT:
                sendToast();
                Toast.makeText(getApplicationContext(), "NAVIGATE_NEXT!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS:
                sendToast();
                Toast.makeText(getApplicationContext(), "NAVIGATE_PREV!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_IN:
                sendToast();
                Toast.makeText(getApplicationContext(), "NAVIGATE_IN!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_OUT:
                sendToast();
                Toast.makeText(getApplicationContext(), "NAVIGATE_OUT!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                sendToast();
                Toast.makeText(getApplicationContext(), "ANYTHING ELSE!", Toast.LENGTH_SHORT).show();
                return super.onKeyDown(keyCode, event);
        }
    }
}








//old one
/*
package com.example.matti.myapplication;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.TextView;
import android.view.KeyEvent;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setAmbientEnabled(); enable if ambient mode is needed

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);
        updateDisplay(); // comment this if ambient mode is needed
    }


    */
/*  enable if ambient mode is on
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }
    *//*

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }

    @Override */
/* KeyEvent.Callback, called when a gesture is detected by android wear os *//*

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_NAVIGATE_NEXT:
                Toast.makeText(getApplicationContext(), "NAVIGATE_NEXT!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS:
                Toast.makeText(getApplicationContext(), "NAVIGATE_PREV!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_IN:
                Toast.makeText(getApplicationContext(), "NAVIGATE_IN!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_OUT:
                Toast.makeText(getApplicationContext(), "NAVIGATE_OUT!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                Toast.makeText(getApplicationContext(), "ANYTHING ELSE!", Toast.LENGTH_SHORT).show();
                return super.onKeyDown(keyCode, event);
        }
    }
}
*/
