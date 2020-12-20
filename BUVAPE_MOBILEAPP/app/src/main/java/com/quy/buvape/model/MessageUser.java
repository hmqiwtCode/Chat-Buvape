package com.quy.buvape.model;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;
import java.util.List;

public class MessageUser implements IDialog, Serializable {
    private String maso;
    private List<Author> users;
    private Message lastMessage;
    private int unRead;
    private String dialogName;
    private String diaLogPhoto;
    private String typeMessage;

    public MessageUser() {
    }

    public MessageUser(String maso, List<Author> users, Message lastMessage, int unRead, String dialogName, String diaLogPhoto,String typeMessage) {
        this.maso = maso;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unRead = unRead;
        this.dialogName = dialogName;
        this.diaLogPhoto = diaLogPhoto;
        this.typeMessage = typeMessage;
    }

    @Override
    public String getId() {
        return maso;
    }

    @Override
    public String getDialogPhoto() {
        return diaLogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public List<? extends IUser> getUsers() {
        return users;
    }

    @Override
    public IMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage message) {
        this.lastMessage = (Message) message;
    }

    @Override
    public int getUnreadCount() {
        return unRead;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }
}
