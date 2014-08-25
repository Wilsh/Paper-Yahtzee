/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        July 29, 2014
* File:        PlayerJP.java
*******************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.*;

/**
*   PlayerJP.java is a JPanel that displays and animates players on the game 
*   board
**/
public class PlayerJP extends JPanel
{
    private Player[] ThePlayers;
    private int numPlayers;
    private int activePlayer;
    private int combinedWidth;
    private int rightmostPosition;
    private int animateNum;
    private int animateOrder;
    private final int HEIGHT = 10;
    private final int PWIDTH = 133;
    private final int PHEIGHT = 46;
    private Player leftmostPlayer;
    private LinkedList<Player> PlayerOrder;
    private ExecutorService threadPool;

	/**
    *   Constructor prepares the PlayerJP object for display on the game board
    **/
	public PlayerJP()
	{
        super();
        ThePlayers = new Player[5];
        numPlayers = 0;
        combinedWidth = 0;
        animateNum = 1;
        animateOrder = 1;
        PlayerOrder = new LinkedList<Player>();
        setOpaque(false);
        setLayout(null);
        setBounds(0,0,800,80);
        setPreferredSize(new Dimension (800,80));
	}
    
    /**
    *   Terminate thread execution to allow the applet to close properly
    **/
    public void killThreads()
    {
        threadPool.shutdownNow();
    }
    
    /**
    *   Add a player to the game board
    *
    *   @param thePlayer is the player to display on the board
    **/
    public void addPlayer(Player thePlayer)
    {
        ThePlayers[numPlayers] = thePlayer;
        numPlayers++;
        PlayerOrder.add(thePlayer);
        this.add(thePlayer);
    }
    
    /**
    *   Determine layout for each player on the game board
    *
    *   Note: each player has a width of 133px and there is a gap of 11px
    *   between the players
    **/
    public void doneAddingPlayers()
    {
        for(int idx = 0; idx < numPlayers; idx++)
        {
            combinedWidth += PWIDTH;
        }
        
        activePlayer = (800-combinedWidth-((numPlayers-1)*11))/2;
        leftmostPlayer = PlayerOrder.peek();

        for(int idx = 0; idx < numPlayers; idx++)
        {
            ThePlayers[idx].setBounds(activePlayer+(idx*144), HEIGHT, 
                    PWIDTH, PHEIGHT);
        }
        
        rightmostPosition = ThePlayers[numPlayers-1].getLocation().x;
        repaint();
    }
    
    /**
    *   Animate players on the player panel by shifting each player to the 
    *   left until the given player is in the first position. When a player
    *   is moved leftward off the game board it will reappear from the right
    *   side of the board in a circular-queue fashion. The players will not
    *   move if the given player is already in the leftmost position.
    *
    *   @param currentPlayer is the player to display on the leftmost side
    **/
    public void shiftPlayers(Player currentPlayer)
    {try{
        if(currentPlayer != leftmostPlayer)
        {
            threadPool = Executors.newFixedThreadPool(5);
            for(int idx = 0; idx < numPlayers; idx++)
            {
                queueAnimation(ThePlayers[idx], 
                        ThePlayers[idx] == leftmostPlayer ? true : false, 
                        animateNum);
            }
            threadPool.shutdown();
            PlayerOrder.add(PlayerOrder.pop());
            leftmostPlayer = PlayerOrder.peek();
            animateNum++;
            
            //call method recursively in case multiple animation sequences
            //are needed to properly position players
            shiftPlayers(currentPlayer);
        }}catch (Exception e) { e.printStackTrace();}
    }
    
    /**
    *   Wait for previously-queued animations to finish before starting a new 
    *   animation group. This method is called for each player.
    *
    *   @param thePlayer is the player to animate
    *
    *   @param moveOffBoard signals whether a player will wrap around the board
    *   during its animation
    *
    *   @param queuePosition is the numbered 'animation group' that determines
    *   when the animation will run
    **/
    private void queueAnimation(final Player thePlayer, 
            final boolean moveOffBoard, final int queuePosition)
    {
        SwingWorker animatePlayer = new SwingWorker(){
            protected String doInBackground()
            {
                while(queuePosition != animateOrder)
                {
                    try
                    {
                        Thread.sleep(50);
                    }
                    catch(Exception ex)
                    {
                      System.out.println(
                        "PlayerJP.java queueAnimation() Ex: sleep failed");
                    }
                }
                return null;
            }
            protected void done()
            {
                if(moveOffBoard)
                    wrapPlayerAround(thePlayer);
                else
                    movePlayerLeft(thePlayer);
            }
        };
        threadPool.submit(animatePlayer);
    }
    
    /**
    *   Move a player left 144px using a SwingWorker for animation
    *
    *   @param thePlayer is the player to move
    **/
    private void movePlayerLeft(final Player thePlayer)
    {
        SwingWorker animatePlayer = new SwingWorker(){
            protected String doInBackground()
            {
                for(int idx = 0; idx < 24; idx++)
                {
                    try
                    {
                        Thread.sleep(18);
                    }
                    catch(Exception ex)
                    {
                      System.out.println(
                        "PlayerJP.java movePlayerLeft() Ex: sleep failed");
                    }
                    process(new String[1], idx);
                }
                return null;
            }
            protected void process(String[] stuff, int idx)
            {
                thePlayer.setBounds(thePlayer.getLocation().x - 6,HEIGHT,
                        PWIDTH,PHEIGHT);
                repaint();
            }
        };
        animatePlayer.execute();
    }
    
    /**
    *   Move a player left off the board then back onto the board from the right
    *   side until the player is in the rightmost position. Uses a SwingWorker
    *   for animation.
    *
    *   @param thePlayer is the player to move
    **/
    private void wrapPlayerAround(final Player thePlayer)
    {try{
        final int rightLoc = rightmostPosition;
        
        SwingWorker animatePlayer = new SwingWorker(){
            protected String doInBackground()
            {
                while(thePlayer.getLocation().x > -PWIDTH)
                {
                    try
                    {
                        Thread.sleep(12);
                    }
                    catch(Exception ex)
                    {
                      System.out.println(
                        "PlayerJP.java wrapPlayerAround() Ex: sleep failed");
                    }
                    process(new String[1]);
                }
                
                thePlayer.setBounds(800, HEIGHT, PWIDTH, PHEIGHT);
                repaint();
                
                while(thePlayer.getLocation().x > (rightLoc + 8))
                {
                    try
                    {
                        Thread.sleep(18);
                    }
                    catch(Exception ex)
                    {
                      System.out.println(
                        "PlayerJP.java wrapPlayerAround() Ex2: sleep failed");
                    }
                    process(new String[1]);
                }
                
                thePlayer.setBounds(rightLoc, HEIGHT, PWIDTH, PHEIGHT);
                repaint();
                return null;
            }
            protected void process(String[] stuff)
            {
                thePlayer.setBounds(thePlayer.getLocation().x - 8, HEIGHT, 
                        PWIDTH, PHEIGHT);
                repaint();
            }
            protected void done()
            {
                animateOrder++;
            }
        };
        animatePlayer.execute();}catch (Exception e) { e.printStackTrace();}
    }
}
