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
    private JLabel[] Player;
    private JLabel[] Turns;
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
        
        RollButton = new JButton();
        //RollButton.setPreferredSize(new Dimension(250,75));
        RollButton.setBorder(BorderFactory.createEmptyBorder());
        RollButton.setOpaque(false);
        RollButton.setBackground(new Color(0,0,0,0));
        RollButton.setFocusPainted(false);
        RollButton.setContentAreaFilled(false);
        RollButton.addMouseListener(new MouseAdapter() 
        {
            public void mouseEntered(MouseEvent e)
            {
                if(RollButton.isEnabled())
                    Sounds.playSound("mouseOver.wav");
            }
        });
        
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
    *   Display the name of the current player.
    *
    *   @param name is the current player's name
    **/
    public void setPlayer(String name)
    {
        Player = ImageText.getLetters(name + "'s turn");
        length = Player.length;
        LinePlayer.removeAll();
        for(int idx = 0; idx < length; idx++)
            LinePlayer.add(Player[idx]);
        LinePlayer.add(Box.createRigidArea(new Dimension((355-(length*12)),0)));
        LinePlayer.validate();
    }

    /**
    *   Display the current player's number of remaining turns.
    *
    *   @param numTurns is the player's number of remaining turns
    **/
    public void setTurns(int numTurns)
    {
        if(numTurns != 1)
            Turns = ImageText.getLetters(numTurns + " turns remaining");
        else
            Turns = ImageText.getLetters("Final turn");
        length = Turns.length;
        LineTurns.removeAll();
        for(int idx = 0; idx < length; idx++)
            LineTurns.add(Turns[idx]);
        LineTurns.add(Box.createRigidArea(new Dimension((355-(length*12)),0)));
        LineTurns.validate();
    }
    
    /**
    *   Display a message indicating the player's possible choices.
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
        LineAction.validate();
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

