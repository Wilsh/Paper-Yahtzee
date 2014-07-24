/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 25, 2014
* File:        DialogJP.java
*******************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
*   DialogJP.java is a JPanel that displays information about the current
*   player's turn using the CharacterImage class and contains the roll button.
**/
public class DialogJP extends JLayeredPane
{
    private CharacterImage ImageText;
    private int length;
    private JPanel HoldComponents;
    private JPanel Container;
    private JPanel LinePlayer;
    private JPanel LineTurns;
    private JPanel LineAction;
    private JPanel LineButton;
    private JLabel[] FirstLine;
    private JLabel[] SecondLine;
    private JLabel[] Action;
    private JLabel BG;
    private ImageIcon bg;
    private ImageIcon[] RollImgs;
    private ImageIcon[] RolloverImgs;
    private JButton RollButton;
    private SoundLib Sounds;
    private String[] sound;

    /**
    *   Constructor initializes a CharacterImage object to translate text into
    *   images and prepares the JPanel for placement on the applet.
    **/
	public DialogJP(String name, int numTurns)
	{
        super();
        try{
        ImageText = new CharacterImage();
        
        sound = new String[1];
        sound[0] = new String("mouseOver.wav");
        Sounds = new SoundLib(sound);

        RollImgs = new ImageIcon[3];
        RollImgs[0] = new ImageIcon(getClass().getResource("rollButton1.png"));
        RollImgs[1] = new ImageIcon(getClass().getResource("rollButton2.png"));
        RollImgs[2] = new ImageIcon(getClass().getResource("rollButton3.png"));
        RolloverImgs = new ImageIcon[3];
        RolloverImgs[0] = new ImageIcon(getClass().getResource(
                "rollButtonActive1.png"));
        RolloverImgs[1] = new ImageIcon(getClass().getResource(
                "rollButtonActive2.png"));
        RolloverImgs[2] = new ImageIcon(getClass().getResource(
                "rollButtonActive3.png"));
        
        RollButton = getNewButton();
        
        HoldComponents = new JPanel();
        HoldComponents.setOpaque(false);
        HoldComponents.setBounds(0,0,455,261);
        HoldComponents.setLayout(new BoxLayout(HoldComponents, 
                BoxLayout.LINE_AXIS));
        
        Container = new JPanel();
        Container.setOpaque(false);
        Container.setLayout(new BoxLayout(Container, BoxLayout.PAGE_AXIS));
        LinePlayer = new JPanel();
        LinePlayer.setOpaque(false);
        LinePlayer.setLayout(new BoxLayout(LinePlayer, BoxLayout.LINE_AXIS));
        LinePlayer.setPreferredSize(new Dimension(355,23));
        LineTurns = new JPanel();
        LineTurns.setOpaque(false);
        LineTurns.setLayout(new BoxLayout(LineTurns, BoxLayout.LINE_AXIS));
        LineTurns.setPreferredSize(new Dimension(355,23));
        LineAction = new JPanel();
        LineAction.setOpaque(false);
        LineAction.setLayout(new BoxLayout(LineAction, BoxLayout.LINE_AXIS));
        LineAction.setPreferredSize(new Dimension(355,23));
        LineButton = new JPanel();
        LineButton.setOpaque(false);
        LineButton.setLayout(new BoxLayout(LineButton, BoxLayout.LINE_AXIS));
        LineButton.setPreferredSize(new Dimension(355,125));
        LineButton.add(RollButton);
        LineButton.add(Box.createRigidArea(new Dimension(20,0)));
        
        Container.add(Box.createRigidArea(new Dimension(0,14)));
        Container.add(LinePlayer);
        Container.add(Box.createRigidArea(new Dimension(0,2)));
        Container.add(LineTurns);
        Container.add(Box.createRigidArea(new Dimension(0,2)));
        Container.add(LineAction);
        Container.add(Box.createRigidArea(new Dimension(0,20)));
        Container.add(LineButton);
        Container.add(Box.createRigidArea(new Dimension(0,48)));
        
        HoldComponents.add(Box.createRigidArea(new Dimension(105,0)));
        HoldComponents.add(Container);
        
        setOpaque(false);
        //setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setPreferredSize(new Dimension(455,261));
        add(HoldComponents, new Integer(3));
        
        BG = new JLabel();
        BG.setBounds(0,0,455,261);
        BG.setOpaque(false);
        bg = new ImageIcon(getClass().getResource("dialogbg.png"));
        BG.setIcon(bg);
        add(BG, new Integer(2));
        
        setPlayer(name);
        setTurns(numTurns);
        setAction("Roll the dice");
        setButton(1);

        validate();
        }catch(Exception ex){ex.printStackTrace();}
	}
    
    /**
    *   Create a new JButton with a rollover sound that is prepared for 
    *   displaying an image.
    *
    *   @return a reference to the new button
    **/
    private JButton getNewButton()
    {
        final JButton Button = new JButton();
        Button.setBorder(BorderFactory.createEmptyBorder());
        //Button.setPreferredSize(new Dimension(250,75));
        Button.setOpaque(false);
        Button.setBackground(new Color(0,0,0,0));
        Button.setFocusPainted(false);
        Button.setContentAreaFilled(false);
        Button.addMouseListener(new MouseAdapter() 
        {
            public void mouseEntered(MouseEvent e)
            {
                if(Button.isEnabled())
                    Sounds.playSound("mouseOver.wav");
            }
        });
        return Button;
    }
    
    /**
    *   Display the name of the current player on the first line.
    *
    *   @param name is the current player's name
    **/
    public void setPlayer(String name)
    {
        setFirstLine(name + "'s turn");
    }
    
    /**
    *   Change the text of the first line.
    *
    *   @param message is the full text to place on the line
    **/
    private void setFirstLine(String message)
    {
        FirstLine = ImageText.getLetters(message);
        length = FirstLine.length;
        LinePlayer.removeAll();
        for(int idx = 0; idx < length; idx++)
            LinePlayer.add(FirstLine[idx]);
        LinePlayer.add(Box.createRigidArea(new Dimension((355-(length*12)),0)));
        LinePlayer.revalidate();
    }

    /**
    *   Display the current player's number of remaining turns on the 
    *   second line.
    *
    *   @param numTurns is the player's number of remaining turns
    **/
    public void setTurns(int numTurns)
    {
        if(numTurns != 1)
            setSecondLine(numTurns + " turns remaining");
        else
            setSecondLine("Final turn");
    }
    
    /**
    *   Change the text of the second line.
    *
    *   @param message is the full text to place on the line
    **/
    private void setSecondLine(String message)
    {
        SecondLine = ImageText.getLetters(message);
        length = SecondLine.length;
        LineTurns.removeAll();
        for(int idx = 0; idx < length; idx++)
            LineTurns.add(SecondLine[idx]);
        LineTurns.add(Box.createRigidArea(new Dimension((355-(length*12)),0)));
        LineTurns.revalidate();
    }
    
    /**
    *   Display a message indicating the player's possible choices on the
    *   third line.
    *
    *   @param message is the message to display
    **/
    public void setAction(String message)
    {
        Action = ImageText.getLetters(message);
        length = Action.length;
        LineAction.removeAll();
        for(int idx = 0; idx < length; idx++)
            LineAction.add(Action[idx]);
        LineAction.add(Box.createRigidArea(new Dimension((355-(length*12)),0)));
        LineAction.revalidate();
    }
    
    /**
    *   Display a game over message, display whether the player in control of
    *   this applet won or lost, and replace the roll button with a play 
    *   again button.
    *
    *   @param isSinglePlayer is whether the game has only one player
    *
    *   @param score is the player's score
    *
    *   @param wonGame is whether the player in control of this applet won
    **/
    public void gameOver(boolean isSinglePlayer, int score, boolean wonGame)
    {
        setFirstLine("Game Over");
        setSecondLine("Score: " + score);
        if(isSinglePlayer)
            setAction(" ");
        else
        {
            if(wonGame)
                setAction("You Won!");
            else
                setAction("You Lost!");
        }
        LineButton.removeAll();
        //reuse RollButton so that a reference can be returned using getButton()
        RollButton = getNewButton();
        RollButton.setIcon(new ImageIcon(
                getClass().getResource("playAgain.png")));
        RollButton.setRolloverIcon(new ImageIcon(
                getClass().getResource("playAgainActive.png")));
        LineButton.add(RollButton);
        LineButton.add(Box.createRigidArea(new Dimension(20,0)));
        LineButton.revalidate();
    }
    
    
    
    /**
    *   Change the number displayed on the roll button.
    *
    *   @param rollNum is the count of rolls for the current player's turn
    **/
    public void setButton(int rollNum)
    {
        RollButton.setIcon(RollImgs[rollNum-1]);
        RollButton.setRolloverIcon(RolloverImgs[rollNum-1]);
        LineButton.repaint();
    }
    
    /**
    *   @return a reference to the roll button
    **/
    public JButton getButton()
    {
        return RollButton;
    }
}

