package com.mycompany.Algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandling {

    private final String fileExtension = ".txt";

    public File createFolder(String rootDirectory) {
        try {
            File myFolder = new File(rootDirectory);
            
            // Creates folder if it doesn't exist
            if (!myFolder.exists()) {
                if (myFolder.mkdir()) {
                    System.out.println("Folder created: " + rootDirectory);
                }

                else {
                    System.out.println("Folder exists: " + rootDirectory);
                }
            }

            return myFolder;
        } 
        
        catch (Exception ex) {
            System.out.println("Error with folder: " + rootDirectory);
        }
        
        return null;
    }

    public File createFile(String rootDirectory) {
        try {
            // Appends ".txt" extension
            File myFile = new File(rootDirectory + fileExtension);
            
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            }

            else {
                System.out.println("File already exists.");
            }
            
            return myFile;
        } 
        
        catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    // Deletes a file
    public void deleteFile(File file) {
        if (file.delete()) {
            System.out.println("Deleted the file: " + file.getName());
        } 
        
        else {
            System.out.println("Failed to delete the file.");
        }
    }

    public String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Read each line of the file and adds to StringBuilder
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } 
        
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return sb.toString();
    }

    // Writes contents to a file
    public void writeFile(File file, Object text) { 
        try (FileWriter writer = new FileWriter(file, false)) { 
            writer.write(text.toString()); 
        }
        
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Appends contents to a file
    public void appendFile(File file, Object text) {
        try (FileWriter writer = new FileWriter(file, true)) { 
            writer.write(text.toString());
        } 
        
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}