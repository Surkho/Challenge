package com.wifibyteschallenge.android.model;

/**
 * Created by juanj on 13/03/2017.
 */

public class Posts {
    String title;
    String body;

    public Posts(){}

    public Posts(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
