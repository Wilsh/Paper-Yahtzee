/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        Player.java
*******************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

/**
*   Player.java is the base class for Human and Robot. It contains the 
*   scoresheet, the player's name, and the number of turns remaining in
*   the game. This class contains abstract methods whose implementations will 
*   vary depending on whether the player is a human or computer.
**/
public abstract class Player extends JPanel
{
    protected Scoresheet MyScore;
    protected JPanel PopupScores;
    protected String playerName;
    private int numTurnsLeft;
    protected JLabel LblName;
    protected JLabel LblScore;
    protected Yahtzee app;
    protected int lastCategory;
    protected int lastScore;
    protected CharacterImage ImageGet = new CharacterImage();
    protected JLabel[] ImgName;
    protected int length;
    protected boolean playerUsingThisApplet;
    private SoundLib Sounds;
    private String[] SoundFiles;
    
	/**
    *   Constructor initializes the scoresheet, the player's name, the 
    *   number of turns remaining in the game, and adds an action listener
    *   to the class itself
    *
    *   @param name of the player
    *
    *   @param a is the main Yahtzee class
    *
    *   @param isUsingThisApp is a boolean whether the player being instantiated
    *           is controlling this applet
    **/
	protected Player(String name, Yahtzee a, boolean isUsingThisApp)
    {
        super();
        playerUsingThisApplet = isUsingThisApp;
        MyScore = new Scoresheet(playerUsingThisApplet, a.muteButton);
        playerName = name;
        numTurnsLeft = 13;
        setLayout(new GridLayout(0,1));
        app = a;
        
        SoundFiles = new String[2];
        SoundFiles[0] = new String("playerHover.wav");
        SoundFiles[1] = new String("smallClick.wav");
        Sounds = new SoundLib(SoundFiles);
        
        this.addMouseListener(new MouseAdapter() 
            {
                public void mouseEntered(MouseEvent e)
                {
                    setBorder(BorderFactory.createLoweredBevelBorder());
                    if(app.muteButton.isEnabled())
                        Sounds.playSound("playerHover.wav");
                }
                public void mouseExited(MouseEvent e)
                {
                    setBorder(BorderFactory.createEmptyBorder());
                }
                public void mousePressed(MouseEvent e)
                {
                    setBorder(BorderFactory.createEmptyBorder());
                }
                public void mouseClicked(MouseEvent e)
                {
                    if(app.muteButton.isEnabled())
                        Sounds.playSound("smallClick.wav");
                    showPopupScores();
                }
            }
        );
        setOpaque(false);
        
        LblName = new JLabel();
        LblName.setOpaque(false);
        LblName.setLayout(new BoxLayout(
                    LblName, BoxLayout.LINE_AXIS));
        LblName.setPreferredSize(new Dimension((12*name.length()),23));
        //transform name into an image
        ImgName = ImageGet.getLetters(name);
        length = ImgName.length;        
        for(int idx = 0; idx < length; idx++)
            LblName.add(ImgName[idx]);
        
        add(LblName);
        
        LblScore = new JLabel();
        LblScore.setOpaque(false);
        LblScore.setLayout(new BoxLayout(
                    LblScore, BoxLayout.LINE_AXIS));
        //make sure LblScore is wide enough to show all characters
        //(12px * 7 char in "Score: ") + (14px * 3 char max in [player's score])
        LblScore.setPreferredSize(new Dimension((12*7+14*3)+7,23));
        //transform score into an image
        ImgName = ImageGet.getLetters("Score: 0");
        length = ImgName.length;        
        for(int idx = 0; idx < length; idx++)
            LblScore.add(ImgName[idx]);
        
        add(LblScore);
    }
    
    /**
    *   Show this player's scoresheet in a dialog panel
    **/
    public void showPopupScores()
    {
        JLayeredPane Window = new JLayeredPane();
        Window.setPreferredSize(new Dimension(225,443));
        PopupScores = new JPanel(new GridLayout(16,2));
        PopupScores.setOpaque(false);
        PopupScores.setPreferredSize(new Dimension(225,443));
        
        //create scoresheet labels
        JLabel LblCategory = new JLabel();
        JLabel LblScore = new JLabel();
        ImageIcon CategoryLabel = new ImageIcon(getClass()
                .getResource("Category.png"));
        ImageIcon YourScore = new ImageIcon(getClass()
                .getResource("YourScore.png"));
        LblCategory.setIcon(CategoryLabel);
        LblCategory.setOpaque(false);
        LblScore.setIcon(YourScore);
        LblScore.setOpaque(false);
        JLabel[] LabelArray = new JLabel[13];
        JPanel[] ScoreArray = new JPanel[13];
        for(int idx = 0; idx < 13; idx++)
        {
            LabelArray[idx] = new JLabel();
            LabelArray[idx].setIcon(MyScore.getCategoryImg(idx));
            LabelArray[idx].setOpaque(false);
        }
        
        //add components to popup JPanel
        PopupScores.add(LblCategory);
        PopupScores.add(LblScore);
        for(int idx = 0; idx < 13; idx++)
        {
            ScoreArray[idx] = new JPanel();
            ScoreArray[idx].setOpaque(false);
            
            int score = MyScore.getScore(idx);
            //create score labels
            JLabel[] BlankScore = ImageGet.getNumber(score);
            for(int index = 0; index < BlankScore.length; index++)
            {
                ScoreArray[idx].add(BlankScore[index]);
            }
                
            PopupScores.add(LabelArray[idx]);
            PopupScores.add(ScoreArray[idx]);
        }
        //labels at bottom of scoresheet
        JLabel NumYahtzees = new JLabel();
        JLabel NumYahtzeesCtr = ImageGet.getLetters(""+
                MyScore.getYahtzeeBonus())[0];
        NumYahtzeesCtr.setOpaque(false);
        JLabel LblPadding = new JLabel("        ");
        LblPadding.setOpaque(false);
        JLabel UpperBonus = new JLabel();
        ImageIcon NumYahtzeesLabel = new ImageIcon(getClass()
                .getResource("YahtzeeBonus.png"));
        ImageIcon UpperBonusLabel;
        if(MyScore.hasUpperBonus())
        {
            UpperBonusLabel = new ImageIcon(getClass()
                    .getResource("UpperBonusChecked.png"));
        }
        else
        {
            UpperBonusLabel = new ImageIcon(getClass()
                    .getResource("UpperBonusUnchecked.png"));
        }
        NumYahtzees.setIcon(NumYahtzeesLabel);
        NumYahtzees.setOpaque(false);
        UpperBonus.setIcon(UpperBonusLabel);
        UpperBonus.setOpaque(false);
        PopupScores.add(UpperBonus);
        PopupScores.add(LblPadding);
        PopupScores.add(NumYahtzees);
        PopupScores.add(NumYahtzeesCtr);

        //background image
        JLabel BG = new JLabel();
        ImageIcon bg = new ImageIcon(getClass().getResource("paperpopup.png"));
        BG.setOpaque(false);
        BG.setIcon(bg);
        BG.setBounds(0, 0, 225, 443);
        PopupScores.setBounds(0, 0, 225, 443);
        Window.add(BG, new Integer(2));
        Window.add(PopupScores, new Integer(3));
        
        Window.validate();
        JOptionPane.showMessageDialog(null, Window, 
                "Scoresheet for " + playerName, JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
    *   Override the paintComponent method to draw a rectangle around the 
    *   player in control of this applet.
    **/
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        if(playerUsingThisApplet)
        {
            Dimension bounds = this.getSize();
            Double x = bounds.getWidth();
            Double y = bounds.getHeight();
            g.drawRect(0, 0, x.intValue()-1, y.intValue()-1);
        }
    }
    
    /**
    *   Call method that runs when the roll button is clicked (only used by
    *   a robot).
    **/
    public abstract void simulateRollButton();
	
	/**
    *   Get the player's choice for the roll menu (used only by a robot).
    *
    *   @param TheDice are needed by a computer player to make a choice
    **/
	public abstract void getRollChoice(Dice TheDice);
    
    /**
    *   Enter the score for a category based on the current state
    *   of the dice.
    *
    *   @param DieValues is a string containing the five Die values
    **/
    public abstract void setCategory(int index, String DieValues);
    
    /**
    *   Enter a value given by a player for a category.
    **/
    public abstract void cheatCategory(int index, int score);
    
    /**
    *   Choose which dice to hold during a turn
    **/
    public abstract void chooseHolds(Dice TheDice);
    
    /**
    *   Calculate the score for each category based on the current state
    *   of the dice.
    *
    *   @param DieValues is a string containing the five Die values
    **/
    public void showPotential(String DieValues)
    {
        for(int idx = 0; idx < 13; idx++)
            MyScore.setPotentialScores(idx, DieValues);
    }
    
    /**
    *   Clear the potential score column on the Scoresheet
    **/
    public void hidePotential()
    {
        MyScore.hidePotentialScores();
    }
    
    /**
	*   @return the number of turns remaining in the game for this player
    **/
    public int getNumTurns()
    {
        return numTurnsLeft;
    }
    
    /**
	*   Increment the number of turns remaining in the game for this player
    **/
    public void addTurn()
    {
        numTurnsLeft++;
    }
    
    /**
	*   Decrement the number of turns remaining in the game for this player
    **/
    public void removeTurn()
    {
        numTurnsLeft--;
    }
    
    /**
	*   @return this player's name
    **/
    public String getName()
    {
        return playerName;
    }
    
    /**
	*   @return the player's score at the end of the game
    **/
    public int getFinalScore()
    {
        return MyScore.getTotalScore();
    }
    
    /**
	*   enable the button for categories that have not been scored
    **/
    public void enableCategories()
    {
        MyScore.enableCategories();
    }
    
    /**
	*   disable the buttons for each category
    **/
    public void disableCategories()
    {
        MyScore.disableCategories();
    }
    
    /**
	*   @return this player's scoresheet object
    **/
    public JPanel getScoresheet()
    {
        return MyScore;
    }
    
    /**
	*   update text on the score label displayed at the top of the game board
    **/
    public void updateScore()
    {
        LblScore.removeAll();
        //transform score into an image
        ImgName = ImageGet.getLetters("Score: " + getFinalScore());
        length = ImgName.length;        
        for(int idx = 0; idx < length; idx++)
            LblScore.add(ImgName[idx]);
    }
    
    /**
	*   @return this player's last category scored
    **/
    public String getLastCategory()
    {
        return MyScore.getCategoryName(lastCategory);
    }
    
    /**
	*   @return this player's last score entered
    **/
    public int getLastScore()
    {
        return lastScore;
    }
    
    /**
    *   Return the name of a category at a given index in the Categories
    *   array from the scoresheet.
    *
	*   @param index of the category name to retrieve
    *
    *   @return the name of the category
    **/
    public String getCategoryName(int index)
    {
        return MyScore.getCategoryName(index);
    }
}
