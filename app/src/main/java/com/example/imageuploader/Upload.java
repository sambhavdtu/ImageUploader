package com.example.imageuploader;

import android.widget.ImageView;

import com.google.firebase.database.Exclude;

public class Upload {

    private String mName;
    private String mUri;
    private String mKey;

    public Upload() {

    }

    public Upload(String name, String uri) {

        if (name.trim().equals(""))
            name = "No name";

        this.mUri = uri;
        this.mName = name;
    }

    public String getUri() {
        return mUri;
    }

    public String getName() {
        return mName;
    }

    public void setUri(String uri) {
        this.mUri = uri;
    }

    public void setName(String name) {
        this.mName = name;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String mKey) {
        this.mKey = mKey;
    }
}
