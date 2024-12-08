package com.gamexpress.guess_the_word.guess_the_word.controllers;

import com.gamexpress.guess_the_word.guess_the_word.service.GameService;
import com.gamexpress.guess_the_word.guess_the_word.utils.GameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


//creating the object of service

//i will be needing that object from my spring , so i will be writing AutoWired


@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameUtils gameUtils;
    @GetMapping("/game-home")
    public String showGameHomePage(@RequestParam(value= "guessedChar", required = false) String guessedChar, Model model){

        System.out.println("captured guesses word is " + guessedChar);
        String[] randomWords = gameService.getRandomWords();
        model.addAttribute("randomWords", randomWords);

        String randomWord = gameService.toString();

        if(guessedChar != null) {
            if(guessedChar.length() != 1){
                model.addAttribute("errorMessage", "Enter exactly one character!");
            }else {
                boolean isGuessCorrect = gameService.addGuess(guessedChar.charAt(0));
                randomWord = gameService.toString();

                if (isGuessCorrect == false) {
                    gameUtils.reduceTry();
                }

//                Check if the word is completely guessed
                if (randomWord.replace(" ", "").equals(gameService.getRandomlyChoosenWord())) {
                    model.addAttribute("gameStatus", "You Won!");
                    model.addAttribute("isGameWon", true);//For Visual representation
                }
            }
        }
        System.out.println("number of tries remaining: " + gameUtils.getTriesRemaining());
        model.addAttribute("wordToDisplay",randomWord);
        model.addAttribute("triesLeft",gameUtils.getTriesRemaining());
        model.addAttribute("errorMessage",null);//Default to null to clear old errors
        return "game-home-page";
    }


    //reset the tries to the initial value
    @GetMapping("/reload")
    public String reloadGame(){
        gameService = gameUtils.reload();
        gameUtils.resetTries();
        return "redirect:/game-home";
    }
}
