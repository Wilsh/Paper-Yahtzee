/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        Dice.java
*******************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
*   Dice.java holds an array of five Die objects. It contains methods to get and
*   set values held by the Die objects.
**/
public class Dice extends JPanel
{
    private RandomNum RandomDieValue;
    private Die[] TheDice;
    private SoundLib Sound, RemoveSound;
    private String[] soundFiles1, soundFiles2;
    private boolean showingDice;
    private volatile boolean isAnimationDone = true;
    
	/**
    *   Constructor initializes the pseudorandom number generator and the array
    *   of Die objects.
    *
    *   @param seed is a seed for a pseudorandom number generator that 
    *   represents a value on a die. Using a value < 0 results in a different
    *   number each time; using a value >= 0 allows a game to be repeated
    *   for testing purposes.
    **/
	public Dice(int seed, boolean canCheat)
	{
        super();
        RandomDieValue = new RandomNum(seed);
        showingDice = false;
		
        TheDice = new Die[5];
        for(int idx = 0; idx < 5; idx++)
        {
            TheDice[idx] = new Die(RandomDieValue.getNum(), idx, canCheat);
            
            final Die theDie = TheDice[idx];
            theDie.addMouseListener(new MouseAdapter() 
            {
                public void mouseEntered(MouseEvent e)
                {
                    theDie.setBounds(theDie.getLocation().x,
                            theDie.getLocation().y - 3, 100, 100);
                    repaint();
                }
                public void mouseExited(MouseEvent e)
                {
                    theDie.setBounds(theDie.getLocation().x,
                            theDie.getLocation().y + 3, 100, 100);
                    repaint();
                }
            });
        }
        
        soundFiles1 = new String[1];
        soundFiles1[0] = new String("putDie.wav");
        
        soundFiles2 = new String[2];
        soundFiles2[0] = new String("removeDie1.wav");
        soundFiles2[1] = new String("removeDie2.wav");
        RemoveSound = new SoundLib(soundFiles2);
        
        setOpaque(false);
        setLayout(null);
        setBounds(0,0,800,115);
        validate();
	}
    
    /**
    *   @return a reference to a Die object contained in the array TheDice
    *
    *   @param index is the index of the Die to be returned
    **/
    public Die getDie(int index)
    {
        return TheDice[index];
    }
    
    /**
    *   add the dice to this panel using animation
    **/
    private void addDice()
    {
        removeAll();
        for(int idx = 0; idx < 5; idx++)
        {
            add(TheDice[idx]);
            TheDice[idx].putImage();
            slideDieIn(idx);
        }
        
        revalidate();
        showingDice = true;
    }
    
    /**
    *   Move a die upwards into position. Timed using a worker thread.
    *   The start of the animation is delayed more for each successive
    *   die to be animated, starting at no delay for the first die.
    *
    *   @param index is the index of the Die in TheDice[] to animate
    **/
    private void slideDieIn(final int index)
    {
        if(!isDieHeld(index))
        {
            int count = 0;
            
            //gather a count of the held dice for proper delay timing
            for(int idx = 0; idx < index; idx++)
            {
                if(isDieHeld(idx))
                    count++;
            }
            
            final int numHeld = count;
            
            SwingWorker animateDie = new SwingWorker(){
                protected String doInBackground()
                {
                    try
                    {   //delay the start of the animation based on the index
                        //and number of held dice
                        Thread.sleep(100*(index-numHeld));
                    }
                    catch(Exception ex)
                    {
                        System.out.println(
                            "Dice.java slideDieIn() Exception1: sleep failed");
                    }
            
                    Sound = new SoundLib(soundFiles1);
                    Sound.playSound("putDie.wav");
                    for(int idx = 0; idx < 20; idx++)
                    {
                        try
                        {
                            Thread.sleep(12);
                        }
                        catch(Exception ex)
                        {
                          System.out.println(
                            "Dice.java slideDieIn() Exception2: sleep failed");
                        }
                        process(new String[1], idx);
                    }
                    return null;
                }
                protected void process(String[] stuff, int idx)
                {
                    TheDice[index].setBounds(150+100*index,105-5*idx,100,100);
                    repaint();
                }
            };
            animateDie.execute();
        }
    }
    
    /**
    *   Move a die downward off the board using a worker thread for animation
    *   timing.
    *
    *   @param index is the index of the Die in TheDice[] to animate
    *   
    *   @param allDice determines whether to ignore the held flag in order
    *   to remove all of the dice
    **/
    private void slideDieOut(final int index, boolean allDice)
    {
        if(allDice || !isDieHeld(index))
        {
            RemoveSound.playSound("removeDie1.wav");
            SwingWorker animateDie = new SwingWorker(){
                protected String doInBackground()
                {
                    for(int idx = 0; idx < 20; idx++)
                    {
                        try
                        {
                            Thread.sleep(12);
                        }
                        catch(Exception ex)
                        {
                          System.out.println(
                            "Dice.java slideDieOut() Exception2: sleep failed");
                        }
                        process(new String[1], idx);
                    }
                    return null;
                }
                protected void process(String[] stuff, int idx)
                {
                    TheDice[index].setBounds(150+100*index,6*idx+10,100,100);
                    repaint();
                }
            };
            animateDie.execute();
        }
    }
    
    /**
    *   Remove the dice from this panel using animation
    *
    *   @param allDice determines whether to ignore the held flag in order
    *   to remove all of the dice
    **/
    private void removeDice(boolean allDice)
    {
        for(int idx = 0; idx < 5; idx++)
            slideDieOut(idx, allDice);
            
        showingDice = false;
    }
    
    /**
    *   Main method to control dice animation
    *
    *   @param endTurn determines whether the dice reappear if they are taken
    *   off of the game board
    **/
    public void animateDice(boolean endTurn)
    {
        if(endTurn)
            removeDice(true);
        else if(showingDice)
        {
            removeDice(false);
            //wait for dice removal animation to complete before adding
            //them again
            SwingWorker timer = new SwingWorker()
            {
                protected String doInBackground()
                {
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch(Exception ex)
                    {
                      System.out.println(
                        "Dice.java animateDice() Exception1: sleep failed");
                    }
                    return null;
                }
                protected void done()
                {
                    addDice();
                }
            };
            timer.execute();
        }
        else
            addDice();
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
		TheDice[location].setNumber(newValue, false);
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
    *   Generate new values for dice that are not held, but do not update the 
    *   images on the dice
    **/
    public void rollDice()
    {
        for(int idx = 0; idx < 5; idx++)
        {
            if(!TheDice[idx].isHeld())
            {
                TheDice[idx].setNumber(RandomDieValue.getNum(), false);
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
