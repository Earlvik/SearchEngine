package ru.hhschool.searchengine.model;

import javax.validation.constraints.NotNull;

/**
 * Created by Earlviktor on 11.02.2015.
 */
public class IndexEntry implements Comparable<IndexEntry> {

    int id;

    int number;

    public IndexEntry(int id, int number) {
        this.id = id;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void incNumber(){
        this.number++;
    }

    @Override
    public int compareTo(@NotNull IndexEntry other) {
        return (this.id == other.id)?0:(this.id > other.id)?1:-1;
    }
}
