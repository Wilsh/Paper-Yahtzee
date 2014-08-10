/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        Yahtzee.java
* Note:        The networking code in this file has been adapted from 
*               SocketClient.java, a file provided by Dr. Spiegel
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.applet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.*;

/**
*   Yahtzee.java is the main class for the game. It contains the set of dice and
*   the players, and drives the Turn class as the game is played. 
**/
public class Yahtzee extends JApplet implements ActionListener
{
    int numPlayers = 1;
    Dice PlayerDice;
    Player[] ThePlayers;
    Player CurrentPlayer, myPlayer;
    int[] Cheats;
    LinkedList<Player> PlayerQ;
    LinkedList<Player> FinishedQ;
    Turn PlayerTurn;
    Font DialogFont = new Font("Dialog", Font.BOLD, 14);
    Font GameOverFont = new Font("Dialog", Font.BOLD, 26);
    JLabel LblName;
    JLabel LblTurns;
    JLabel LblPrompt;
    JButton BtnRoll;
    //main container for applet
    JLayeredPane Window;
    //start menu components
    JPanel StartMenu, StartMenuSub1, StartMenuSub2, StartMenuSub3;
    ButtonGroup NumPlayers;
    JPanel LblSingle;
    JPanel LblComputer;
    int length;
    JLabel[] tempLabelArray;
    JRadioButton OnePlayer;
    JRadioButton TwoPlayers;
    JButton Play;
    
    //cheat panels
    JPanel CheatPanel;
    JPanel CheatPanelSub1;
    JPanel CheatPanelSub2;
    JPanel CheatPanelSub3;
    JPanel CheatPanelSub4;
    JPanel[] CheatCategory;
    JTextField[] CheatScores;
    
    //panels for main game
    public PlayerJP PlayerPanel;
    public JPanel ScorePanel;
    public JPanel DicePanel;
    public PreviousJP PreviousPanel;
    public DialogJP DialogPanel;
    
    private Image background;
    private ImageIcon bg;
    private JLabel BG;
    private CharacterImage ImageGet;
    
    //network components
    public static final int PORT = 0;
    final String SERVERNAME = "redacted";
    boolean useServer = false;
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String playerName;
    String isInternet;
    String[] playerNames;
    int orderIdx;
    volatile boolean stillWaitingForServer;
    ImageIcon[] icons;
    private JLabel Dots;
    boolean isGameOver = false;
    
    //sounds
    SoundLib Sounds;
    SoundLib PlayBtnSounds;
    SoundTask TurnAlert;
    SoundTask LastTurn;

    /**
    *   Init gets parameters from the HTML launch page and calls 
    *   setUpStartMenu() to prepare the game setup screen.
    **/
    public void init()
    {try{
        //applet parameters
        playerName = getParameter("Name");
        isInternet = getParameter("IsNet");
        //System.out.println(playerName + "  " + isInternet);
        if(isInternet.equals("true"))
        {
            useServer = true;
            icons = new ImageIcon[4];
            icons[0] = new ImageIcon(getClass().getResource("waiting0.png"));
            icons[1] = new ImageIcon(getClass().getResource("waiting1.png"));
            icons[2] = new ImageIcon(getClass().getResource("waiting2.png"));
            icons[3] = new ImageIcon(getClass().getResource("waiting3.png"));
            
            Dots = new JLabel();
            Dots.setIcon(icons[0]);
        }
        else
        {
            useServer = false;
        }
        
        //transform text into images
        ImageGet = new CharacterImage();
        loadSounds();
        setUpStartMenu();
        
        }catch (Exception e) { e.printStackTrace();}
    }
    
    /**
    *   Load sounds that will be played during the course of the game.
    **/
    public void loadSounds()
    {
        String[] soundFiles = new String[13];
        
        soundFiles[0] = new String("buttonClicked.wav");
        soundFiles[1] = new String("mouseOver.wav");
        soundFiles[2] = new String("yourTurn.wav");
        soundFiles[3] = new String("gameStarting.wav");
        soundFiles[4] = new String("ready1.wav");
        soundFiles[5] = new String("ready2.wav");
        soundFiles[6] = new String("ready3.wav");
        soundFiles[7] = new String("metalHit.wav");
        soundFiles[8] = new String("anotherRound.wav");
        soundFiles[9] = new String("putOnTab.wav");
        soundFiles[10] = new String("smallClick.wav");
        soundFiles[11] = new String("won.wav");
        soundFiles[12] = new String("lost.wav");
        //soundFiles[] = new String();
        
        Sounds = new SoundLib(soundFiles);
        
        if(useServer)
        {
            soundFiles = new String[3];
            
            soundFiles[0] = new String("ready1.wav");
            soundFiles[1] = new String("ready2.wav");
            soundFiles[2] = new String("ready3.wav");
            
            PlayBtnSounds = new SoundLib(soundFiles);
        }
    }
    
    /**
    *   Initialize game components and display the background image
    **/
    public void setUpStartMenu()
    {
        boolean errorOccurred = false;
        PlayerQ = new LinkedList<Player>();
        FinishedQ = new LinkedList<Player>();
        LblName = new JLabel();
        LblTurns = new JLabel();
        LblPrompt = new JLabel("Roll the dice");
        //panels for main game
        PlayerPanel = new PlayerJP();
        ScorePanel = new JPanel();
        DicePanel = new JPanel();
        PlayerDice = new Dice(-1, !useServer);
        PreviousPanel = new PreviousJP();
        //DialogPanel = new JPanel();
        //PlayerPanel.setOpaque(false);
        DicePanel.setOpaque(false);
        //DialogPanel.setOpaque(false);
        ScorePanel.setOpaque(false);
        

        //this.setLayout(new BorderLayout());
        this.setSize(800,600);
        //Window is the main container for all game content
        Window = new JLayeredPane();
        Window.setLayout(new BorderLayout());
        
        if(useServer)
        {
            errorOccurred = startConnection();
            if(!errorOccurred)
            {
                setupInternet();
                bg = new ImageIcon(getClass().getResource("waiting.png"));
            }
        }
        else
        {
            setupLocal();
            orderIdx = 0;
            playerNames = new String[2];
            playerNames[0] = playerName;
            playerNames[1] = "Robot";
            bg = new ImageIcon(getClass().getResource("paperfullbg.png"));
        }
        
        if(!errorOccurred)
        {
            //background image
            BG = new JLabel();
            BG.setIcon(bg);
            Window.setLayer(BG, 9);
            Window.add(BG);
            
            add(Window);
            Window.validate();
            Window.repaint();
            revalidate();
            repaint();

            if(useServer)
            {
                //run waitForServer() in a separate thread so EDT can draw GUI
                SwingWorker worker = new SwingWorker(){
                    protected String doInBackground()
                    {
                        waitForServer();
                        return null;
                    }
                };
                worker.execute();
            }
        }
    }
    
    /**
    *   Connect to the server and send this player's name
    *
    *   @return whether an exception was thrown while attempting to connect
    **/
    public boolean startConnection()
    {
        boolean errorOccurred = false;
        
        try
        {
            socket = new Socket(SERVERNAME, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } 
        catch(UnknownHostException e) 
        {
            System.out.println("Unknown host: " + SERVERNAME);
            errorOccurred = true;
        } 
        catch(IOException e) 
        {
            System.out.println("No I/O");
            errorOccurred = true;
        }
        catch(Exception e)
        {
            System.out.println("General Exception: " + e);
            errorOccurred = true;
        }
        
        if(errorOccurred)
        {
            bg = new ImageIcon(getClass().getResource("serverUnavailable.png"));
            BG = new JLabel();
            BG.setIcon(bg);
            add(BG);
            revalidate();
            repaint();
        }
        else
        {
            //Send player's name to server
            out.println(playerName);
        }
        
        return errorOccurred;
    }
    
    /**
    *   Cycle images on the waiting screen
    **/
    public void showDots(int idx)
    {
        Window.remove(Dots);
        Dots.setIcon(icons[idx]);
        Window.add(Dots);
        Window.repaint();
    }
    
    /**
    *   Animate the waiting screen, receive order index (this applet's turn 
    *   order), number of players, and their names from server, then
    *   start the game
    **/ 
    public void waitForServer()
    {
        stillWaitingForServer = true;
        Window.setLayer(Dots, 11);
            
        //animate waiting message using new thread
        SwingWorker BGworker = new SwingWorker(){
            protected String doInBackground()
            {
                while(true)
                {
                    for(int idx = 0; idx < 4; idx++)
                    {
                        showDots(idx);
                        try
                        {
                            if(stillWaitingForServer)
                                Thread.sleep(250);
                            else
                                return null;
                        }
                        catch(Exception ex)
                        {
                            System.out.println(
                                "waitForServer Exception: sleep failed");
                            //System.exit(-1);
                        }
                    }
                }
            }
        };
        BGworker.execute();
        
        try
        {
            orderIdx = Integer.parseInt(in.readLine());
            numPlayers = Integer.parseInt(in.readLine());
            playerNames = new String[numPlayers];
            //System.out.println(numPlayers);
            for(int idx = 0; idx < numPlayers; idx++)
            {
                playerNames[idx] = in.readLine();
                //System.out.println(playerNames[idx]);
            }
        } 
        catch(IOException e){
            System.out.println("************* Name read failed *************");
            System.exit(1);
        }
        
        //gameStarting.play();
        stillWaitingForServer = false;
        
        try
        {//wait for worker thread to finish
            Thread.sleep(400);
        }
        catch(Exception ex)
        {
            System.out.println(
                "waitForServer Exception2: sleep failed");
            System.exit(-1);
        }
        Window.remove(Dots);
        startGame(numPlayers, -1);
        getAnotherMessage();
    }
    
    /*
    *   Transform a String into a series of images on a JPanel.
    *   Currently unused in this class.
    *
    *   @param word is the string to transform
    *
    *   @return JLabel containing image representation of the String
    **   
    public JPanel transformChars(String word)
    {
        JPanel theImages = new JPanel();
        theImages.setOpaque(false);
        JLabel[] tempArray = ImageGet.getLetters(word);
        int length = tempArray.length;
        theImages.setMinimumSize(new Dimension((12*length),23));
        for(int idx = 0; idx < length; idx++)
        {
            theImages.add(tempArray[idx]);
        }
        return theImages;
    } */
    
    /**
    *   Initialize the dice and players then call playGame() to set up
    *   the game board.
    *
    *   @param numPlayers is the number of players in the game
    *
    *   @param seed determines the outcome of the PRNG used for the dice
    **/
    public void startGame(int numPlayers, int seed)
    {
        boolean drawABox;
        //create the set of dice using specified seed
        //PlayerDice = new Dice(seed, this);
        ThePlayers = new Player[numPlayers];
        
        //change background for network game
        if(useServer)
        {
            bg = new ImageIcon(getClass().getResource("paperfullbg.png"));
            BG.setIcon(bg);
            Window.repaint();
        }
        
        for(int playerNum = 1; playerNum <= numPlayers; playerNum++)
        {
            if(orderIdx == playerNum-1)
                drawABox = true;
            else
                drawABox = false;
                
            switch(playerNum)
            {
                case 1:
                {
                    ThePlayers[0] = new Human(playerNames[0], this, drawABox);
                    //only add action listeners to a scoresheet if this player
                    //is playing with this applet
                    if(orderIdx == 0)
                    {
                        createListeners(ThePlayers[0].getScoresheet());
                        myPlayer = ThePlayers[0];
                    }
                    if(!useServer)
                        addCheatsToScoresheet(ThePlayers[0], CheatPanel);
                    PlayerQ.add(ThePlayers[0]);
                    break;
                }
                case 2:
                {
                    if(useServer)
                    {
                        ThePlayers[1] = new Human(playerNames[1], this, 
                                drawABox);
                        if(orderIdx == 1)
                        {
                            createListeners(ThePlayers[1].getScoresheet());
                            myPlayer = ThePlayers[1];
                        }
                    }
                    else
                    {
                        ThePlayers[1] = new Robot(playerNames[1], this, false);
                    }
                    PlayerQ.add(ThePlayers[1]);
                    break;
                }
                case 3:
                {
                    ThePlayers[2] = new Human(playerNames[2], this, drawABox);
                    if(orderIdx == 2)
                    {
                        createListeners(ThePlayers[2].getScoresheet());
                        myPlayer = ThePlayers[2];
                    }
                    PlayerQ.add(ThePlayers[2]);
                    break;
                }
                case 4:
                {
                    ThePlayers[3] = new Human(playerNames[3], this, drawABox);
                    if(orderIdx == 3)
                    {
                        createListeners(ThePlayers[3].getScoresheet());
                        myPlayer = ThePlayers[3];
                    }
                    PlayerQ.add(ThePlayers[3]);
                    break;
                }
                case 5:
                {
                    ThePlayers[4] = new Human(playerNames[4], this, drawABox);
                    if(orderIdx == 4)
                    {
                        createListeners(ThePlayers[4].getScoresheet());
                        myPlayer = ThePlayers[4];
                    }
                    PlayerQ.add(ThePlayers[4]);
                    break;
                }
            }
            
            PlayerPanel.addPlayer(ThePlayers[playerNum-1]);
        }

        PlayerPanel.doneAddingPlayers();
        playGame();
    }
    
    /**
    *   Add action listeners to buttons on a Scoresheet JPanel.
    **/
    public void createListeners(JPanel PPanel)
    {
        Component[] Fields = PPanel.getComponents();
        
        for(int idx = 3; idx < 43; idx+=3)
        {
            ((JButton)Fields[idx]).addActionListener(this);
        }
    }
    
    /**
    *   Prepare the game board and initiate the first player's turn.
    *
    *   Precondition: startGame has already been called
    **/
    public void playGame()
    {
        Window.remove(StartMenu);
        
        JLayeredPane GameInfo = new JLayeredPane();
        GameInfo.setLayout(new BoxLayout(GameInfo, BoxLayout.PAGE_AXIS));
        GameInfo.setOpaque(false);
        
        Window.setLayer(GameInfo, 10);
        Window.setLayer(ScorePanel, 10);
        Window.setLayer(PlayerPanel, 10);
        Window.setLayer(DicePanel, 10);
        
        //score panel
        ScorePanel.setLayout(new GridLayout(1,1));
        Window.add(ScorePanel, BorderLayout.EAST);
        //player panel
        //PlayerPanel.setPreferredSize(new Dimension (800,80));
        Window.add(PlayerPanel, BorderLayout.NORTH);
        //dice panel
        DicePanel.setLayout(new BoxLayout(DicePanel, BoxLayout.LINE_AXIS));
        DicePanel.add(Box.createRigidArea(new Dimension(0,115)));
        DicePanel.add(PlayerDice);
        Window.add(DicePanel, BorderLayout.SOUTH);
        
        Window.add(GameInfo, BorderLayout.CENTER);
        
        CurrentPlayer = PlayerQ.pop();
        ScorePanel.add(CurrentPlayer.getScoresheet());
        DialogPanel = new DialogJP(CurrentPlayer.getName(), CurrentPlayer.getNumTurns());
        GameInfo.add(PreviousPanel);
        GameInfo.add(DialogPanel);
        Window.revalidate();
        Window.repaint();
        
        BtnRoll = DialogPanel.getButton();
        BtnRoll.addActionListener(this);
        
        if(CurrentPlayer == myPlayer)
        {
            BtnRoll.setEnabled(true);
            Sounds.playSound("yourTurn.wav");
            if(CurrentPlayer.getNumTurns() == 1) //possible if cheat is used
                LastTurn = new SoundTask(1000 * 1, "lastTurn.wav");
            if(useServer)
                TurnAlert = new SoundTask(1000 * 60, "hurryUp.wav");
        }
        else
            BtnRoll.setEnabled(false);
            
        PlayerTurn = new Turn(PlayerDice, CurrentPlayer, DialogPanel);
    }

    /**
    *   Call methods for the end of a player's turn, enable or disable certain
    *   features depending on the player to go next, and start the next
    *   player's turn
    **/
    public void nextPlayer()
    {
        CurrentPlayer.updateScore();
        //remove the previously scored category's potential score
        CurrentPlayer.showPotential(PlayerDice.getDieValues());
        CurrentPlayer.hidePotential();
        
        CurrentPlayer.disableCategories();
        PlayerDice.animateDice(true);
        revalidate();
        repaint();
        
        if(CurrentPlayer.getNumTurns() != 0)
            PlayerQ.add(CurrentPlayer);
        else
            FinishedQ.add(CurrentPlayer);
        
        PlayerDice.resetHolds();
        
        PreviousPanel.setName(CurrentPlayer.getName());
        PreviousPanel.setCategory(CurrentPlayer.getLastCategory());
        PreviousPanel.setScore("" + CurrentPlayer.getLastScore());
        
        if(PlayerQ.peek() != null)
        {
            ScorePanel.remove(CurrentPlayer.getScoresheet());
            CurrentPlayer = PlayerQ.pop();
            ScorePanel.add(CurrentPlayer.getScoresheet());
            PlayerPanel.shiftPlayers(CurrentPlayer);
            
            if(CurrentPlayer == myPlayer)
                BtnRoll.setEnabled(true);
            else
                BtnRoll.setEnabled(false);
            
            if(CurrentPlayer instanceof Human)
            {
                if(CurrentPlayer == myPlayer)
                {
                    Sounds.playSound("yourTurn.wav");
                    if(CurrentPlayer.getNumTurns() == 1)
                        LastTurn = new SoundTask(1700, "lastTurn.wav");
                    if(useServer)
                        TurnAlert = new SoundTask(1000 * 60, "hurryUp.wav");
                }
                DialogPanel.setAction("Roll the dice");
            }
            else
            {
                DialogPanel.setAction("Robot is rolling the dice");
            }

            DialogPanel.setPlayer(CurrentPlayer.getName());
            DialogPanel.setTurns(CurrentPlayer.getNumTurns());
            DialogPanel.setButton(1);
            
            PlayerTurn = new Turn(PlayerDice, CurrentPlayer, DialogPanel);

            //automatically take the turn for a computer player
            if(CurrentPlayer instanceof Robot)
            {
                SwingWorker waitRobot = new SwingWorker(){
                    protected String doInBackground()
                    {
                        for(int roll = 1; roll < 4; roll++)
                        {
                            try
                            {//allow human players to observe robot's play
                                Thread.sleep(2000);
                            }
                            catch(Exception ex)
                            {
                                System.out.println(
                                    "nextPlayer() Exception: sleep failed");
                            }
                            process(new String[1]);
                        }
                        return null;
                    }
                    protected void process(String[] stuff)
                    {
                        doRollBtn();
                        repaint();
                    }
                };
                waitRobot.execute();
            }
        }
        else
        {
            //display scoresheet for the player in control of the applet
            ScorePanel.remove(CurrentPlayer.getScoresheet());
            ScorePanel.add(myPlayer.getScoresheet());
            myPlayer.hidePotential();
            
            if(useServer)
            {
                isGameOver = true;
                //tell server the game has finished
                out.println("GAMEOVER");
            }
            gameOver();
        }
    }
    
    /**
    *   Display the final score for player and announce the winner
    **/
    public void gameOver()
    {
        int waitTime;
        
        if(numPlayers == 1)
        {
            waitTime = 1700;
            DialogPanel.gameOver(true, myPlayer.getFinalScore(), true);
        }
        else
        {
            waitTime = 4000;
            int score;
            int highest = 0;
            for(int idx = 0; idx < numPlayers; idx++)
            {
                CurrentPlayer = FinishedQ.pop();
                score = CurrentPlayer.getFinalScore();
                
                if(highest < score)
                {
                    highest = score;
                }
            }
            
            if(myPlayer.getFinalScore() == highest)
            {
                Sounds.playSound("won.wav");
                DialogPanel.gameOver(false, myPlayer.getFinalScore(), true);
            }
            else
            {
                Sounds.playSound("lost.wav");
                DialogPanel.gameOver(false, myPlayer.getFinalScore(), false);
            }
        }
        
        final JButton playAgain = DialogPanel.getButton();
        playAgain.setEnabled(false);
        final int timer = waitTime;
        SwingWorker waitTimer = new SwingWorker(){
            protected String doInBackground()
            {
                try
                {
                    Thread.sleep(timer);
                }
                catch(Exception ex)
                {
                  System.out.println(
                    "Yahtzee.java gameOver() Exception: sleep failed");
                }

                return null;
            }
            protected void done()
            {
                playAgain.setEnabled(true);
                Sounds.playSound("anotherRound.wav");
            }
        };
        waitTimer.execute();
        
        playAgain.addMouseListener(new MouseAdapter() 
        {
            public void mousePressed(MouseEvent e)
            {
                if(SwingUtilities.isLeftMouseButton(e))
                {
                    Sounds.playSound("putOnTab.wav");
                    resetGame();
                }
            }
        });
    }
    
    /**
    *   Reset the applet for a new game
    **/
    public void resetGame()
    {
        if(useServer)
        {
            try
            {
                socket.close();
                isGameOver = false;
            }
            catch(IOException e)
            {
                System.out.println("Could not close socket");
                System.exit(-1);
            }
        }

        remove(Window);
        revalidate();
        repaint();
        numPlayers = 1;
        setUpStartMenu();
    }
    
    /**
    *   Place labels and text fields on the given JPanel
    **/
    public void addCheats()
    {
        //instantiate a player to get enumerated score categories
        Player Dummy = new Robot("Dummy", this, false);
        
        CheatCategory = new JPanel[13];
        CheatScores = new JTextField[13];
        
        //create category names
        for(int idx = 0; idx < 13; idx++)
        {
            CheatCategory[idx] = new JPanel();
            CheatCategory[idx].setOpaque(false);
            CheatCategory[idx].setLayout(new BoxLayout(
                        CheatCategory[idx], BoxLayout.LINE_AXIS));
            
            tempLabelArray = ImageGet.getLetters(Dummy.getCategoryName(idx));
            length = tempLabelArray.length;
            CheatCategory[idx].setPreferredSize(new Dimension((15*length),23));
            for(int index = 0; index < length; index++)
                CheatCategory[idx].add(tempLabelArray[index]);
        }
        //create textfields
        for(int idx = 0; idx < 13; idx++)
        {
            CheatScores[idx] = new JTextField("0");
            CheatScores[idx].setOpaque(false);
            CheatScores[idx].setPreferredSize(new Dimension(30,23));
        }
        
        //add category names and textfields to cheat panels
        for(int idx = 0; idx < 4; idx++)
        {
            CheatPanelSub1.add(CheatCategory[idx]);
            CheatPanelSub1.add(CheatScores[idx]);
           CheatPanelSub1.add(Box.createRigidArea(new Dimension(30,0)));//spacer
        }
        for(int idx = 4; idx < 7; idx++)
        {
            CheatPanelSub2.add(CheatCategory[idx]);
            CheatPanelSub2.add(CheatScores[idx]);
           CheatPanelSub2.add(Box.createRigidArea(new Dimension(30,0)));//spacer
        }
        for(int idx = 7; idx < 10; idx++)
        {
            CheatPanelSub3.add(CheatCategory[idx]);
            CheatPanelSub3.add(CheatScores[idx]);
           CheatPanelSub3.add(Box.createRigidArea(new Dimension(30,0)));//spacer
        }
        for(int idx = 10; idx < 13; idx++)
        {
            CheatPanelSub4.add(CheatCategory[idx]);
            CheatPanelSub4.add(CheatScores[idx]);
           CheatPanelSub4.add(Box.createRigidArea(new Dimension(30,0)));//spacer
        }
    }
    
    /**
    *   Add values given at the setup menu to a player's scoresheet
    **/
    public void addCheatsToScoresheet(Player P1, JPanel CheatPanel)
    {
        int score = 0;
        
        for(int idx = 0; idx < 13; idx++)
        {
            try
            {
                score = Integer.parseInt(CheatScores[idx].getText());
            }
            catch(Exception ex)
            {
                score = 0;
            }

            if(score != 0)
                P1.cheatCategory(idx, score);
        }
        
        //need to enable categories to set disabled properties
        P1.enableCategories();
        P1.disableCategories();
    }
    
    /**
    *   Set up the applet for a network game
    **/
    public void setupInternet()
    {
        //start menu components
        StartMenu = new JPanel();
        StartMenuSub1 = new JPanel();
        StartMenuSub2 = new JPanel();
        StartMenuSub3 = new JPanel();
        Play = new JButton();

        StartMenu.setLayout(new GridLayout(3,1));
        StartMenu.setSize(800,600);
        StartMenu.setOpaque(false);
        StartMenuSub1.setLayout(new BoxLayout(
                StartMenuSub1, BoxLayout.LINE_AXIS));
        StartMenuSub1.add(Box.createRigidArea(new Dimension(200,115)));
        StartMenuSub1.setOpaque(false);
        StartMenuSub2.setLayout(new BoxLayout(
                StartMenuSub2, BoxLayout.LINE_AXIS));
        StartMenuSub2.add(Box.createRigidArea(new Dimension(0,115)));
        StartMenuSub2.setOpaque(false);
        StartMenuSub3.setLayout(new BoxLayout(
                StartMenuSub3, BoxLayout.LINE_AXIS));
        //StartMenuSub3.add(Box.createRigidArea(new Dimension(300,115)));
        StartMenuSub3.setOpaque(false);
        
        //add content to start menu
        //add JPanels with no layout set
        JPanel CheatPanel1 = new JPanel();
        CheatPanel1.setOpaque(false);
        JPanel CheatPanel2 = new JPanel();
        CheatPanel2.setOpaque(false);
        JPanel CheatPanel3 = new JPanel();
        CheatPanel3.setOpaque(false);
        JPanel CheatPanel4 = new JPanel();
        CheatPanel4.setOpaque(false);
        
        //play button
        final JButton currentButton = new JButton();
        currentButton.addMouseListener(new MouseAdapter() 
        {
            public void mouseEntered(MouseEvent e)
            {
                if(currentButton.isEnabled())
                {
                    currentButton.setBorder(
                            BorderFactory.createLoweredBevelBorder());
                    Sounds.playSound("mouseOver.wav");
                    currentButton.setIcon(new ImageIcon(
                            getClass().getResource("PlayNowHighlight.png")));
                }
            }
            public void mouseExited(MouseEvent e)
            {
                currentButton.setBorder(BorderFactory.createEmptyBorder());
                
                if(currentButton.isEnabled())
                {
                    currentButton.setIcon(new ImageIcon(
                            getClass().getResource("PlayNow.png")));
                }
            }
        });
        
        currentButton.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e)
                {
                    Sounds.playSound("buttonClicked.wav");
                    
                    //send ready signal to server
                    out.println("READY");
                    currentButton.setEnabled(false);
                    
                    try
                    {//wait for first sound to play
                        Thread.sleep(200);
                    }
                    catch(Exception ex)
                    {
                        System.out.println(
                            "setUpInternet() Exception: sleep failed");
                    }
        
                    PlayBtnSounds.playRandom();
                }
            }
        );
        Play = currentButton;
        Play.setPreferredSize(new Dimension(250,75));
        Play.setBorder(BorderFactory.createEmptyBorder());
        Play.setOpaque(false);
        Play.setBackground(new Color(0,0,0,0));
        Play.setFocusPainted(false);
        //Play.setBorderPainted(false);
        Play.setContentAreaFilled(false);
        Play.setIcon(new ImageIcon(getClass().getResource("PlayNow.png")));
        //add JPanel with no layout set
        JPanel BtnPanel = new JPanel();
        BtnPanel.setOpaque(false);
        BtnPanel.add(Play);
        StartMenuSub3.add(BtnPanel);

        StartMenu.add(StartMenuSub1);
        StartMenu.add(StartMenuSub2);
        StartMenu.add(StartMenuSub3);
        
        Window.setLayer(StartMenu, 10);
        Window.add(StartMenu, BorderLayout.CENTER);
        
        //add listeners to dice to transmit holds across the network
        for(int idx = 0; idx < 5; idx++)
        {
            final int index = idx;
            PlayerDice.getDie(idx).addMouseListener(new MouseAdapter() 
                {
                    public void mousePressed(MouseEvent e)
                    {
                        if(SwingUtilities.isLeftMouseButton(e))
                        {
                            out.println("16");
                            out.println(""+index);
                        }
                    }
                }
            );
        }
    }
    
    /**
    *   Set up the applet for a local game
    **/
    public void setupLocal()
    {
        //start menu components
        Cheats = new int[13];
        StartMenu = new JPanel();
        StartMenuSub1 = new JPanel();
        StartMenuSub2 = new JPanel();
        StartMenuSub3 = new JPanel();
        NumPlayers = new ButtonGroup();
        OnePlayer = new JRadioButton();
        TwoPlayers = new JRadioButton();
        Play = new JButton("Play!");
        //cheat panels
        CheatPanel = new JPanel();
        CheatPanelSub1 = new JPanel();
        CheatPanelSub2 = new JPanel();
        CheatPanelSub3 = new JPanel();
        CheatPanelSub4 = new JPanel();
        
        
        StartMenu.setLayout(new GridLayout(3,1));
        StartMenu.setSize(800,600);
        StartMenu.setOpaque(false);
        StartMenuSub1.setLayout(new BoxLayout(
                StartMenuSub1, BoxLayout.LINE_AXIS));
        StartMenuSub1.add(Box.createRigidArea(new Dimension(200,115)));
        StartMenuSub1.setOpaque(false);
        StartMenuSub2.setLayout(new BoxLayout(
                StartMenuSub2, BoxLayout.LINE_AXIS));
        StartMenuSub2.add(Box.createRigidArea(new Dimension(0,115)));
        StartMenuSub2.setOpaque(false);
        StartMenuSub3.setLayout(new BoxLayout(
                StartMenuSub3, BoxLayout.LINE_AXIS));
        //StartMenuSub3.add(Box.createRigidArea(new Dimension(300,115)));
        StartMenuSub3.setOpaque(false);
        
        //add listeners to start menu radio buttons
        OnePlayer.setOpaque(false);
        OnePlayer.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e)
                {
                    numPlayers = 1;
                    CheatPanel.setVisible(true);
                    Sounds.playSound("smallClick.wav");
                }
            }
        );
        NumPlayers.add(OnePlayer);
        OnePlayer.setSelected(true);
        
        TwoPlayers.setOpaque(false);
        TwoPlayers.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e)
                {
                    numPlayers = 2;
                    CheatPanel.setVisible(false);
                    Sounds.playSound("smallClick.wav");
                }
            }
        );
        NumPlayers.add(TwoPlayers);
        
        //add content to start menu
        //generate labels for radio buttons
        //  Single Player
        StartMenuSub1.add(OnePlayer);
        tempLabelArray = ImageGet.getLetters("Single Player");
        length = tempLabelArray.length; 
        LblSingle = new JPanel();
        LblSingle.setOpaque(false);
        LblSingle.setLayout(new BoxLayout(
                    LblSingle, BoxLayout.LINE_AXIS));
        LblSingle.setPreferredSize(new Dimension((13*length),23));
        for(int idx = 0; idx < length; idx++)
            LblSingle.add(tempLabelArray[idx]);
        StartMenuSub1.add(LblSingle);
        StartMenuSub1.add(Box.createRigidArea(new Dimension(30,0)));//spacer
        //  Versus Computer
        StartMenuSub1.add(TwoPlayers);
        tempLabelArray = ImageGet.getLetters("Versus Computer");
        length = tempLabelArray.length;
        LblComputer = new JPanel();
        LblComputer.setOpaque(false);
        LblComputer.setLayout(new BoxLayout(
                    LblComputer, BoxLayout.LINE_AXIS));
        LblComputer.setPreferredSize(new Dimension((15*length),23));
        for(int idx = 0; idx < length; idx++)
            LblComputer.add(tempLabelArray[idx]);
        StartMenuSub1.add(LblComputer);
        
        //panels for preloading scores
        CheatPanel.setLayout(new GridLayout(0,1));
        CheatPanel.setOpaque(false);
        CheatPanelSub1.setLayout(new BoxLayout(
                CheatPanelSub1, BoxLayout.LINE_AXIS));
        CheatPanelSub1.setOpaque(false);
        CheatPanelSub2.setLayout(new BoxLayout(
                CheatPanelSub2, BoxLayout.LINE_AXIS));
        CheatPanelSub2.setOpaque(false);
        CheatPanelSub3.setLayout(new BoxLayout(
                CheatPanelSub3, BoxLayout.LINE_AXIS));
        CheatPanelSub3.setOpaque(false);
        CheatPanelSub4.setLayout(new BoxLayout(
                CheatPanelSub4, BoxLayout.LINE_AXIS));
        CheatPanelSub4.setOpaque(false);
        addCheats();

        JPanel CheatPanel1 = new JPanel();
        CheatPanel1.setOpaque(false);
        JPanel CheatPanel2 = new JPanel();
        CheatPanel2.setOpaque(false);
        JPanel CheatPanel3 = new JPanel();
        CheatPanel3.setOpaque(false);
        JPanel CheatPanel4 = new JPanel();
        CheatPanel4.setOpaque(false);
        
        CheatPanel1.add(CheatPanelSub1);
        CheatPanel.add(CheatPanel1);
        CheatPanel2.add(CheatPanelSub2);
        CheatPanel.add(CheatPanel2);
        CheatPanel3.add(CheatPanelSub3);
        CheatPanel.add(CheatPanel3);
        CheatPanel4.add(CheatPanelSub4);
        CheatPanel.add(CheatPanel4);
        CheatPanel.setOpaque(false);
        StartMenuSub2.add(CheatPanel);
        
        //play button
        final JButton currentButton = new JButton();
        currentButton.addMouseListener(new MouseAdapter() 
        {
            public void mouseEntered(MouseEvent e)
            {
                Sounds.playSound("mouseOver.wav");
                currentButton.setIcon(new ImageIcon(
                        getClass().getResource("PlayHighlight.png")));
            }
            public void mouseExited(MouseEvent e)
            {
                currentButton.setIcon(new ImageIcon(
                            getClass().getResource("PlayButton.png")));
            }
        });
        currentButton.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e)
                {
                    startGame(numPlayers, -1);
                }
            }
        );
        Play = currentButton;
        Play.setPreferredSize(new Dimension(250,75));
        Play.setBorder(BorderFactory.createEmptyBorder());
        Play.setOpaque(false);
        Play.setBackground(new Color(0,0,0,0));
        Play.setFocusPainted(false);
        Play.setBorderPainted(false);
        Play.setContentAreaFilled(false);
        Play.setIcon(new ImageIcon(getClass().getResource("PlayButton.png")));

        JPanel BtnPanel = new JPanel();
        BtnPanel.setOpaque(false);
        BtnPanel.add(Play);
        StartMenuSub3.add(BtnPanel);

        StartMenu.add(StartMenuSub1);
        StartMenu.add(StartMenuSub2);
        StartMenu.add(StartMenuSub3);
        
        Window.setLayer(StartMenu, 10);
        Window.setLayer(CheatPanel, 10);
        Window.add(StartMenu, BorderLayout.CENTER);
    }
    
    /**
    *    actionPerformed calls methods when an action listener fires
    **/
    public void actionPerformed(ActionEvent e)
    {
        Component[] Fields = ScorePanel.getComponents();
        Fields = ((JPanel)Fields[0]).getComponents();
        
        Object Obj = e.getSource();
        
        if(Obj instanceof JButton)
        {
            JButton CompareThis = ((JButton)Obj);
            
            if(CompareThis.equals(BtnRoll))
            {   //roll button
                doRollBtn();
                if(useServer)
                {
                    out.println("14");
                    out.println("15"); //signal that die values are incoming
                    out.println(PlayerDice.getDieValues());
                }
            }
            else if(CompareThis.equals((JButton)Fields[3]))
            {   //score button ones
                Sounds.playSound("metalHit.wav");
                scoreButton(0);
                if(useServer)
                    out.println(""+0);
            }
            else if(CompareThis.equals((JButton)Fields[6]))
            {   //score button twos
                Sounds.playSound("metalHit.wav");
                scoreButton(1);
                if(useServer)
                    out.println(""+1);
            }
            else if(CompareThis.equals((JButton)Fields[9]))
            {   //score button threes
                Sounds.playSound("metalHit.wav");
                scoreButton(2);
                if(useServer)
                    out.println(""+2);
            }
            else if(CompareThis.equals((JButton)Fields[12]))
            {   //score button fours
                Sounds.playSound("metalHit.wav");
                scoreButton(3);
                if(useServer)
                    out.println(""+3);
            }
            else if(CompareThis.equals((JButton)Fields[15]))
            {   //score button fives
                Sounds.playSound("metalHit.wav");
                scoreButton(4);
                if(useServer)
                    out.println(""+4);
            }
            else if(CompareThis.equals((JButton)Fields[18]))
            {   //score button sixes
                Sounds.playSound("metalHit.wav");
                scoreButton(5);
                if(useServer)
                    out.println(""+5);
            }
            else if(CompareThis.equals((JButton)Fields[21]))
            {   //score button 3 of a kind
                Sounds.playSound("metalHit.wav");
                scoreButton(6);
                if(useServer)
                    out.println(""+6);
            }
            else if(CompareThis.equals((JButton)Fields[24]))
            {   //score button 4 of a kind
                Sounds.playSound("metalHit.wav");
                scoreButton(7);
                if(useServer)
                    out.println(""+7);
            }
            else if(CompareThis.equals((JButton)Fields[27]))
            {   //score button full house
                Sounds.playSound("metalHit.wav");
                scoreButton(8);
                if(useServer)
                    out.println(""+8);
            }
            else if(CompareThis.equals((JButton)Fields[30]))
            {   //score button small straight
                Sounds.playSound("metalHit.wav");
                scoreButton(9);
                if(useServer)
                    out.println(""+9);
            }
            else if(CompareThis.equals((JButton)Fields[33]))
            {   //score button large straight
                Sounds.playSound("metalHit.wav");
                scoreButton(10);
                if(useServer)
                    out.println(""+10);
            }
            else if(CompareThis.equals((JButton)Fields[36]))
            {   //score button Yahtzee
                Sounds.playSound("metalHit.wav");
                scoreButton(11);
                if(useServer)
                    out.println(""+11);
            }
            else if(CompareThis.equals((JButton)Fields[39]))
            {   //score button chance
                Sounds.playSound("metalHit.wav");
                scoreButton(12);
                if(useServer)
                    out.println(""+12);
            }
            else if(CompareThis.equals((JButton)Fields[42]))
            {   //"no score" button; need setCategory to know last play
                //Sounds.playSound("metalHit.wav");
                scoreButton(13);
                if(useServer)
                    out.println(""+13);
            }
        }
    }
    
    /**
    *   Translate a message from the server to a series of function calls.
    *
    *   @param message is the string to translate
    **/
    public void serverEvent(String message)
    {
        int msg = Integer.parseInt(message);

        if(msg >= 0 && msg <=13)
        {//score button clicked
            CurrentPlayer.setCategory(msg, PlayerDice.getDieValues());
            if(CurrentPlayer == myPlayer && useServer)
                TurnAlert.cancelSound(); //tell hurryUp sound to not play
            nextPlayer();
        }
        else if(msg == 14)
        {//roll button clicked
            //no need to roll dice here; that was done in doRollBtn()
            PlayerTurn.takeTurn();
            //CurrentPlayer.showPotential(PlayerDice.getDieValues());
            
            if(!PlayerTurn.canRoll())
            {
                BtnRoll.setEnabled(false);
            }
            if(CurrentPlayer != myPlayer && CurrentPlayer instanceof Human)
            {
                BtnRoll.setEnabled(false);
            }
        }
        else if(msg == 16)
        {//Die click incoming
            try
            {
                int dieIdx = Integer.parseInt(in.readLine());
                if(CurrentPlayer != myPlayer)
                    PlayerDice.holdDie(dieIdx);
            }
            catch(IOException ex)
            {
                System.out.println("IOException1: Read " +
                        "in serverEvent()");
            }
        }
        else if(msg != 15)
            System.out.println("Unrecognized message from server: "+message);
            
        //System.out.println(message + " from applet: " + orderIdx);
        if(msg == 15)
        {//string of die values incoming
            String dieValues = null;
            try
            {
                dieValues = in.readLine();
            }
            catch(IOException ex)
            {
                System.out.println("IOException2: Read " +
                        "in serverEvent()");
            }
            
            //parse the string of die values
            Scanner parseDieValues = new Scanner(dieValues);
            for(int idx = 0; idx < 5; idx++)
            {
                PlayerDice.setNumber(idx, parseDieValues.nextInt());
            }
            PlayerDice.animateDice(false);
            CurrentPlayer.showPotential(PlayerDice.getDieValues());
        }
        
        if(!isGameOver)
            getAnotherMessage();
    }
    
    /**
    *   Set the category for the given index and pass the turn to the next 
    *   player.
    *
    *   @param idx is the score index to set
    **/
    public void scoreButton(int idx)
    {
        if(!useServer)
        {
            CurrentPlayer.setCategory(idx, PlayerDice.getDieValues());
            // if(CurrentPlayer instanceof Human)
                // TurnAlert.cancelSound(); //tell hurryUp sound to not play
            nextPlayer();
        }
    }
    
    /**
    *   Add the dice to the gameboard, roll the dice, and show potential scores.
    **/
    public void doRollBtn()
    {
        //always roll dice or server will not get sent updated info
        PlayerDice.rollDice();
        
        if(!useServer)
        {
            PlayerDice.animateDice(false);
            PlayerTurn.takeTurn();
            CurrentPlayer.showPotential(PlayerDice.getDieValues());
            
            if(!PlayerTurn.canRoll() || CurrentPlayer != myPlayer)
            {
                BtnRoll.setEnabled(false);
            }
        }
    }
    
    /**
    *   Spawn a new worker thread to receive a message from the server
    **/
    public void getAnotherMessage()
    {
        if(useServer)
        {
            //wait for server response in a separate thread
            SwingWorker<String, Object> worker = new 
                    SwingWorker<String, Object>()
            {
                protected String doInBackground()
                {
                    String message = null;
                    try
                    {
                        message = in.readLine();
                    }
                    catch(IOException ex)
                    {
                        System.out.println("IOException: Read "+
                                "in getAnotherMessage()");
                    }
                    return message;
                }
                protected void done()
                {
                    try
                    {
                        serverEvent(get());
                    }
                    catch(Exception ex)
                    {
                        System.out.println("Exception: get in getAnotherMsg");
                    }
                }
            };
            worker.execute();
        }
    }
}
