package com.technologies.tuch.tuch;

public class DialogItem {

    String text;
    String time;
    String author_id;

    DialogItem(String tx, String tm, String ai) {
        this.text = tx;
        this.time = tm;
        this.author_id = ai;
    }

    public String getMessageText() {
        return text;
    }
    public void setMessageText(String text) {
        this.text = text;
    }
    public String getMessageTime() {
        return time;
    }
    public void setMessageTime(String time) {
        this.time = time;
    }
    public String getAuthorId() {
        return author_id;
    }
    public void setAuthorId(String author_id) {
        this.author_id = author_id;
    }
}
