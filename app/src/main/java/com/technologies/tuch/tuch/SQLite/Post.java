package com.technologies.tuch.tuch.SQLite;

/**
 * Created by Stas on 18.01.2017.
 */
public class Post {

    private String id;
    private String authorId;
    private String authorName;
    private String text;
    private String dateCreate;

    public Post(String id, String authorId, String authorName, String text, String dateCreate) {
        this.id =id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.text =text;
        this.dateCreate = dateCreate;
    }

    public String getId() {
        return this.id;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public String getText() {
        return this.text;
    }

    public String getDateCreate() {
        return this.dateCreate;
    }

    public void setId(String data) {
        this.id = data;
    }

    public void setAuthorId(String data) {
        this.authorId = data;
    }

    public void setAuthorName(String data) {
        this.authorName = data;
    }

    public void setText(String data) {
        this.text = data;
    }

    public void setDateCreate(String data) {
        this.dateCreate = data;
    }
}
