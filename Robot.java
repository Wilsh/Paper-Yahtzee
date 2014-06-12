/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        Robot.java
*******************************************************************************/

import javax.swing.*;
import java.awt.*;

/**
*   Robot.java implements methods for a computer player to make menu choices and
*   decisions on scoring.
**/
public class Robot extends Player
{	
	/**
    *   Constructor creates a named player.
    *
    *   @param name of the player
    *
    *   @param app is the main Yahtzee class
    *
    *   @param isUsingThisApp is a boolean whether the player being instantiated
    *           is controlling this applet
    **/
	public Robot(String name, Yahtzee app, boolean isUsingThisApp)
    {
        super(name, app, isUsingThisApp);
	}
	
	/**
    *   Check for yahtzee or hold any pairs.
    *
    *   @param TheDice are needed by a computer player to make a choice
    **/
	public void getRollChoice(Dice TheDice)
	{
        int[] valueCount = new int[6];
        
        //ensure valueCount indicies are initialized to 0
        for(int idx = 0; idx < 6; idx++)
            valueCount[idx] = 0;
        
        //gather a count of each die value
        for(int idx = 0; idx < 5; idx++)
        {
            valueCount[(TheDice.getNum(idx) - 1)]++;
        }
        
        for(int idx = 0; idx < 6; idx++)
        {
            //score dice for yahtzee
            if(valueCount[idx] == 5)
            {
                TheDice.holdAll();
                break;
            }
            
            //hold dice if a pair exists
            if(valueCount[idx] > 1)
            {
                chooseHolds(TheDice);
                break;
            }
        }
	}
    
    /**
    *   Call method that runs when the roll button is clicked.
    **/
    public void simulateRollButton()
    {
        app.doRollBtn();
    }
    
    /**
    *   Enter a value given by a player for a category.
    **/
    public void cheatCategory(int a, int b)
    {
        ///Computer player doesn't cheat
    }
    
    /**
    *   Determine which category will maximize the player's score and enter
    *   the score into the scoresheet.
    *
    *   @param DieValues is a string containing the five Die values
    **/
    public void setCategory(int index, String DieValues)
    {
        int bestIndex = 0;
        int bestScore = 0;
        int thisScore = 0;
        
        for(int idx = 0; idx < 13; idx++)
        {
            thisScore = MyScore.calculateScore(idx, DieValues);
            
            //if this category gets the most points and has not already 
            //been chosen
            if(thisScore > bestScore && MyScore.getScore(idx) == 0)
            {
                bestIndex = idx;
                bestScore = thisScore;
            }
            
            //have Yahtzee
            if(thisScore == 50 && idx == 11)
            {
                bestIndex = idx;
                bestScore = thisScore;
                break;
            }
        }
        
        //don't enter a score if at least one point cannot be earned
        if(bestScore == 0)
        {
            bestIndex = 13;
        }
        else
        {
            if(MyScore.setScore(bestIndex, MyScore.calculateScore(
                    bestIndex, DieValues)))
            {
                addTurn();
            }
        }
        
        //set category as used for non-"no score" and no yahtzee
        if(bestScore != 0 && bestScore != 50)
            MyScore.usedCategory(bestIndex);
            
        lastCategory = bestIndex;
        lastScore = bestScore;
        
        //end turn
        app.nextPlayer();
    }
    
    /**
    *   Choose which dice to hold during a turn.
    *
    *   Precondition: this method should only be called when it is known that a
    *       pair exists within the Dice.
    *
    *   @param TheDice is the array of Die objects
    **/
    public void chooseHolds(Dice TheDice)
    {
        int numToHold;
        
        int[] valueCount = new int[6];
        
        //ensure valueCount indicies are initialized to 0
        for(int idx = 0; idx < 6; idx++)
            valueCount[idx] = 0;
        
        //gather a count of each die value
        for(int idx = 0; idx < 5; idx++)
        {
            valueCount[(TheDice.getNum(idx) - 1)]++;
        }
        
        //hold any two/three/four of a kind
        for(int idx = 0; idx < 6; idx++)
        {
            //found a two/three/four of a kind
            if(valueCount[idx] > 1)
            {
                numToHold = idx + 1;
                
                for(int dieIdx = 0; dieIdx < 5; dieIdx++)
                {
                    //if a Die has the value of the two/three/four of a kind
                    //and is not already held, hold it
                    if(TheDice.getNum(dieIdx) == numToHold && 
                            !TheDice.isDieHeld(dieIdx))
                    {
                        TheDice.holdDie(dieIdx);
                    }
                }
            }
        }
    }
}
