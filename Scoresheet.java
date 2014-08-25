/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        Scoresheet.java
*******************************************************************************/

import java.util.Scanner;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.border.*;
import java.applet.Applet;
import java.net.*;

/**
*   Scoresheet.java manages the scoring for a player. It uses parallel arrays
*   to hold category names and the score for each category.
**/
public class Scoresheet extends JPanel
{
    private String[] Categories;
    private int[] Scores;
    private boolean playerUsingThisApplet;
    private boolean hasYahtzee = false;
    private int yahtzeeBonus = 0;
    private boolean hasUBonus = false;
    private boolean[] CategoryScored;
    private boolean isGoodScore;
    private CharacterImage ImageScore = new CharacterImage();
    private SoundLib Sounds;
    private String[] SoundFiles;
    private JButton muteButton;
    
    private Image background;
    public ImageIcon[] CategoryImg, CategoryBorderImg;
    private JButton[] ButtonArray;
    private JPanel[] TextScoreArray;
    private JPanel[] TextPotentialArray;
    
    private JLabel LblCategory;
    private JLabel LblScore;
    private JLabel LblPotential;
    private JLabel NumYahtzees;
    private JLabel NumYahtzeesCtr;
    private JLabel LblPadding;
    private JLabel LblPadding2;
    private JLabel UpperBonus;
    private ImageIcon CategoryLabel;
    private ImageIcon YourScore;
    private ImageIcon Potential;
    private ImageIcon NumYahtzeesLabel;
    private ImageIcon NumYahtzeesCtrLabel;
    private ImageIcon UpperBonusLabel;
    
	/**
    *   Constructor initializes the category arrays, score arrays, and 
    *       JComponents displayed by the scoresheet.
    **/
	public Scoresheet(boolean isUsingThisApp, JButton mute)
	{
        super();
        try{
        playerUsingThisApplet = isUsingThisApp;
        muteButton = mute;
        Categories = new String[14];
        Scores = new int[13];
        CategoryScored = new boolean[13];
        ButtonArray = new JButton[14];
        TextScoreArray = new JPanel[13];
        TextPotentialArray = new JPanel[13];
        
        Categories[0] = "Ones";
        Categories[1] = "Twos";
        Categories[2] = "Threes";
        Categories[3] = "Fours";
        Categories[4] = "Fives";
        Categories[5] = "Sixes";
        Categories[6] = "3 of a Kind";
        Categories[7] = "4 of a Kind";
        Categories[8] = "Full House";
        Categories[9] = "Small Straight";
        Categories[10] = "Large Straight";
        Categories[11] = "Yahtzee";
        Categories[12] = "Chance";
        Categories[13] = "No Score";
        
        CategoryImg = new ImageIcon[14];
        CategoryBorderImg = new ImageIcon[14];
        for(int idx = 0; idx < 14; idx++)
        {
            String file = new String("Score"+idx+".png");
            CategoryImg[idx] = new ImageIcon(getClass().getResource(file));
            file = new String("Score"+idx+"Border.png");
            CategoryBorderImg[idx] = new ImageIcon(getClass().getResource(file));
        }
        
        for(int idx = 0; idx < 13; idx++)
        {
            Scores[idx] = 0;
            CategoryScored[idx] = false;
        }
        
        setLayout(new GridLayout(16,3));
        
        createBtnLbl();
        disableCategories();
        addBtnLbl();

        setPreferredSize(new Dimension(345,443));
        setOpaque(false);

        try
        {
            background = ImageIO.read(getClass().getResourceAsStream(
                    "scorebg.png"));
        }
        catch (IOException ex)
        {
            System.out.println(
                    "Error reading background image in Scoresheet.java");
        }
        
        SoundFiles = new String[3];
        SoundFiles[0] = new String("buttonHover1.wav");
        SoundFiles[1] = new String("buttonHover2.wav");
        SoundFiles[2] = new String("buttonHover3.wav");
        Sounds = new SoundLib(SoundFiles);
        
        } catch (Exception e) { e.printStackTrace();}
	}
    
    /**
    *   Draw the background border image on the Scorecard
    **/
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

    /**
    *   Initialize the JComponents used by the scoresheet and add images
    **/
    private void createBtnLbl()
    {
        LblCategory = new JLabel();
        LblScore = new JLabel();
        LblPotential = new JLabel();

        CategoryLabel = new ImageIcon(getClass()
                .getResource("Category.png"));
        YourScore = new ImageIcon(getClass()
                .getResource("YourScore.png"));
        Potential = new ImageIcon(getClass()
                .getResource("PotentialScore.png"));
        LblCategory.setIcon(CategoryLabel);
        LblScore.setIcon(YourScore);
        LblPotential.setIcon(Potential);
        
        for(int idx = 0; idx < 14; idx++)
        {
            final JButton currentButton = new JButton();
            final ImageIcon[] Cat = CategoryImg;
            final ImageIcon[] CatBorder = CategoryBorderImg;
            final int index = idx;
        
            if(playerUsingThisApplet)
            {
                currentButton.addMouseListener(new MouseAdapter() 
                {
                    public void mouseEntered(MouseEvent e)
                    {
                        if(currentButton.isEnabled())
                        {
                            currentButton.setIcon(CatBorder[index]);
                            if(muteButton.isEnabled())
                                Sounds.playRandom();
                        }
                    }
                    public void mouseExited(MouseEvent e)
                    {
                        currentButton.setIcon(Cat[index]);
                    }
                    public void mousePressed(MouseEvent e)
                    {
                        currentButton.setIcon(Cat[index]);
                    }
                });
            }
            
            ButtonArray[idx] = currentButton;
            ButtonArray[idx].setBorder(BorderFactory.createEmptyBorder());
            ButtonArray[idx].setIcon(CategoryImg[idx]);
            ButtonArray[idx].setContentAreaFilled(false);
        }
        
        for(int idx = 0; idx < 13; idx++)
        {
            TextScoreArray[idx] = new JPanel();
            TextPotentialArray[idx] = new JPanel();
            TextScoreArray[idx].setOpaque(false);
            TextPotentialArray[idx].setOpaque(false);
            TextScoreArray[idx].setLayout(new BoxLayout(
                    TextScoreArray[idx], BoxLayout.LINE_AXIS));
            TextPotentialArray[idx].setLayout(new BoxLayout(
                    TextPotentialArray[idx], BoxLayout.LINE_AXIS));
            
        }

        NumYahtzees = new JLabel();
        NumYahtzeesCtr = ImageScore.getLetters("0")[0];
        LblPadding = new JLabel("        ");
        LblPadding2 = new JLabel("        ");
        UpperBonus = new JLabel();
        
        NumYahtzeesLabel = new ImageIcon(getClass()
                .getResource("YahtzeeBonus.png"));
        UpperBonusLabel = new ImageIcon(getClass()
                .getResource("Meter0.png"));
        NumYahtzees.setIcon(NumYahtzeesLabel);
        UpperBonus.setIcon(UpperBonusLabel);
    }
    
    /**
    *   Add JComponents to the scoresheet.
    **/
    private void addBtnLbl()
    {
        add(LblCategory);
        add(LblScore);
        add(LblPotential);
        
        for(int idx = 0; idx < 14; idx++)
        {
            add(ButtonArray[idx]);
            
            if(idx < 13)
            {
                //ImageScore.getNumber() returns a new array; must create a
                //variable for each JLabel since a JLabel can only be added
                //to a component once
                JLabel[] BlankScore1 = ImageScore.getNumber(0);
                JLabel[] BlankScore2 = ImageScore.getNumber(0);
                add(TextScoreArray[idx]);
                add(TextPotentialArray[idx]);
                TextScoreArray[idx].add(BlankScore1[0]);
                TextPotentialArray[idx].add(BlankScore2[0]);
            }
        }

        add(LblPadding);
        add(UpperBonus);
        add(LblPadding2);
        add(NumYahtzees);
        add(NumYahtzeesCtr);
        
        validate();
    }
    
    /**
    *   Set a flag for the given index indicating that a category has been
    *       scored.
    **/
    public void usedCategory(int index)
    {
        CategoryScored[index] = true;
    }
    
    /**
    *   Enable the JButtons used by the scoresheet if they have not already
    *       been scored.
    **/
    public void enableCategories()
    {
        for(int idx = 0; idx < 13; idx++)
        {
            if(!CategoryScored[idx])
                ButtonArray[idx].setEnabled(true);
        }

        //no score button is always a choice
        ButtonArray[13].setEnabled(true);
    }
    
    /**
    *   Disable the JButtons used by the scoresheet
    **/
    public void disableCategories()
    {
        for(int idx = 0; idx < 14; idx++)
        {
            ButtonArray[idx].setEnabled(false);
        }
    }
	
	/**
    *   Set the score for a category and update the images on the scoresheet.
    *   If the player scores a yahtzee, set the hasYahtzee flag and 
    *   increment the yahtzee bonus counter
    *
	*   @param index of the Score array to change
    *
    *   @param score to insert
    *
    *   @return boolean that determines whether the player recieves an 
    *           additional turn because of multiple yahtzees
    **/
	public boolean setScore(int index, int score)
	{
		Scores[index] = score;
        JLabel[] ImgScores;
        int length;
        boolean scoredAnotherYahtzee = false;
        
        if(index == 11 && score == 50)
        {
            if(hasYahtzee)
            {
                yahtzeeBonus++;
                remove(NumYahtzeesCtr);
                NumYahtzeesCtr = ImageScore.getLetters(""+yahtzeeBonus)[0];
                add(NumYahtzeesCtr);
                scoredAnotherYahtzee = true;
            }
            
            hasYahtzee = true;
        }
        
        ImgScores = ImageScore.getNumber(score);
        length = ImgScores.length;
        TextScoreArray[index].removeAll();
        
        for(int idx = 0; idx < length; idx++)
            TextScoreArray[index].add(ImgScores[idx]);
            
        TextScoreArray[index].revalidate();
        
        return scoredAnotherYahtzee;
	}
    
    /**
    *   Calculate the score for a category based on the current state
    *   of the dice. If a category has already been scored, the potential
    *   score will not appear (except for the yahtzee category if it has
    *   not been scored as 0).
    *
	*   @param index of the category used to determine the score
    *
    *   @param DieValues is a string containing the five Die values
    **/
	public void setPotentialScores(int index, String DieValues)
	{
        JLabel[] ImgScores;
        int length;
        
        ImgScores = ImageScore.getNumber(calculateScore(index, DieValues));
        length = ImgScores.length;
        TextPotentialArray[index].removeAll();
        TextPotentialArray[index].revalidate();
        TextPotentialArray[index].repaint();
        for(int idx = 0; idx < length; idx++)
        {
            if(!CategoryScored[index])
                TextPotentialArray[index].add(ImgScores[idx]);
        }
        TextPotentialArray[index].revalidate();
	}
    
    /**
    *   Clear the potential score column
    **/
	public void hidePotentialScores()
	{
        for(int index = 0; index < 13; index++)
        {
            TextPotentialArray[index].removeAll();
            TextPotentialArray[index].revalidate();
            TextPotentialArray[index].repaint();
        }
	}
    
    /**
    *   Return the name of a category at a given index in the Categories
    *   array.
    *
	*   @param index of the category name to retrieve
    *
    *   @return the name of the category
    **/
    public String getCategoryName(int index)
    {
        return Categories[index];
    }
    
    /**
    *   @return whether a player has scored the upper bonus
    **/
    public boolean hasUpperBonus()
    {
        return hasUBonus;
    }
    
    /**
    *   @return the number of yahtzee bonuses a player has earned
    **/
    public int getYahtzeeBonus()
    {
        return yahtzeeBonus;
    }
    
    /**
    *   Return the ImageIcon for the category at the given index
    *
    *   @param idx in the index of the category to retrieve
    *
    *   @return the ImageIcon for the category
    **/
    public ImageIcon getCategoryImg(int idx)
    {
        return CategoryImg[idx];
    }
    
    /**
    *   Determine the score granted for a category given the state of the dice
    *
	*   @param index of the category to score
    *
    *   @param DieValues is the string of die values
    *
    *   @return score for the input category index (-1 if index is not in the
    *           array of categories)
    **/
	public int calculateScore(int index, String DieValues)
	{
        int score = 0;
        int[] values = new int[5];
        int[] valueCount = new int[6];
        Scanner parseDieValues = new Scanner(DieValues);
        isGoodScore = false;
		
        //initialize valueCount array
        for(int idx = 0; idx < 6; idx++)
        {
            valueCount[idx] = 0;
        }
        
        //parse the string of die values into an array and
        //increment a counter for each value
        for(int idx = 0; idx < 5; idx++)
        {
            int num = parseDieValues.nextInt();
            values[idx] = num;
            valueCount[(num - 1)]++;
        }
        
        switch(index)
        {
            //ones
            case 0:
                for(int idx = 0; idx < 5; idx++)
                {
                    if(values[idx] == 1)
                    {
                        score++;
                    }
                }
                if(score > 3)
                    isGoodScore = true;
                return score;
                
            //twos
            case 1:
                for(int idx = 0; idx < 5; idx++)
                {
                    if(values[idx] == 2)
                    {
                        score += 2;
                    }
                }
                if(score > 6)
                    isGoodScore = true;
                return score;
                
            //threes
            case 2:
                for(int idx = 0; idx < 5; idx++)
                {
                    if(values[idx] == 3)
                    {
                        score += 3;
                    }
                }
                if(score > 9)
                    isGoodScore = true;
                return score;
                
            //fours
            case 3:
                for(int idx = 0; idx < 5; idx++)
                {
                    if(values[idx] == 4)
                    {
                        score += 4;
                    }
                }
                if(score > 12)
                    isGoodScore = true;
                return score;
                
            //fives
            case 4:
                for(int idx = 0; idx < 5; idx++)
                {
                    if(values[idx] == 5)
                    {
                        score += 5;
                    }
                }
                if(score > 15)
                    isGoodScore = true;
                return score;
                
            //sixes
            case 5:
                for(int idx = 0; idx < 5; idx++)
                {
                    if(values[idx] == 6)
                    {
                        score += 6;
                    }
                }
                if(score > 18)
                    isGoodScore = true;
                return score;
            
            //3 of a kind
            case 6:
                for(int idx = 0; idx < 6; idx++)
                {
                    if(valueCount[idx] >= 3)
                    {
                        score = 3 * (idx + 1);
                    }
                }
                if(score > 14)
                    isGoodScore = true;
                return score;
            
            //4 of a kind
            case 7:
                for(int idx = 0; idx < 6; idx++)
                {
                    if(valueCount[idx] >= 4)
                    {
                        score = valueCount[idx] * (idx + 1);
                    }
                }
                if(score > 15)
                    isGoodScore = true;
                return score;
            
            //full house
            case 8:
                for(int idx = 0; idx < 6; idx++)
                {
                    //check for 3 of a kind
                    if(valueCount[idx] == 3)
                    {
                        for(int subIdx = 0; subIdx < 6; subIdx++)
                        {
                            //check for a pair
                            if(valueCount[subIdx] == 2)
                            {
                                score = 25;
                            }
                        }
                    }
                }
                if(score == 25)
                    isGoodScore = true;
                return score;
            
            //small straight
            case 9:
                if(valueCount[0] >= 1 && valueCount[1] >= 1 && 
                    valueCount[2] >= 1 && valueCount[3] >= 1)
                {
                    score = 30;
                }
                else if(valueCount[1] >= 1 && valueCount[2] >= 1 && 
                    valueCount[3] >= 1 && valueCount[4] >= 1)
                {
                    score = 30;
                }
                else if(valueCount[2] >= 1 && valueCount[3] >= 1 && 
                    valueCount[4] >= 1 && valueCount[5] >= 1)
                {
                    score = 30;
                }
                if(score == 30)
                    isGoodScore = true;
                return score;
            
            //large straight
            case 10:
                if(valueCount[0] >= 1 && valueCount[1] >= 1 && 
                    valueCount[2] >= 1 && valueCount[3] >= 1 && 
                    valueCount[4] >= 1)
                {
                    score = 40;
                }
                else if(valueCount[1] >= 1 && valueCount[2] >= 1 && 
                    valueCount[3] >= 1 && valueCount[4] >= 1 && 
                    valueCount[5] >= 1)
                {
                    score = 40;
                }
                if(score == 40)
                    isGoodScore = true;
                return score;
            
            //yahtzee
            case 11:
                for(int idx = 0; idx < 6; idx++)
                {
                    if(valueCount[idx] == 5)
                    {
                        score = 50;
                    }
                }
                if(score == 50)
                    isGoodScore = true;
                return score;
            
            //chance
            case 12:
                for(int idx = 0; idx < 5; idx++)
                {
                    score += values[idx];
                }
                if(score > 23)
                    isGoodScore = true;
                return score;
        }
        
        //bad index value
        return -1;
	}
    
    /**
    *   @return whether the previously scored category was scored with a 
    *       "good" score. Used by a Human to play sounds.
    **/
    public boolean wasGoodScore()
    {
        return isGoodScore;
    }
    
    /**
    *   Get the score for a category 
    *
    *   @param index of the category to return
    *
	*   @return score value for a category
    **/
	public int getScore(int index)
	{
		return Scores[index];
	}

    /**
    *   Calculate the total score for a player, including bonuses
    *
	*   @return the total score
    **/
    public int getTotalScore()
    {
        int totalScore = 0;
        
        for(int idx = 0; idx < 6; idx++)
        {
            totalScore += getScore(idx);
        }
        
        //determine if upper bonus achieved and update bonus meter
        if(totalScore >= 63)
        {
            totalScore += 35;
            UpperBonus.setIcon(new ImageIcon(getClass()
                .getResource("Meter10.png")));
            hasUBonus = true;
        }
        else if(totalScore < 60)
        {
            String file = new String("Meter"+totalScore/6+".png");
            UpperBonus.setIcon(new ImageIcon(getClass().getResource(file)));
        }
        else
        {
            UpperBonus.setIcon(new ImageIcon(getClass()
                .getResource("Meter9.png")));
        }
            
        for(int idx = 6; idx < 13; idx++)
        {
            totalScore += getScore(idx);
        }
        
        //add yahtzee bonus
        if(yahtzeeBonus > 0)
            totalScore += (yahtzeeBonus * 100);
        
        return totalScore;
    }
}
