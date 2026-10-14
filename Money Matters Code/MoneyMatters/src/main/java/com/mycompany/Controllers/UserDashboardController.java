package com.mycompany.Controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mycompany.Algorithms.BinarySearch;
import com.mycompany.Algorithms.Glossary;
import com.mycompany.PythonFlaskAPI.PythonFlaskAPI;
import com.mycompany.Users.Consumer;
import com.mycompany.Users.Producer;

import jakarta.servlet.http.HttpSession;

@RestController
public class UserDashboardController {

    // Store glossary data - terms and definitions
    private final LinkedHashMap<String, String> wordsDefinitionsPairs = Glossary.storeTermsToLinkedHashMap();
    private final ArrayList<String> wordsArrayList = Glossary.storeWordsToArrayList();
    private final ArrayList<String> plainWordsArrayList = Glossary.storePlainWordsToArrayList();
    private final ArrayList<String> definitionsArrayList = Glossary.storeDefinitionsToArrayList();

    public ModelAndView showData(ModelAndView mv) {
        mv.addObject("wordsDefinitionsPairs", wordsDefinitionsPairs);
        return mv;
    }

    @GetMapping("/user-dashboard")
    public ModelAndView showUserDashboard(HttpSession session) {
        ModelAndView mv = new ModelAndView("userdashboard/user-dashboard");

        // If no valid user 
        if (!(session.getAttribute("user") instanceof Consumer consumer || session.getAttribute("user") instanceof Producer producer)) {
            mv.setViewName("login/start");
            session.invalidate();
        }

        showData(mv);
        return mv;
    }

    @PostMapping("/user-dashboard")
    public Object postUserDashboard(@RequestParam String formName, 
        @RequestParam(required = false) String glossarySearchInput,
        @RequestParam(required = false) String userPrompt) {
        
        if (formName.equals("glossarySearch")) {
            ModelAndView mv = new ModelAndView("userdashboard/user-dashboard");

            glossarySearchInput = glossarySearchInput.replace(" ", "").toLowerCase();

            // Perform binary search on glossary word list
            int index = BinarySearch.binarySearchObject(plainWordsArrayList, 0, plainWordsArrayList.size(), glossarySearchInput);

            LinkedHashMap<String, String> wordsDefinitionsPair = new LinkedHashMap<>();

            // If word not found, show error message
            if (index == -1) {
                wordsDefinitionsPair.put("Word Not Found", "");
            }

            // If found, show word with definition
            else {
                wordsDefinitionsPair.put(wordsArrayList.get(index), definitionsArrayList.get(index));
            }

            mv.addObject("wordsDefinitionsPairs", wordsDefinitionsPair);

            return mv;
        }

        // User asked AI a question
        else if (formName.equals("askAIBot")) {
            return Map.of("aiReply", PythonFlaskAPI.aiAnswer(userPrompt));
        }

        return null;
    }
}