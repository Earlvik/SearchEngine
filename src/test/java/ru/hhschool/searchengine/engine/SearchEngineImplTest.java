package ru.hhschool.searchengine.engine;

import org.junit.Before;
import org.junit.Test;
import ru.hhschool.searchengine.model.Document;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class SearchEngineImplTest {

    private SearchEngine searchEngine;

    @Before
    public void setUp() throws IOException {
        searchEngine = new SearchEngineImpl();
    }

    @Test
    public void engineIndexTest(){
        Document doc1 = new Document("3", "Однажды в студёную зимнюю пору я из лесу вышел, был сильный мороз! В лесу родилась ёлочка!");
        Document doc2 = new Document("2", "To be, or not to be? - that is the question");
        Document doc3 = new Document("1", "Question is not in зимнюю, but in лесу");

        searchEngine.index(doc1);
        searchEngine.index(doc2);
        searchEngine.index(doc3);


        System.out.println("Success!");
    }

}