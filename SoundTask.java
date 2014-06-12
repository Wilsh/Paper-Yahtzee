/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 21, 2014
* File:        SoundTask.java
* Note:        Code in this class for loading sounds is adapted from the 
*               tutorial located at:
*      http://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
*******************************************************************************/

import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

/**
*   SoundTask.java plays a given sound after a specified period
*   of time. The timer can be cancelled so that the sound will not play.
**/
public class SoundTask extends TimerTask
{
    private Clip sound;
    private AudioInputStream audioIn;
    private Timer timer;
    
    /**
    *   Constructor starts a timer for the specified time and loads the given 
    *       sound to play when the timer completes.
    *
    *   @param theTime is the specified time to wait before playing the sound
    *
    *   @param file is the [path/]name of the specified sound to play relative 
    *           to the location of the instantiating class
    **/
	public SoundTask(long theTime, String file)
    {
        super();

        try
        {
            audioIn = AudioSystem.getAudioInputStream(
                    getClass().getResource(file));
            sound = AudioSystem.getClip();
            sound.open(audioIn);
        }
        catch(UnsupportedAudioFileException e){   
            e.printStackTrace();}
        catch(IOException e){
            e.printStackTrace();}
        catch(LineUnavailableException e){
            e.printStackTrace();}
        
        timer = new Timer(true);
        timer.schedule(this, theTime);
	}

    /**
    *   Play the sound when this task runs.
    **/
    @Override
	public void run()
    {
		sound.start();
	}
    
    /**
    *   Cancel the timer so that the sound is not played.
    **/
	public void cancelSound()
    {
        timer.cancel();
    }
}