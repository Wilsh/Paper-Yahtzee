/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        Die.java
*******************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
*   Die.java holds an integer between 1 and 6 to represent the value of a die
**/
public class Die extends JButton
{
    private int number;
    private int indexInDice;
    private ImageIcon[] Faces;
    private ImageIcon[] Disabled;
    private boolean held = false;
    private JPopupMenu popup;
    private JMenuItem menuItem;
	private SoundLib Click;
    private String[] sound;
    
	/**
    *   Constructor initializes the value on the die, loads images that are
    *       associated with each value, and adds an action listener to this
    *       object.
    *
    *   @param initVal is the initial value used as the number on the die
    *
    *   @param idx is the index of this object as it exists in the array
    *       of Die objects in the Dice class. Used for transmitting hold
    *       information in networked play.
    **/
	public Die(int initVal, int idx)
	{
        super();
        try{
		number = initVal;
        indexInDice = idx;
        sound = new String[1];
        sound[0] = new String("smallClick.wav");
        Click = new SoundLib(sound);
        
        Faces = new ImageIcon[6];
        Faces[0] = new ImageIcon(getClass().getResource("Die1.png"));
        Faces[1] = new ImageIcon(getClass().getResource("Die2.png"));
        Faces[2] = new ImageIcon(getClass().getResource("Die3.png"));
        Faces[3] = new ImageIcon(getClass().getResource("Die4.png"));
        Faces[4] = new ImageIcon(getClass().getResource("Die5.png"));
        Faces[5] = new ImageIcon(getClass().getResource("Die6.png"));
        
        Disabled = new ImageIcon[6];
        Disabled[0] = new ImageIcon(getClass().getResource("Die1disabled.png"));
        Disabled[1] = new ImageIcon(getClass().getResource("Die2disabled.png"));
        Disabled[2] = new ImageIcon(getClass().getResource("Die3disabled.png"));
        Disabled[3] = new ImageIcon(getClass().getResource("Die4disabled.png"));
        Disabled[4] = new ImageIcon(getClass().getResource("Die5disabled.png"));
        Disabled[5] = new ImageIcon(getClass().getResource("Die6disabled.png"));
        
        putImage();
        createCheatPopup();
        
        this.addMouseListener(new MouseAdapter() 
            {
                public void mousePressed(MouseEvent e)
                {
                    if(SwingUtilities.isLeftMouseButton(e))
                    {
                        Click.playSound("smallClick.wav");
                        hold();
                    }
                    else
                    {
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        );
        
        setPreferredSize(new Dimension(100,100));
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);
        setBackground(new Color(0,0,0,0));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        }catch (Exception e) { e.printStackTrace();}
	}
    
    /**
    *   @return the index of this object as it exists in the array
    *       of Die objects in the Dice class.
    **/
    public int getDieIndex()
    {
        return indexInDice;
    }
	
    /**
    *   Toggle the held flag for the die
    **/
    public void hold()
    {
        if(this.isEnabled())
        {
            this.setEnabled(false);
            held = true;
        }
        else
        {
            this.setEnabled(true);
            held = false;
        }
    }
    
    /**
	*   @return whether the die is held
    **/
    public boolean isHeld()
    {
        return held;
    }
    
    /**
    *   Set the held flag on the die to "not held"
    **/
    public void resetHold()
    {
        this.setEnabled(true);
        held = false;
    }
    
    /**
    *   Place the image corresponding to the die's value on the die
    **/
    public void putImage()
    {
        setIcon(Faces[number - 1]);
        setDisabledIcon(Disabled[number - 1]);
    }
  
	/**
    *   Set the number on the die
    *
	*   @param newValue is the new number
    *
    *   @param addImage determines whether the new number is displayed 
    *   automatically
    **/
	public void setNumber(int newValue, boolean addImage)
	{
		number = newValue;
        if(addImage)
            putImage();
        validate();
	}
    
    /**
    *   Get the number on the die
    *
    *   @return the generated number
    **/
	public int getNumber()
	{
		return number;
	}
    
    /**
    *   Create a popup menu for selecting a value to display on the die and
    *   add actionListeners to set the new value.
    **/
    private void createCheatPopup()
    {
        popup = new JPopupMenu();
        for(int num = 1; num < 7; num++)
        {
            final int theNum = num;
            menuItem = new JMenuItem(""+num);
            menuItem.addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        setNumber(theNum, true);
                    }
                }
            );
            popup.add(menuItem);
        }
    }
}
