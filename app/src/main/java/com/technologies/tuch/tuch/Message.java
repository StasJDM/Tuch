package com.technologies.tuch.tuch;

public class Message {

    private String id;
    private String authorId;
    private String clientId;
    private String type;
    private String name;
    private String text;
    private String dateTime;
    private String quantity;
    private String usersId;
    //private String userName;
    //private String userSurname;
    private String userNameSurname;
    private String authorName;
    private String clientName;


    public Message(String id, String authorId, String clientId, String type, String name, String text, String dateTime, String quantity, String usersId, String userNameSurname, String authorName, String clientName) {
        this.id = id;
        this.authorId = authorId;
        this.clientId = clientId;
        this.type = type;
        this.name = name;
        this.text = text;
        this.dateTime = dateTime;
        this.quantity = quantity;
        this.usersId = usersId;
        this.userNameSurname = userNameSurname;
        this.authorName = authorName;
        this.clientName = clientName;
    }

    public Message(String id, String authorId, String clientId, String type, String name, String text, String dateTime, String authorName, String clientName) {
        this.id = id;
        this.authorId = authorId;
        this.clientId = clientId;
        this.type = type;
        this.name = name;
        this.text = text;
        this.dateTime = dateTime;
        this.authorName = authorName;
        this.clientName = clientName;
    }

    public Message(String usersId, String userNameSurname, String text, String quantity, String dateTime) {
        this.usersId = usersId;
        this.userNameSurname = userNameSurname;
        this.text = text;
        this.quantity =quantity;
        this.dateTime = dateTime;
    }

    //GETTING
    public String getId() {
        return this.id;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getText() {
        return this.text;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public String getUsersId() {
        return this.usersId;
    }

	/* public String getUserName() {
		return this.userName;
	} */

	/* public String getUserSurname() {
		return this.userSurname;
	} */

    public String getUserNameSurname() {
        return this.userNameSurname;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public String getClientName() {
        return this.clientName;
    }


    //SETTING

    public void setId(String data) {
        this.id = data;
    }

    public void setAuthorId(String data) {
        this.authorId = data;
    }

    public void setClientId(String data) {
        this.clientId = data;
    }

    public void setType(String data) {
        this.type = data;
    }

    public void setName(String data) {
        this.name = data;
    }

    public void setText(String data) {
        this.text = data;
    }

    public void setDateTime(String data) {
        this.dateTime = data;
    }

    public void setQuantity(String data) {
        this.quantity = data;
    }

    public void setUsersId(String data) {
        this.usersId = data;
    }

	/* public void setUserName(String data) {
		this.userName = data;
	} */

	/* public void setUserSurname(String data) {
		this.userSurname = data;
	} */

    public void setUserNameSurname(String data) {
        this.userNameSurname = data;
    }

    public void setAuthorName(String data) {
        this.authorName = data;
    }

    public void setClientName(String data) {
        this.clientName = data;
    }

}