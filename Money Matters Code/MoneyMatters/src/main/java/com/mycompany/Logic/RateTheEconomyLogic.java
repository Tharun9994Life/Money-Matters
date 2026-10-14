package com.mycompany.Logic;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.Algorithms.FileHandlingJSON;
import com.mycompany.Records.RateTheEconomyQuestions;
import com.mycompany.Repository.SQLQueriesRateTheEconomy;
import com.mycompany.Users.Consumer;

public class RateTheEconomyLogic {

    private static final FileHandlingJSON FileHandlingJSON = new FileHandlingJSON();

    // Loads the full list of RateTheEconomyQuestions objects from JSON into an ArrayList
    public static ArrayList<RateTheEconomyQuestions> loadJSONToArrayList() {
        File questionAnswerPairingsFile = new File("rateTheEconomyQuestions.json");
        return FileHandlingJSON.readFileList(questionAnswerPairingsFile, new TypeReference<List<RateTheEconomyQuestions>>() {});
    }

    // Loads only the questions from the JSON file into an ArrayList of Strings
    public static ArrayList<String> loadQuestionsToArrayList() {
        File questionAnswerPairingsFile = new File("rateTheEconomyQuestions.json");
        return FileHandlingJSON.readFieldNameString(questionAnswerPairingsFile, "question");
    }

    // Loads the answer-to-weight mappings for each question from the JSON file
    public static ArrayList<LinkedHashMap<String, Integer>> loadAnswerWeightPairingsToArrayList() {
        ArrayList<LinkedHashMap<String, Integer>> allAnswerWeights = new ArrayList<>();
        File questionAnswerPairingsFile = new File("rateTheEconomyQuestions.json");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(questionAnswerPairingsFile);
            for (JsonNode questionNode : rootNode) {
                // Extract the "answers" node for each question
                JsonNode answersNode = questionNode.get("answers");
                LinkedHashMap<String, Integer> currentAnswerWeights = objectMapper.convertValue(answersNode, new TypeReference<LinkedHashMap<String, Integer>>() {});
                allAnswerWeights.add(currentAnswerWeights);
            }
        }
       
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return allAnswerWeights;
    }

    // Stores a user's answers along with the question and corresponding weight
    public static ArrayList<ArrayList<Object>> storeUserOpinion(ArrayList<String> answers) {
        ArrayList<ArrayList<Object>> userOpinion = new ArrayList<>();
        ArrayList<String> questions = loadQuestionsToArrayList();
        ArrayList<LinkedHashMap<String, Integer>> answerWeightPairings = loadAnswerWeightPairingsToArrayList();

        for (int i = 0; i < questions.size(); i++) {
            ArrayList<Object> newRow = new ArrayList<>();
            newRow.add(questions.get(i));
            newRow.add(answers.get(i));
            newRow.add(answerWeightPairings.get(i).get(answers.get(i)));
            userOpinion.add(newRow);
        }

        return userOpinion;
    }

    // Stores the user's opinion to a JSON file
    public static void storeUserOpinionToAppropiateLocations(ArrayList<ArrayList<Object>> userOpinion, Consumer consumer) {
        String folderName1 = "UserOpinions";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String folderName2 = LocalDate.now().format(dateFormatter);

        FileHandlingJSON.createFolder(folderName1);
        FileHandlingJSON.createFolder(folderName1 + File.separator + folderName2);

        String fileName = "userOpinion" + consumer.getConsumerID();
        File userOpinionFile = FileHandlingJSON.createFile(folderName1 + File.separator + folderName2 + File.separator + fileName);

        // Convert the 2D ArrayList of user opinions to a list of LinkedHashMaps
        ArrayList<LinkedHashMap<String, Object>> userOpinionArrayList = new ArrayList<>();
        for (ArrayList<Object> row : userOpinion) {
            LinkedHashMap<String, Object> tempLinkedHashMap = new LinkedHashMap<>();
            tempLinkedHashMap.put("question", row.get(0));
            tempLinkedHashMap.put("answer", row.get(1));
            tempLinkedHashMap.put("weight", row.get(2));
            userOpinionArrayList.add(tempLinkedHashMap);
        }

        // Write the user's opinion to JSON
        FileHandlingJSON.writeFile(userOpinionFile, userOpinionArrayList);

        // Calculate total score and update the database
        int totalScore = getTotalScore(userOpinionFile);
        int ratingID = SQLQueriesRateTheEconomy.checkNumberOfRatings(consumer);
        if (ratingID == -1) {
            SQLQueriesRateTheEconomy.insertDataIntoEconomyRaterTable(consumer, fileName, totalScore);
        }
       
        else {
            SQLQueriesRateTheEconomy.updateConsumerOpinion(ratingID, totalScore);
        }
    }

    // Calculates the total score of a user's answers as a percentage of the maximum score (57)
    public static int getTotalScore(File userOpinionFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        double totalScore = 0;

        try {
            JsonNode rootNode = objectMapper.readTree(userOpinionFile);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    totalScore += node.get("weight").asInt();
                }
            }
        }
       
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return (int) ((totalScore / 57) * 100);
    }

    // Returns the available dates for which user opinions have been stored
    public static LinkedHashMap<String, String> loadUserOpinionDates() {
        File folder = new File("UserOpinions");
        File[] files = folder.listFiles();
        LinkedHashMap<String, String> availableDates = new LinkedHashMap<>();

        for (File file : files) {
            if (file.isDirectory()) {
                availableDates.put(file.getName(), file.getName());
            }
        }

        return availableDates;
    }

    // Get all user answers for a given date and counts frequencies for each answer
    public static LinkedHashMap<String, LinkedHashMap<String, Integer>> getAllUserOpinions(String date) {
        ArrayList<String> questions = loadQuestionsToArrayList();
        LinkedHashMap<String, LinkedHashMap<String, Integer>> allUserOpinions = new LinkedHashMap<>();

        for (String question : questions) {
            allUserOpinions.put(question, new LinkedHashMap<>());
        }

        File folder = new File("UserOpinions" + File.separator + date);
        File[] files = folder.listFiles();
        if (files == null) {
            return allUserOpinions;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < files.length; i++) {
            File userOpinionFile = files[i];

            try {
                JsonNode rootNode = objectMapper.readTree(userOpinionFile);
               
                for (int j = 0; j < rootNode.size(); j++) {
                    JsonNode node = rootNode.get(j);
                    String question = node.get("question").asText();
                    String answer = node.get("answer").asText();

                    LinkedHashMap<String, Integer> answerCounts = allUserOpinions.get(question);
                    answerCounts.put(answer, answerCounts.getOrDefault(answer, 0) + 1);
                }
            }
           
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return allUserOpinions;
    }
}