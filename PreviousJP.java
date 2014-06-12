/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 22, 2014
* File:        PreviousJP.java
*******************************************************************************/

import javax.swing.*;
import java.awt.*;

/**
*   PreviousJP.java is a JPanel that displays information about the previous
*   player's turn using the CharacterImage class.
**/
public class PreviousJP extends JLayeredPane
{
    private CharacterImage ImageText;
    private int length;
    private JPanel HoldComponents;
    private JPanel Container;
    private JPanel LineHeader;
    private JPanel LineName;
    private JPanel LineCategory;
    private JPanel LineScore;
    private JLabel[] Header;
    private JLabel[] Name;
    private JLabel[] Category;
    private JLabel[] Score;
    private JLabel BG;
    private ImageIcon bg;

    /**
    *   Constructor initializes a CharacterImage object to translate text into
    *   images and prepares the JPanel for placement on the applet.
    **/
	public PreviousJP()
	{
        super();
        try{
        ImageText = new CharacterImage();
        
        HoldComponents = new JPanel();
        HoldComponents.setOpaque(false);
        HoldComponents.setBounds(0,0,455,141);
        HoldComponents.setLayout(new BoxLayout(HoldComponents, 
                BoxLayout.LINE_AXIS));
        Container = new JPanel();
        Container.setOpaque(false);
        Container.setLayout(new BoxLayout(Container, BoxLayout.PAGE_AXIS));
        LineHeader = new JPanel();
        LineHeader.setOpaque(false);
        LineHeader.setLayout(new BoxLayout(LineHeader, BoxLayout.LINE_AXIS));
        LineHeader.setPreferredSize(new Dimension(355,23));
        LineName = new JPanel();
        LineName.setOpaque(false);
        LineName.setLayout(new BoxLayout(LineName, BoxLayout.LINE_AXIS));
        LineName.setPreferredSize(new Dimension(355,23));
        LineCategory = new JPanel();
        LineCategory.setOpaque(false);
        LineCategory.setLayout(new BoxLayout(LineCategory,BoxLayout.LINE_AXIS));
        LineCategory.setPreferredSize(new Dimension(355,23));
        LineScore = new JPanel();
        LineScore.setOpaque(false);
        LineScore.setLayout(new BoxLayout(LineScore, BoxLayout.LINE_AXIS));
        LineScore.setPreferredSize(new Dimension(355,23));
        
        Container.add(Box.createRigidArea(new Dimension(0,7)));
        Container.add(LineHeader);
        Container.add(Box.createRigidArea(new Dimension(0,2)));
        Container.add(LineName);
        Container.add(Box.createRigidArea(new Dimension(0,2)));
        Container.add(LineCategory);
        Container.add(Box.createRigidArea(new Dimension(0,2)));
        Container.add(LineScore);
        Container.add(Box.createRigidArea(new Dimension(0,90)));

        HoldComponents.add(Box.createRigidArea(new Dimension(105,0)));
        HoldComponents.add(Container);
        
        setOpaque(false);
        setPreferredSize(new Dimension(455,141));
        add(HoldComponents, new Integer(3));
        
        BG = new JLabel();
        BG.setBounds(0,0,455,141);
        BG.setOpaque(false);
        bg = new ImageIcon(getClass().getResource("previousbg.png"));
        BG.setIcon(bg);
        add(BG, new Integer(2));
        
        Header = ImageText.getLetters("Previous Turn");
        length = Header.length;
        for(int idx = 0; idx < length; idx++)
            LineHeader.add(Header[idx]);
        LineHeader.add(Box.createRigidArea(new Dimension((355-(length*12)),0)));
        
        //need to fill the JPanels with something so they don't collapse
        LineName.add(Box.createRigidArea(new Dimension(0,23)));
        LineCategory.add(Box.createRigidArea(new Dimension(0,23)));
        LineScore.add(Box.createRigidArea(new Dimension(0,23)));
 
        validate();
        }catch(Exception ex){ex.printStackTrace();}
	}

    /**
    *   Display the name of the previous player.
    *
    *   @param name is the previous player's name
    **/
    public void setName(String name)
    {
        Name = ImageText.getLetters("Player: " + name);
        length = Name.length;
        LineName.removeAll();
        for(int idx = 0; idx < length; idx++)
            LineName.add(Name[idx]);
        LineName.add(Box.createRigidArea(new Dimension((355-(length*12)),0)));
        LineName.validate();
    }
    
    /**
    *   Display the previous player's category choice.
    *
    *   @param message is the category that the previous player chose
    **/
    public void setCategory(String message)
    {
        Category = ImageText.getLetters("Category: " + message);
        length = Category.length;
        LineCategory.removeAll();
        for(int idx = 0; idx < length; idx++)
            LineCategory.add(Category[idx]);
        LineCategory.add(Box.createRigidArea(
                new Dimension((355-(length*12)),0)));
        LineCategory.validate();
    }
    
    /**
    *   Display the score of the previous turn for the previous player.
    *
    *   @param message is the score of the previous player's turn
    **/
    public void setScore(String message)
    {
        Score = ImageText.getLetters("Scored: " + message);
        length = Score.length;
        LineScore.removeAll();
        for(int idx = 0; idx < length; idx++)
            LineScore.add(Score[idx]);
        LineScore.add(Box.createRigidArea(new Dimension((355-(length*12)),0)));
        LineScore.validate();
    }
}

