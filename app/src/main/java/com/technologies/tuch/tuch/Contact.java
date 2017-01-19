package com.technologies.tuch.tuch;

/**
 * Created by StasJDM on 19.01.2017.
 */
public class Contact {

    private String contact_1_id;
    private String contact_2_id;
    private String contact_1_name;
    private String contact_2_name;
    private String friend_id;
    private String friend_name;
    private String type;

    public String getContact_1_id() {
        return this.contact_1_id;
    }

    public String getContact_2_id() {
        return this.contact_2_id;
    }

    public String getContact_1_name() {
        return this.contact_1_name;
    }

    public String getContact_2_name() {
        return this.contact_2_name;
    }

    public String getFriend_id() {
        return this.friend_id;
    }

    public String getFriend_name() {
        return this.friend_name;
    }

    public String getType() {
        return this.type;
    }

    public void setContact_1_id(String data) {
        this.contact_1_id = data;
    }

    public void setContact_2_id(String data) {
        this.contact_2_id = data;
    }

    public void setContact_1_name(String data) {
        this.contact_1_name = data;
    }

    public void setContact_2_name(String data) {
        this.contact_2_name = data;
    }

    public void setFriend_id(String data) {
        this.friend_id = data;
    }

    public void setFriend_name(String data) {
        this.friend_name = data;
    }

    public void setType(String data) {
        this.type = data;
    }
}
