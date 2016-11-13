package com.example.matti.myapplication.gui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matti.myapplication.R;
import com.example.matti.myapplication.listener.FragmentListener;


/**
 * Created by Davide on 12/11/2016.
 */

public class CustomFragment extends Fragment implements
        DelayedConfirmationView.DelayedConfirmationListener {

    String myText;
    private int position;
    private DelayedConfirmationView mDelayedView;

    public CustomFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            switch (position) {
                case 0: {
                    View view = inflater.inflate(R.layout.custom_fragment3, container, false);

                    TextView textView;
                    textView = (TextView) view.findViewById(R.id.text_fragment);
                    textView.setText(myText);
                    return view;
                }
                case 1: {
                    View view = inflater.inflate(R.layout.custom_fragment, container, false);

                    mDelayedView = (DelayedConfirmationView) view.findViewById(R.id.delayed_confirm);
                    ImageView imageView;
                    imageView = (ImageView) view.findViewById(R.id.image_fragment);

                    imageView.setImageDrawable(getActivity().getDrawable(R.drawable.gesture));
                    FragmentListener listener;
                    listener = new FragmentListener(getActivity().getApplicationContext(), position, getActivity());
                    imageView.setOnClickListener(listener);
                    imageView.setOnLongClickListener(listener);
                    return view;
                }
                case 2: {
                    View view = inflater.inflate(R.layout.custom_fragment2, container, false);

                    mDelayedView = (DelayedConfirmationView) view.findViewById(R.id.delayed_confirm2);
                    ImageView imageView;
                    imageView = (ImageView) view.findViewById(R.id.image_fragment2);
                    imageView.setImageDrawable(getActivity().getDrawable(R.drawable.gesture2));
                    FragmentListener listener;
                    listener = new FragmentListener(getActivity().getApplicationContext(), position, getActivity());
                    imageView.setOnClickListener(listener);
                    imageView.setOnLongClickListener(listener);
                    return view;
                }
            }

        return null;
    }

    public void setMyText(String text){
        myText = text;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.custom_fragment);

        //mDelayedView.setListener(this);
    }

    @Override
    public void onTimerFinished(View view) {
        // User didn't cancel, perform the action
    }

    @Override
    public void onTimerSelected(View view) {
        // User canceled, abort the action
    }


}
