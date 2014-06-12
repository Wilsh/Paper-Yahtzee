/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        Dice.java
*******************************************************************************/

import javax.swing.*;
import java.awt.*;

/**
*   Dice.java holds an array of five Die objects. It contains methods to get and
*   set values held by the Die objects.
**/
public class Dice extends JPanel
{
    private RandomNum RandomDieValue;
    private Die[] TheDice;
    private boolean[] IsHeld;
    
	/**
    *   Constructor initializes the pseudorandom number generator and the array
    *   of Die objects.
    *
    *   @param seed is a seed for a pseudorandom number generator that 
    *   represents a value on a die. Using a value < 0 results in a different
    *   number each time; using a value >= 0 allows a game to be repeated
    *   for testing purposes.
    *
    *   @param a is the main Yahtzee class
    **/
	public Dice(int seed)
	{
        super();
        RandomDieValue = new RandomNum(seed);
		
        TheDice = new Die[5];
        for(int idx = 0; idx < 5; idx++)
        {
            TheDice[idx] = new Die(RandomDieValue.getNum());
        }
        
        setOpaque(false);
        //setBackground(Color.blue);
        //setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setLayout(null);
        //add(Box.createRigidArea(new Dimension(0,115)));
        setBounds(0,0,800,115);
        validate();
	}
    
    /**
    *   add the dice to this panel using animation
    **/
    public void addDice()
    {
        removeAll();
        add(TheDice[0]);
        TheDice[0].setBounds(150,10,100,100);
        add(TheDice[1]);
        TheDice[1].setBounds(250,10,100,100);
        add(TheDice[2]);
        TheDice[2].setBounds(350,10,100,100);
        add(TheDice[3]);
        TheDice[3].setBounds(450,10,100,100);
        add(TheDice[4]);
        TheDice[4].setBounds(550,10,100,100);
        
        revalidate();
        repaint();
    }
    
    /**
    *   remove the dice from this panel using animation
    **/
    public void removeDice()
    {
        removeAll();
        repaint();
    }
    
    /**
    *   remove a die and add another in its place using animation
    *
    *   @param index is the die to remove, counting from 0, left-to-right
    **/
    public void replaceDie(int index)
    {
        //TheDice[index]
    }
    
    /**
    *   Set the number held by a Die at a specific location in the array
    *
	*   @param location is the index of the array holding the Die to be modified
    *   
    *   @param newValue is the new number held by the Die
    **/
	public void setNumber(int location, int newValue)
	{
		TheDice[location].setNumber(newValue);
	}
	
	/**
    *   Get the number held by a Die at a specific location in the array
    *
	*   @param index of the array holding the Die to be examined
    *
    *   @return the number held by a Die
    **/
	public int getNum(int index)
	{
		return TheDice[index].getNumber();
	}
    
    /**
    *   Set or remove flags for dice that the player would like to hold
    *
	*   @param index of the array corresponding to the die to be held
    **/
    public void holdDie(int index)
    {
        TheDice[index].hold();
    }
    
    /**
    *   Return the status of a Die being held
    *
    *   @param index of the Die to examine
    *
    *   @return whether the Die is currently held
    **/
    public boolean isDieHeld(int index)
    {
        return TheDice[index].isHeld();
    }
    
    /**
    *   Reset flags for dice to be held
    **/
    public void resetHolds()
    {
        for(int idx = 0; idx < 5; idx++)
        {
            TheDice[idx].resetHold();
        }
    }
    
    /**
    *   Generate new values for dice that are not held by the player
    **/
    public void rollDice()
    {
        for(int idx = 0; idx < 5; idx++)
        {
            if(!TheDice[idx].isHeld())
            {
                TheDice[idx].setNumber(RandomDieValue.getNum());
            }
        }
    }
    
    /**
    *   Return the value of each Die
    *
    *   @return a string containing the five Die values
    **/
    public String getDieValues()
    {
        String DieValues = new String();
        
        for(int idx = 0; idx < 5; idx++)
        {
            DieValues = DieValues + 
                ((Integer)(TheDice[idx].getNumber())).toString() + " ";
        }
        
        return DieValues;
    }
    
    /**
    *   Set the flag for each die to "held"
    **/
    public void holdAll()
    {
        resetHolds();
        for(int idx = 0; idx < 5; idx++)
            holdDie(idx);
    }
}
