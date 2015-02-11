package ru.hhschool.searchengine.model;

/**
 * Created by Earlviktor on 11.02.2015.
 */
public class Document {
    int id;
    String text;

    public Document(String id, String text) {
        this.id = Integer.parseInt(id);
        this.text = text;
    }

    public Document(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
