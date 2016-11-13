package com.example.matti.myapplication.io;

/**
 * Created by Davide on 12/11/2016.
 */

public class SimplePage {

    public String mTitle;
    public String mText;
    public int mIconId;
    public int mBackgroundId;

    public SimplePage(String title, String text, int iconId, int backgroundId) {
        this.mTitle = title;
        this.mText = text;
        this.mIconId = iconId;
        this.mBackgroundId = backgroundId;
    }
}
