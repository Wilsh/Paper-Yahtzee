/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        Turn.java
*******************************************************************************/

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
*   Turn.java controls the gameplay for each player's turn.
**/
public class Turn
{
    private int rollsLeft, rollCount;
    private Player ThePlayer;
    private Dice TheDice;
    private DialogJP DialogPanel;
    private JButton RollButton;
    private boolean isRobot;
    private javax.swing.Timer Timer;

    /**
    *   Constructor initiates the number of rolls remaining in a turn and the 
    *   count of rolls taken, decrements the turn counter in the player class,
    *   and prepares the dice for a new turn.
    *
    *   @param Cubes is the set of dice that will be rolled for the player
    *
    *   @param p is the player who is granted the turn
    *   
    *   @param panel is the DialogPanel used on the game board
    **/
	public Turn(Dice cubes, Player p, DialogJP panel)
	{
        rollsLeft = 3;
        rollCount = 1;
        ThePlayer = p;
        ThePlayer.removeTurn();
        isRobot = (ThePlayer instanceof Robot);
        TheDice = cubes;
        TheDice.resetHolds();
        DialogPanel = panel;
	}

    /**
    *   @return whether the player has any rolls left for this turn
    **/
    public boolean canRoll()
    {
        if(rollsLeft == 0)
            return false;

        return true;
    }
    
    /**
    *   Update the Dialog panel or call methods for a robot player
    *
    *   @return whether the player has used all three rolls
    **/
    public boolean takeTurn()
    {
        rollsLeft--;
        rollCount++;
        
        if(canRoll())
        {
            if(isRobot)
            {
                //wait for dice animation to finish before selecting holds
                SwingWorker timer = new SwingWorker()
                {
                    protected String doInBackground()
                    {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch(Exception ex)
                        {
                          System.out.println(
                            "Turn.java takeTurn() Exception1: sleep failed");
                        }
                        return null;
                    }
                    protected void done()
                    {
                        ThePlayer.getRollChoice(TheDice);
                        DialogPanel.setButton(rollCount);
                    }
                };
                timer.execute();
                
            }
            else
            {
                DialogPanel.setAction("Roll or score a category");
                DialogPanel.setButton(rollCount);
            }
        }
        else
        {
            if(isRobot)
            {
                try
                {
                    Thread.sleep(2000);
                }
                catch(Exception ex)
                {
                    System.out.println(
                        "Turn.java takeTurn() Exception2: sleep failed");
                }
                
                ThePlayer.setCategory(-1, TheDice.getDieValues());
            }
            else
            {
                DialogPanel.setAction("Select a category to score");
            }
            return false;
        }

        ThePlayer.enableCategories();
        return true;
    }
}

