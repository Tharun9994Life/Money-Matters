//https://mkyong.com/java/jackson-how-to-parse-json/

package com.mycompany.Algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Glossary {

    private static final FileHandlingJSON FileHandlingJSON = new FileHandlingJSON();

    // Reads glossaryOfEconomicTerms.json and stores each word-definition pair in a LinkedHashMap.
    public static LinkedHashMap<String, String> storeTermsToLinkedHashMap() {
        File glossary = new File("glossaryOfEconomicTerms.json");
        LinkedHashMap<String, String> wordsDefintionsPairs = new LinkedHashMap<>();

        // Extract "word" and "definition" fields and put them in the map
        try {
            ArrayList<String> words = FileHandlingJSON.readFieldNameString(glossary, "word");
            ArrayList<String> definitions = FileHandlingJSON.readFieldNameString(glossary, "definition");
            
            for (int i = 0; i < words.size(); i++) {
                wordsDefintionsPairs.put(words.get(i), definitions.get(i));
            }
        } 
        
        catch (Exception ex) {
            System.out.println("Error reading glossary JSON file");
        }

        return wordsDefintionsPairs;
    }

    // Returns all words from the glossary as an ArrayList.
    public static ArrayList<String> storeWordsToArrayList() {
        return new ArrayList<>(storeTermsToLinkedHashMap().keySet());
    }

    // Returns all words in lowercase and without spaces, for searching.
    public static ArrayList<String> storePlainWordsToArrayList() {
        ArrayList<String> storeWordsToArrayList = storeWordsToArrayList();
        ArrayList<String> storePlainWordsToArrayList = new ArrayList<>();

        for (String word : storeWordsToArrayList) {
            storePlainWordsToArrayList.add(word.replace(" ", "").toLowerCase());
        }

        return storePlainWordsToArrayList;
    }

    // Returns all definitions from the glossary as an ArrayList.
    public static ArrayList<String> storeDefinitionsToArrayList() {
        return new ArrayList<>(storeTermsToLinkedHashMap().values());
    }
}