package com.quy.buvape.model;

import java.util.List;

public class ItemMessageWrapper {
    private String maso;
    private List<ItemMessage> messages;


    public ItemMessageWrapper(String idMessage,List<ItemMessage> itemMessages) {
        this.maso = idMessage;
        this.messages = itemMessages;
    }

    public List<ItemMessage> getItemMessages() {
        return messages;
    }

    public String getIdMessage() {
        return maso;
    }

    public void setIdMessage(String idMessage) {
        this.maso = idMessage;
    }

    public void setItemMessages(List<ItemMessage> itemMessages) {
        this.messages = itemMessages;
    }
}
