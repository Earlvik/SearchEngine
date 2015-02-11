package ru.hhschool.searchengine.engine;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.russianStemmer;
import ru.hhschool.searchengine.model.Document;
import ru.hhschool.searchengine.model.IndexEntry;



import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Earlviktor on 11.02.2015.
 */
public class SearchEngineImpl implements SearchEngine {
    int docsNum = 0;
    Map<String,TreeSet<IndexEntry>> reverseIndex = new HashMap<String, TreeSet<IndexEntry>>();

    Set<String> stopWords = new HashSet<String>();

    russianStemmer ruStemmer;
    englishStemmer enStemmer;

    public SearchEngineImpl() throws IOException {
        ClassLoader classLoader =  getClass().getClassLoader();
        URL resource = classLoader.getResource("stopWords.txt");
        if(resource == null) throw new NoSuchFileException("No stop-words list found");
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader( new FileInputStream(resource.toURI().getPath()), "UTF-8");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            String[] lines = line.split("\\s");
            stopWords.addAll(Arrays.asList(lines));
        }
        bufferedReader.close();

        try {
            ruStemmer = russianStemmer.class.newInstance();
            enStemmer = englishStemmer.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalStateException("No stemmer classes found", e);
        }
    }


    @Override
    public List<Integer> search(String query, String logic, int count) {
        String[] terms = query.split("\\s");
        for (int i = 0; i < terms.length; i++) {
            terms[i] = stem(terms[i]);
        }
        List<Integer> result;
        switch (logic) {
            case "and":
                result = searchAdd(terms);
                break;
            case "or":
                result = searchOr(terms);
                break;
            default:
                throw new IllegalArgumentException("Logic was neither or nor and");
        }
        if(count > result.size()) count = result.size();
        return new ArrayList<Integer>(result.subList(0, count));
    }

    private List<Integer> searchOr(String[] terms) {
        IteratorTree iteratorTree = new IteratorTree();
        List<Integer> result = new ArrayList<>();
        for(String term:terms){
            if(reverseIndex.containsKey(term)){
                iteratorTree.add(getIds(reverseIndex.get(term)));
            }
        }
        while(!iteratorTree.isEmpty()){
            result.add(iteratorTree.pop());
        }
        relevanceSort(terms, result);
        return result;
    }



    private List<Integer> searchAdd(String[] terms) {
        TreeSet<Integer> resultSet = getIds(reverseIndex.get(terms[0]));
        for(String term:terms){
            if(reverseIndex.containsKey(term)){
                resultSet = zipper(resultSet, getIds(reverseIndex.get(term)));
            }
        }
        List<Integer> result = new ArrayList<>(resultSet);
        relevanceSort(terms, result);
        return result;
    }

    private TreeSet<Integer> zipper(TreeSet<Integer> one, TreeSet<Integer> another) {
        Iterator<Integer> iter1 = one.iterator();
        Iterator<Integer> iter2 = another.iterator();
        TreeSet<Integer> result = new TreeSet<>();
        int first = iter1.next();
        int second = iter2.next();
        while(iter1.hasNext() && iter2.hasNext()){
            if(first < second){
                first = iter1.next();
            }else if(second < first){
                second = iter2.next();
            }else{
                result.add(first);
                first = iter1.next();
                second = iter2.next();
            }
        }
        if(iter1.hasNext()){
            while(iter1.hasNext()){
                first = iter1.next();
                if(first == second){
                    result.add(first);
                }
            }
        } else if(iter2.hasNext()){
            while(iter2.hasNext()){
                second = iter2.next();
                if(first == second){
                    result.add(first);
                }
            }
        }
        if(second == first) result.add(first);
        return result;
    }

    @Override
    public void index(Document document) {
        docsNum++;
        int docId = document.getId();
        for(String word:document.getText().split("\\s")){
            String trimmed = stem(word.replaceAll("[^a-zA-Zа-яёА-Я0-9]", "").toLowerCase());
            if(isStopWord(trimmed)) continue;
            if(reverseIndex.containsKey(trimmed)){
                TreeSet<IndexEntry> termIterator = reverseIndex.get(trimmed);
                boolean found = false;
                for(IndexEntry entry:termIterator){
                    if(entry.getId() == docId){
                        found = true;
                        entry.incNumber();
                        break;
                    }
                }
                if(!found){
                    termIterator.add(new IndexEntry(docId, 1));
                }
            }else{
                TreeSet<IndexEntry> treeSet = new TreeSet<IndexEntry>();
                treeSet.add(new IndexEntry(docId, 1));
                reverseIndex.put(trimmed, treeSet);
            }

        }
    }

    private String stem(String s) {
        SnowballStemmer stemmer;
        if(s.matches("^[а-я]*$")){
            stemmer = ruStemmer;
            stemmer.setCurrent(s);
            stemmer.stem();
            return stemmer.getCurrent();
        }else if(s.matches("^[a-z]*$")){
            stemmer = enStemmer;
            stemmer.setCurrent(s);
            stemmer.stem();
            return stemmer.getCurrent();
        }
        return s;
    }

    boolean isStopWord(String word){
        return stopWords.contains(word);
    }

    public double relevance(int docId, String term){
        IndexEntry entry = null;
        if(!reverseIndex.containsKey(term)) return 0;
        for(IndexEntry iEntry: reverseIndex.get(term)){
            if(iEntry.getId() == docId){
                entry = iEntry;
                break;
            }
        }
        if(entry == null ) return 0;
        double tf = Math.sqrt(entry.getNumber());
        int relevantDocs = reverseIndex.get(term).size();
        if( relevantDocs <= 0) return 0.0;
        double idf = Math.log(docsNum/relevantDocs);
        return tf*idf;
    }

    TreeSet<Integer> getIds(TreeSet<IndexEntry> input){
        return input.stream().map(IndexEntry::getId).collect(Collectors.toCollection(() -> new TreeSet<>()));
    }

    private void relevanceSort(final String[] terms, List<Integer> result) {

        Map<Integer, Double> totalRelevance = new HashMap<>();

        result.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                double rel1 = 0, rel2 = 0;
                if(totalRelevance.containsKey(o1)){
                    rel1 = totalRelevance.get(o1);
                }else{
                    for(String term:terms){
                        rel1+= relevance(o1, term);
                    }
                    totalRelevance.put(o1, rel1);
                }

                if(totalRelevance.containsKey(o2)){
                    rel2 = totalRelevance.get(o2);
                }else{
                    for(String term:terms){
                        rel2+= relevance(o2, term);
                    }
                    totalRelevance.put(o2, rel2);
                }

                return (rel1 == rel2)?0:(rel1 > rel2)?-1:1;
            }
        });
    }
}
