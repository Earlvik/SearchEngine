package ru.hhschool.searchengine.engine;

import org.junit.Test;

import java.util.Arrays;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class IteratorTreeTest {


    @Test
    public void testPop() throws Exception {
        TreeSet<Integer> tree1 = new TreeSet<Integer>(Arrays.asList(1,3,5,7,9));
        TreeSet<Integer> tree2 = new TreeSet<Integer>(Arrays.asList(2,4,5,6,8));
        IteratorTree iteratorTree = new IteratorTree();
        iteratorTree.add(tree1);
        iteratorTree.add(tree2);

        while(!iteratorTree.isEmpty()){
            System.out.println(iteratorTree.pop());
        }
    }
}