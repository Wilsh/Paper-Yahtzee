/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        YahtzeeServer.java
* Note:        The code in this file has been adapted from 
*               SocketThrdServer.java, a file provided by Dr. Spiegel
*******************************************************************************/

import java.io.*;
import javax.swing.*;
import java.net.*;

/**
*   The ServerData class holds data that is used by multiple threads
**/
class ServerData
{
    final int minPlayers = 2;
    final int maxPlayers = 5;
    volatile int numConnected = 0;
    volatile int numReadyToStart = 0;
    volatile Worker[] Players = new Worker[maxPlayers];
    volatile boolean[] Ready = new boolean[maxPlayers];
    volatile String[] Names = new String[maxPlayers];
}

/**
*   YahtzeeServer is the server for the Yahtzee applet. It provides a means for
*   multiple Yahtzee applets to communicate.
**/
public class YahtzeeServer
{
    static final int PORT = 0;
    static ServerSocket server;
    
    /**
    *   Initialize server data and accept new connections
    **/
    public static void main(String[] args)
    {
        ServerData Stats = new ServerData();
        //set ready flag for all players to false
        for(int idx = 0; idx < Stats.maxPlayers; idx++)
        {
            Stats.Ready[idx] = false;
        }

        try
        {
            server = new ServerSocket(PORT);
            System.out.println("Server is running!");
        }
        catch(IOException e)
        {
            System.out.println("Server failed to listen on port");
            System.exit(-1);
        }

        while(true)
        {
            Worker w;
            try
            {
                //listen for new connections
                w = new Worker(server.accept(), Stats);
                System.out.println("New connection accepted: "+Stats.numConnected);
                Thread t = new Thread(w);
                t.start();
                Thread.sleep(250);
            }
            catch(IOException ex)
            {
                System.out.println("Accept failed");
                System.exit(-1);
            }
            catch(Exception ex)
            {
                System.out.println("Server Exception: sleep failed");
                System.exit(-1);
            }
        }
    }
    
    /**
    *   Close the ServerSocket
    **/
    protected void finalize()
    {
        try
        {
            server.close();
        }
        catch(IOException e)
        {
            System.out.println("Could not close socket");
            System.exit(-1);
        }
    }
}

/**
*   The Worker class organizes player connections and communicates with 
*   applets to play the game.
**/
class Worker implements Runnable
{
    Socket client;
    ServerData stats;
    BufferedReader in = null;
    PrintWriter out = null;
    int myIdx;
    
    /**
    *   Constructor sets play order and adds this object to the Players array
    **/
    Worker(Socket newClient, ServerData Stats)
    {
        client = newClient;
        this.stats = Stats;
        if(stats.numConnected < stats.maxPlayers)
        {
            myIdx = stats.numConnected;
            stats.Players[myIdx] = this;
            stats.numConnected++;
        }
        else
        {
            //the maximum number of players is already connected
        }
    }
    
    /**
    *   Wait for all players to give the ready signal before beginning the game
    **/
    public void run()
    {
        String word;
        boolean allReady = false;
        
        try
        {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            Thread.sleep(1000);
            out = new PrintWriter(client.getOutputStream(), true);
            stats.Names[myIdx] = in.readLine();
        }
        catch(IOException ex)
        {
            System.out.println("Server IOException: IO failed");
            System.exit(-1);
        }
        catch(Exception ex)
        {
            System.out.println("Server Exception in Worker");
            System.exit(-1);
        }
        
        //check if this player is ready using a separate thread
        SwingWorker BGworker = new SwingWorker(){
            String value;
            protected String doInBackground()
            {
                try
                {
                    value = in.readLine();
                    System.out.println("BGworker"+myIdx+": "+value);
                    if(value.equals("READY"))
                        stats.Ready[myIdx] = true;
                    else
                    {
                        System.out.println("Server worked received "+
                            "unexpected value");
                    }
                }
                catch(IOException ex)
                {
                    System.out.println("BGworker IOException: IO failed");
                    System.exit(-1);
                }
                return null;
            }
        };
        BGworker.execute();
    
        while(!allReady)
        {
            try
            {
                if(stats.numConnected >= stats.maxPlayers)
                {
                    //in case extra connections have been made
                    stats.numConnected = stats.maxPlayers;
                    BGworker.cancel(true);
                    playGame();
                }
                else
                {
                    if(stats.numConnected >= stats.minPlayers)
                    {
                        for(int idx = 0; idx < stats.numConnected; idx++)
                        {
                            if(stats.Ready[idx] == false)
                                break;
                            else if(idx == (stats.numConnected - 1))
                            {
                                allReady = true;
                                BGworker.cancel(true);
                            }
                        }
                    }
                }
                Thread.sleep(1000);
            }
            catch(Exception ex)
            {
                System.out.println("Server Exception in Worker: allReady loop");
                System.exit(-1);
            }
        }
        playGame();
    }
    
    /**
    *   Send player data to applets and begin broadcasting play choices
    **/
    public void playGame()
    {
        boolean isOver = false;
        String message;
        
        //signal client to begin game
        //send order index, # players, and their names
        try
        {
            out.println(""+myIdx);
            out.println(""+stats.numConnected);

            for(int idx = 0; idx < stats.numConnected; idx++)
            {
                while(stats.Names[idx] == null)
                {//eliminate race condition where last player thread has not
                 //yet added its String to Names
                    Thread.sleep(1000);
                }
                out.println(stats.Names[idx]);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Server Exception in Worker: playGame "+myIdx);
            System.exit(-1);
        }
        
        while(!isOver)
        {
            try
            {
                message = in.readLine();
                if(message.equals("GAMEOVER"))
                    isOver = true;
                else
                    broadcast(message);
            }
            catch(IOException ex)
            {
                System.out.println("Server IOException: IO failed in playGame");
                System.exit(-1);
            }
            catch(Exception ex)
            {
                System.out.println("Server Exception in Worker: playGame");
                System.exit(-1);
            }
        }
        
        if(myIdx == 0)
        {
            gameOver(stats);
            System.out.println("Game complete. Ready for new game.");
        }
        //System.exit(0);
    }
    
    /**
    *   Reset the server data to begin a new game
    *
    *   @param stats is the object containing server data
    **/
    public void gameOver(ServerData stats)
    {
        stats.numConnected = 0;
        stats.numReadyToStart = 0;
        stats.Players = new Worker[stats.maxPlayers];
        stats.Names = new String[stats.maxPlayers];
        //set ready flag for all players to false
        for(int idx = 0; idx < stats.maxPlayers; idx++)
        {
            stats.Ready[idx] = false;
        }
    }
    
    /**
    *   Send a String to each connected client
    **/
    public void broadcast(String message)
    {
        for(int idx = 0; idx < stats.numConnected; idx++)
        {
            stats.Players[idx].out.println(message);
        }
        System.out.println("Msg thread "+myIdx+" "+message);
    }
}