package com.mycompany.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycompany.Algorithms.VernamCipher;
import com.mycompany.Repository.SQLQueriesConsumers;
import com.mycompany.Repository.SQLQueriesProducers;
import com.mycompany.Users.Consumer;
import com.mycompany.Users.Producer;

import jakarta.servlet.http.HttpSession;

@RestController
public class LoginController {

    private static final SQLQueriesConsumers SQLQueriesConsumers = new SQLQueriesConsumers(); 
    private static final SQLQueriesProducers SQLQueriesProducers = new SQLQueriesProducers(); 

    @GetMapping("/")
    public ModelAndView redirectToStartScreen() {
        ModelAndView mv = new ModelAndView("redirect:start");
        return mv;    
    }

    // Show start screen
    @GetMapping("start")
    public ModelAndView showStartScreen(HttpSession session) {
        //invalidate any existing session
        session.invalidate();
        ModelAndView mv = new ModelAndView("login/start");
        return mv;
    }

    // Show signup form
    @GetMapping("signup")
    public ModelAndView showSignupScreen(HttpSession session) {
        ModelAndView mv = new ModelAndView("login/signup");
        return mv;
    }

    // Handle signup form and store user data to Database
    @PostMapping("signup")
    public ModelAndView storeSignupData(@RequestParam String firstName, 
                    @RequestParam String lastName, 
                    @RequestParam Integer age, 
                    @RequestParam String dateOfBirth, 
                    @RequestParam String email, 
                    @RequestParam String phoneNumber, 
                    @RequestParam String password, 
                    @RequestParam String typeOfEconomicAgent) {
        
        ModelAndView mv = new ModelAndView("login/login");

        // Check if password is strong
        char[] passwordAsCharArray = password.toCharArray();
        boolean length = false;
        boolean uppercase = false;
        boolean lowercase = false;
        boolean digit = false;
        boolean special = false;

        if (password.length() > 8) {
            length = true;
        }

        for (char ch : passwordAsCharArray) {
            if (Character.isUpperCase(ch)) {
                uppercase = true;
            }
            
            else if (Character.isLowerCase(ch)) {
                lowercase = true;
            }

            else if (Character.isDigit(ch)) {
                digit = true;
            }

            else {
                special = true;
            }
        }

        if ((length && uppercase && lowercase && digit && special) == false) {
            mv.setViewName("login/signup");
            mv.addObject("windowsAlert", true);
            mv.addObject("windowsAlertMessage", "Password is not strong. Password must contain at least:\n Uppercase Character\n Lowercase Character\n Digit\n Special Character\n More Than 8 Characters");
            return mv;
        }

        // Check if email already exists for either Consumers or Producers
        if (SQLQueriesConsumers.doesEmailExist(email) > 0 || SQLQueriesProducers.doesEmailExist(email) > 0){
            mv.setViewName("login/signup");
            mv.addObject("windowsAlert", true);
            mv.addObject("windowsAlertMessage", "Email Already Exists");           
            return mv;
        }

        // Encrypt password using Vernam cipher
        String vernamKey = VernamCipher.createVernamKey(email, password);
        String passwordInCipherText = VernamCipher.XORfunction(VernamCipher.convertPlainTextToBinary(password), vernamKey);

        // Store user depending on type (Consumer or Producer)
        if (typeOfEconomicAgent.equals("Consumer")) {
            Consumer consumer = new Consumer(firstName, lastName, age, dateOfBirth, email, phoneNumber, passwordInCipherText);
            SQLQueriesConsumers.insertUserDataIntoTable(consumer);
        } 
        
        else if (typeOfEconomicAgent.equals("Producer")) {
            Producer producer = new Producer(firstName, lastName, age, dateOfBirth, email, phoneNumber, passwordInCipherText);
            SQLQueriesProducers.insertUserDataIntoTable(producer);
        } 

        return mv;
    }

    // Show login screen
    @GetMapping("/login")
    public ModelAndView showLoginScreen(HttpSession session) {
        ModelAndView mv = new ModelAndView("login/login");
        return mv;
    }

    // Handle login form submission and authenticate user, by checking database
    @PostMapping("/login")
    public ModelAndView authenticateLogin(@RequestParam String email, 
                    @RequestParam String password,
                    HttpSession session, 
                    RedirectAttributes redirectAttributes) throws Exception {        
        ModelAndView mv = new ModelAndView("redirect:user-dashboard");

        // Load stored Vernam key for user
        String vernamKey = VernamCipher.loadVernamKey(email);

        // If email not found, return error
        if (vernamKey == null) {
            mv.setViewName("login/login");
            mv.addObject("windowsAlert", true);
            mv.addObject("windowsAlertMessage", "Invalid Email");
            return mv;
        }

        // Encrypt entered password for comparison with stored one
        String passwordAsPlainText = password;
        password = VernamCipher.XORfunction(VernamCipher.convertPlainTextToBinary(password), vernamKey);

        // Try to authenticate as Consumer or Producer
        int consumerID = SQLQueriesConsumers.authenticateLogin(email, password);
        int producerID = SQLQueriesProducers.authenticateLogin(email, password);

        if (consumerID > 0) {
            Consumer consumer = SQLQueriesConsumers.getData(consumerID);
            session.setAttribute("user", consumer);

            // Change Vernam key in JSON and update stored password in Database
            String newVernamKey = VernamCipher.createVernamKey(email, passwordAsPlainText);
            String newPasswordInCipherText = VernamCipher.XORfunction(VernamCipher.convertPlainTextToBinary(passwordAsPlainText), newVernamKey);
            SQLQueriesConsumers.updatePassword(newPasswordInCipherText, consumerID);
        } 

        else if (producerID > 0) {
            Producer producer = SQLQueriesProducers.getData(producerID);
            session.setAttribute("user", producer);

            // Change Vernam key in JSON and update stored password in Database 
            String newVernamKey = VernamCipher.createVernamKey(email, passwordAsPlainText);
            String newPasswordInCipherText = VernamCipher.XORfunction(VernamCipher.convertPlainTextToBinary(passwordAsPlainText), newVernamKey);
            SQLQueriesProducers.updatePassword(newPasswordInCipherText, producerID);
        }

        // Invalid password
        else {
            mv.setViewName("redirect:login");
            redirectAttributes.addFlashAttribute("windowsAlert", true);
            redirectAttributes.addFlashAttribute("windowsAlertMessage", "Invalid Password");
        }

        return mv;
    }
}