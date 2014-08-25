/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        Human.java
*******************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
*   Human.java implements methods for a human player to score categories.
**/
public class Human extends Player
{
    private SoundLib Sounds;
    private SoundLib BadPlaySounds;
    private SoundLib LowerCheerSounds;
    private SoundLib UpperCheerSounds;
    private String[] soundFiles;
    
    /**
    *   Constructor creates a named player and loads sounds related to scoring.
    *
    *   @param name of the player
    *
    *   @param app is the main Yahtzee class
    *
    *   @param isUsingThisApp is a boolean whether the player being instantiated
    *           is controlling this applet
    **/
    public Human(String name, Yahtzee app, boolean isUsingThisApp)
    {
        super(name, app, isUsingThisApp);
        
        soundFiles = new String[1];
        soundFiles[0] = new String("kiddinMe.wav");
        Sounds = new SoundLib(soundFiles);
        
        soundFiles = new String[4];
        soundFiles[0] = new String("murloc.wav");
        soundFiles[1] = new String("comeOn.wav");
        soundFiles[2] = new String("goblinLaugh.wav");
        soundFiles[3] = new String("goblinYell.wav");
        BadPlaySounds = new SoundLib(soundFiles);
        
        soundFiles = new String[5];
        soundFiles[0] = new String("lowerGood1.wav");
        soundFiles[1] = new String("lowerGood2.wav");
        soundFiles[2] = new String("lowerGood3.wav");
        soundFiles[3] = new String("lowerGood4.wav");
        soundFiles[4] = new String("lowerGood5.wav");
        LowerCheerSounds = new SoundLib(soundFiles);
        
        soundFiles = new String[5];
        soundFiles[0] = new String("upperGood1.wav");
        soundFiles[1] = new String("upperGood2.wav");
        soundFiles[2] = new String("upperGood3.wav");
        soundFiles[3] = new String("upperGood4.wav");
        soundFiles[4] = new String("upperGood5.wav");
        UpperCheerSounds = new SoundLib(soundFiles);
    }
    
    /**
    *   Get the player's choice for the roll menu (used only by a robot).
    *
    *   @param TheDice are needed by a computer player to make a choice
    **/
    public void getRollChoice(Dice TheDice)
    {
        ///not used by human player
    }
    
    public void simulateRollButton()
    {
        ///not used by human player
    }
    
    /**
    *   Enter a value given by a player for a category.
    *
    *   @param index of the category to score
    *
    *   @param score to place in the category
    **/
    public void cheatCategory(int index, int score)
    {
        if(!MyScore.setScore(index, score))
            removeTurn();
         
        if(!(score == 50 && index == 11))
            MyScore.usedCategory(index);
            
        updateScore();
    }
    
    /**
    *   Enter the score for a category based on the current state
    *   of the dice.
    *
    *   @param index of the category to score
    *
    *   @param DieValues is a string containing the five Die values
    **/
    public void setCategory(int index, String DieValues)
    {
        int score = 0;
        
        if(index < 13)
        {
            score = MyScore.calculateScore(index, DieValues);
            
            if(MyScore.setScore(index, score))
                addTurn();
            
            if(score != 50)
                MyScore.usedCategory(index);
        }
        
        if(app.muteButton.isEnabled())
        {
            if(score == 0 && playerUsingThisApplet)
                BadPlaySounds.playRandom();
            else if(MyScore.wasGoodScore() && playerUsingThisApplet)
            {
                if(index < 6)
                    UpperCheerSounds.playRandom();
                else
                    LowerCheerSounds.playRandom();
            }
            else if(score == 50 && !playerUsingThisApplet)
                Sounds.playSound("kiddinMe.wav");
        }
        
        lastCategory = index;
        lastScore = score;
    }
    
    /**
    *   Choose which dice to hold during a turn
    *
    *   @param TheDice is the set of Die objects
    **/
    public void chooseHolds(Dice TheDice)
    {
        ///no longer used by human player
    }
}
