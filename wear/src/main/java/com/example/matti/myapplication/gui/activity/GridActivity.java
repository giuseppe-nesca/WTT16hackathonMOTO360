package com.example.matti.myapplication.gui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matti.myapplication.R;
import com.example.matti.myapplication.gui.adapter.MyViewPagerAdapter;
import com.example.matti.myapplication.gui.fragment.CustomFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * Created by Davide on 12/11/2016.
 */

public class GridActivity extends WearableActivity {

    private static final long CONNECTION_TIME_OUT_MS = 1000;
    private static final String MESSAGE = "Hello Wear!";
    private GoogleApiClient client;
    private String nodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        initApi();

        //EventBus.getDefault().register(this);

        final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        setAmbientEnabled();
       // setSubscribeWindowEvents(false);
    }

    private void setupViewPager(ViewPager viewPager){
        List<String> text = new ArrayList<>();
        text.add("Monitoring");
        text.add("Gesture");
        text.add("Gesture 2");
        MyViewPagerAdapter mAdapter = new MyViewPagerAdapter(getFragmentManager(), text);
        CustomFragment mScenarioFragment = new CustomFragment();
        CustomFragment mMonitoringFragment = new CustomFragment();

        viewPager.setAdapter(mAdapter);
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
    public void sendMessageToPhone(final String message) {
        if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(client, nodeId, message, null);
                    client.disconnect();
                }
            }).start();
        }
        else
            Toast.makeText(getApplicationContext(), "No node here!", Toast.LENGTH_SHORT).show();

    }


    @Override /* KeyEvent.Callback, called when a gesture is detected by android wear os */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_NAVIGATE_NEXT:
                sendMessageToPhone("");
                Toast.makeText(getApplicationContext(), "NAVIGATE_NEXT!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS:
                sendMessageToPhone("");
                Toast.makeText(getApplicationContext(), "NAVIGATE_PREV!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_IN:
                sendMessageToPhone("");
                Toast.makeText(getApplicationContext(), "NAVIGATE_IN!", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_OUT:
                sendMessageToPhone("");
                Toast.makeText(getApplicationContext(), "NAVIGATE_OUT!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                sendMessageToPhone("");
                Toast.makeText(getApplicationContext(), "ANYTHING ELSE!", Toast.LENGTH_SHORT).show();
                return super.onKeyDown(keyCode, event);
        }
    }

    /*public void onEvent(String message){
        sendMessageToPhone(message);
    }
    */
}
