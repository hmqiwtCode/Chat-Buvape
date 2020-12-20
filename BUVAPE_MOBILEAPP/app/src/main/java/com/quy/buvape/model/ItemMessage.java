package com.quy.buvape.model;

public class ItemMessage {
    private String id;
    private String time;
    private String message;
    private String id_gen;
    private String current;

    public ItemMessage(String id, String time, String message, String id_gen, String current) {
        this.id = id;
        this.time = time;
        this.message = message;
        this.id_gen = id_gen;
        this.current = current;
    }

    public ItemMessage() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId_gen() {
        return id_gen;
    }

    public void setId_gen(String id_gen) {
        this.id_gen = id_gen;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }


    @Override
    public String toString() {
        return "ItemMessage{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", message='" + message + '\'' +
                ", id_gen='" + id_gen + '\'' +
                ", current='" + current + '\'' +
                '}';
    }
}
