package com.mycompany.Algorithms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// Extends FileHandling Class to provide JSON file operations.
public class FileHandlingJSON extends FileHandling {
    
    private final String fileExtension = ".json";

    @Override
    public File createFile(String rootDirectory) {
        try {
            // Appends ".json" extension
            File myFile = new File(rootDirectory + fileExtension);
            
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } 
            
            else {
                System.out.println("File already exists.");
            }
            return myFile;
        } 
        
        catch (IOException e) {
            System.out.println("An error occurred while creating the JSON file.");
        }
        
        return null;
    }

    @Override
    public void writeFile(File file, Object text) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Serialise object to JSON and write it to file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, text);
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Reads JSON files and converts it into an LinkedHashMap.
    public <T> LinkedHashMap<String, T> readFile(File file, TypeReference<LinkedHashMap<String, T>> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LinkedHashMap<String, T> contents = objectMapper.readValue(file, typeReference);
            if (contents != null) {
                return contents;
            }
        } 

        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Reads JSON files and converts it into an ArrayList of objects.
    public <T> ArrayList<T> readFileList(File file, TypeReference<List<T>> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ArrayList<T> contents = new ArrayList<>(objectMapper.readValue(file, typeReference));
            return contents;
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
        
    // Reads a specific field from all objects in a JSON array file and returns them as strings.
    public ArrayList<String> readFieldNameString(File file, String fieldName) {              
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Parse JSON file into a tree
            JsonNode rootNode = objectMapper.readTree(file);
            ArrayList<String> contents = new ArrayList<>();
            
            // Loop through each object in the JSON array and extract the field value
            for (JsonNode node : rootNode) {
                contents.add(node.get(fieldName).asText());
            }

            return contents;
        } 
        
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}