package com.mycompany.Records;

import java.util.LinkedHashMap;

public class RateTheEconomyQuestions {
    private String question;
    private LinkedHashMap<String, Integer> answers;

    public RateTheEconomyQuestions() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LinkedHashMap<String, Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(LinkedHashMap<String, Integer> answers) {
        this.answers = answers;
    }
}