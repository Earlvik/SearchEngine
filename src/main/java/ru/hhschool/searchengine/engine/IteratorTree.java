package ru.hhschool.searchengine.engine;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Earlviktor on 11.02.2015.
 */
public class IteratorTree {

    SortedSet<ListIterator<Integer>> tree = new TreeSet<ListIterator<Integer>>(new Comparator<ListIterator<Integer>>(){

        @Override
        public int compare(ListIterator<Integer> iter1, ListIterator<Integer> iter2) {
            if(iter1 == iter2) return 0;
            Integer value1 = iter1.getValue();
            Integer value2 = iter2.getValue();
            if(value1 == null && value2 == null){
                return 0;
            }
            if(value1 == null){
                return 1;
            }
            if(value2 == null){
                return -1;
            }
            int result =  iter1.getValue().compareTo(iter2.getValue());
            if(result == 0){
                iter1.next();
                return compare(iter1, iter2);
            }
            return result;
        }
    });

    public void add(TreeSet<Integer> newTree){
        tree.add(new ListIterator<Integer>(newTree.iterator()));
    }

    public int pop(){
        if(isEmpty()) throw new IllegalStateException("Tree was empty");
        int result = tree.first().getValue();
        ListIterator<Integer> temp = tree.first();

        tree.remove(temp);
        temp.next();
        tree.add(temp);
        return result;
    }

    public boolean isEmpty() {
        return (tree.isEmpty() || tree.first().getValue() == null);
    }

    @Override
    public String toString(){
        String result = "";
        for(ListIterator<Integer> iterator:tree){
            result+= iterator.getValue() + " ";
        }
        return result;
    }
}
