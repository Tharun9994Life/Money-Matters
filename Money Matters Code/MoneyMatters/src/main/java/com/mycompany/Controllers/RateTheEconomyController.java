package com.mycompany.Controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mycompany.Logic.RateTheEconomyLogic;
import com.mycompany.Repository.SQLQueriesRateTheEconomy;
import com.mycompany.Users.Consumer;
import com.mycompany.Users.Producer;

import jakarta.servlet.http.HttpSession;

@RestController
public class RateTheEconomyController {

    public static void loadData(ModelAndView mv, Consumer consumer) {
        // Load all questions and their multiple answers 
        mv.addObject("allQuestionsAndAnswerPairs", RateTheEconomyLogic.loadJSONToArrayList());

        // Load available dates for which user opinions exist
        mv.addObject("avaliableDates", RateTheEconomyLogic.loadUserOpinionDates());

        // Load latest consumer opinion and the date it was submitted
        mv.addObject("consumerOpinionDate", SQLQueriesRateTheEconomy.getLatestConsumerOpinion(consumer).get("consumerOpinionDate"));
        mv.addObject("consumerOpinion", SQLQueriesRateTheEconomy.getLatestConsumerOpinion(consumer).get("consumerOpinion"));
    }

    @GetMapping("rate-the-economy")
    public ModelAndView showQuestions(HttpSession session) {
        ModelAndView mv = new ModelAndView();

        // If logged-in user is a Consumer, allow access
        if (session.getAttribute("user") instanceof Consumer consumer) {
            mv.setViewName("ratetheeconomy/rate-the-economy");
            loadData(mv, consumer);
        }

        // If logged-in user is a Producer
        else if (session.getAttribute("user") instanceof Producer producer) {
            mv.setViewName("userdashboard/user-dashboard");
            mv.addObject("windowsAlert", true);
            mv.addObject("windowsAlertMessage", "Unable To Access. Need To Be A Consumer.");
        }

        // If no user is logged in, redirect to start/login page
        else {
            mv.setViewName("login/start");
        }

        return mv;
    }

    @PostMapping("rate-the-economy")
    public ModelAndView storeUserInput(HttpSession session, 
                @RequestParam String formName, 
                @RequestParam (required = false) String answer1, @RequestParam (required = false) String answer2, 
                @RequestParam (required = false) String answer3, @RequestParam (required = false) String answer4,
                @RequestParam (required = false) String answer5, @RequestParam (required = false) String answer6,
                @RequestParam (required = false) String answer7, @RequestParam (required = false) String answer8,
                @RequestParam (required = false) String answer9, @RequestParam (required = false) String answer10,
                @RequestParam (required = false) String answer11, @RequestParam (required = false) String selectedDate) {

        ModelAndView mv = new ModelAndView("ratetheeconomy/rate-the-economy");

        // Only Consumers can submit
        if (session.getAttribute("user") instanceof Consumer consumer) {
            loadData(mv, consumer);

            // User selects a previous date to view opinions
            if (formName.equals("chooseDateForm")) {
                mv.addObject("allUserOpinions", RateTheEconomyLogic.getAllUserOpinions(selectedDate));

                DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // Input date format
                DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Format for database queries

                // Calculate average opinion for the selected date
                mv.addObject("averageConsumerOpinion", SQLQueriesRateTheEconomy.averageConsumerOpinion(LocalDate.parse(selectedDate, df1).format(df2)));
            }

            else if (formName.equals("rateTheEconomyForm")) {
                ArrayList<String> answers = new ArrayList<>();
                answers.add(answer1);
                answers.add(answer2);
                answers.add(answer3);
                answers.add(answer4);
                answers.add(answer5);
                answers.add(answer6);
                answers.add(answer7);
                answers.add(answer8);
                answers.add(answer9);
                answers.add(answer10);
                answers.add(answer11);

                ArrayList<ArrayList<Object>> userOpinion = RateTheEconomyLogic.storeUserOpinion(answers);
                RateTheEconomyLogic.storeUserOpinionToAppropiateLocations(userOpinion, consumer);

                mv.setViewName("redirect:rate-the-economy");
            }
        }

        return mv;
    }
}