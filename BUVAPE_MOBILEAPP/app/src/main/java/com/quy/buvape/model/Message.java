package com.quy.buvape.model;

import android.provider.ContactsContract;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class Message implements IMessage {

    private Author author;
    private String id;
    private String text;
    private Date current;

    public Message(Author author,String id,String text,Date current){
        this.author = author;
        this.id = id;
        this.text = text;
        this.current = current;

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return current;
    }


    @Override
    public String toString() {
        return "Message{" +
                "author=" + author +
                ", id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", current=" + current +
                '}';
    }
}
