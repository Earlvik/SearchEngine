package ru.hhschool.searchengine.engine;

import ru.hhschool.searchengine.model.Document;

import java.util.List;

/**
 * Created by Earlviktor on 11.02.2015.
 */
public interface SearchEngine {
    List<Integer> search(String query, String logic, int count);

    void index(Document document);
}
