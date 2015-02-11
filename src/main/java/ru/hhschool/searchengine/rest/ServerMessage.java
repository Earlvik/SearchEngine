package ru.hhschool.searchengine.rest;

/**
 * Created by Earlviktor on 11.02.2015.
 */
public class ServerMessage {
    String message;

    public ServerMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
