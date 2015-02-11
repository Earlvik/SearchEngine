package ru.hhschool.searchengine.engine;

import java.util.Iterator;

/**
 * Created by Earlviktor on 11.02.2015.
 */
public class ListIterator<T> {

    Iterator<T> iterator;
    T value;

    public ListIterator(Iterator<T> iterator){
        this.iterator = iterator;
        value = (iterator.hasNext())?iterator.next():null;
    }

    public boolean hasNext(){
        return iterator.hasNext();
    }

    public T getValue(){
        return value;
    }

    public T next(){
        if(!iterator.hasNext()){
            value = null;
        }else {
            value = iterator.next();
        }
        return value;
    }
}
