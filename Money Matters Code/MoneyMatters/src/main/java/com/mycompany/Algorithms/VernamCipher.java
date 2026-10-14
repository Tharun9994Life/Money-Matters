package com.mycompany.Algorithms;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Random;
import com.fasterxml.jackson.core.type.TypeReference;

public class VernamCipher {

    private static final FileHandlingJSON FileHandlingJSON = new FileHandlingJSON();

    public static String convertPlainTextToBinary(String plainText) {
        StringBuilder binaryText = new StringBuilder();
        char[] plainTextAsCharArray = plainText.toCharArray();

        for (int i = 0; i < plainTextAsCharArray.length; i++) {
            String binaryValue = Integer.toBinaryString(plainTextAsCharArray[i]);
            binaryText.append(binaryValue);
        }
        return binaryText.toString();
    }

    // Generates a random Vernam key of the same length as the ASCII version of the plain text. Stores the generated key in a JSON file, which is named as the user's email.
    public static String createVernamKey(String email, String plainText) {
        Random random = new Random();
        int keyLength = plainText.length() * 7;
        StringBuilder vernamKey = new StringBuilder();

        // Generate random binary key of required length
        for (int i = 0; i < keyLength; i++) {
            int bit = random.nextInt(2);
            vernamKey.append(bit);
        }

        // Store the key to a JSON file
        storeVernamKeysInJSON(email, vernamKey.toString());
        return vernamKey.toString();
    }

    // XOR Operation between the ASCII text and the Vernam key.
    public static String XORfunction(String binaryText, String vernamKey) {
        StringBuilder resultingText = new StringBuilder();
        char[] binaryTextAsCharArray = binaryText.toCharArray();
        char[] vernamKeyAsCharArray = vernamKey.toCharArray();

        for (int i = 0; i < binaryTextAsCharArray.length; i++) {
            if (binaryTextAsCharArray[i] == vernamKeyAsCharArray[i]) {
                resultingText.append(0);
            }
            
            else {
                resultingText.append(1);
            }
        }

        return resultingText.toString();
    }

    // Stores Vernam Key in JSON File
    public static void storeVernamKeysInJSON(String email, String vernamKey) {
        String folderName = "VernamKeys";

        // Creates VernamKeys folder, if folder doesn't exist.
        FileHandlingJSON.createFolder(folderName);
        File vernamKeyFile = FileHandlingJSON.createFile(folderName + File.separator + email);
        
        LinkedHashMap<String, String> vernamKeyLinkedHashMap = new LinkedHashMap<>();
        vernamKeyLinkedHashMap.put("vernamKey", vernamKey);

        // Write the key to the file
        FileHandlingJSON.writeFile(vernamKeyFile, vernamKeyLinkedHashMap);
    }

    // Loads the Vernam key for a given user's email from the JSON file.
    public static String loadVernamKey(String email) {
        String folderName = "VernamKeys";
        String fileName = email + ".json";
        File vernamKeyFile = new File(folderName + File.separator + fileName);

        LinkedHashMap<String, String> vernamKeyMap = FileHandlingJSON.readFile(vernamKeyFile, new TypeReference<LinkedHashMap<String, String>>() {});
        return vernamKeyMap.get("vernamKey");  
    }
}