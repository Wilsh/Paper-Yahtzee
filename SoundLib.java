/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 21, 2014
* File:        SoundLib.java
* Note:        Code in this class for loading sounds is adapted from the 
*               tutorial located at:
*      http://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
*******************************************************************************/

import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

/**
*   SoundLib.java loads one or more given sound files and plays them when the 
*       playSound() method is called. The name for each file must be unique.
*       The playRandom() method will randomly choose a sound to play.
**/
public class SoundLib
{
    private Clip[] sounds;
    private AudioInputStream audioIn;
    private int numFiles;
    private String[] filenames;
    private java.util.Random randomNumGenerator;
    
    /**
    *   Constructor loads the given sounds and populates an array with the
    *       name of each file.
    *
    *   @param files is an array of sound files to load
    **/
	public SoundLib(String[] files)
    {
        numFiles = files.length;
        sounds = new Clip[numFiles];
        filenames = new String[numFiles];
        randomNumGenerator = new java.util.Random();
        
        for(int idx = 0; idx < numFiles; idx++)
        {
            try
            {
                audioIn = AudioSystem.getAudioInputStream(
                        getClass().getResource(files[idx]));
                sounds[idx] = AudioSystem.getClip();
                sounds[idx].open(audioIn);
            }
            catch(UnsupportedAudioFileException e)
            {   
                e.printStackTrace();
                break;
            }
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
            catch(LineUnavailableException e)
            {
                e.printStackTrace();
                break;
            }
            
            //remove path information and put the filename in an array
            filenames[idx] = new File(files[idx]).getName();
        }
	}
    
    /**
    *   Play the sound with the given file name. Prints an error message if
    *       the given file name has not been previously loaded.
    **/
	public void playSound(String name)
    {
        int idx = 0;
        boolean found = false;
        
        //find the index of the array where the given name is located
        for(; idx < numFiles; idx++)
        {
            if(filenames[idx].equals(name))
            {
                found = true;
                break;
            }
        }
        
        if(((idx + 1) == numFiles) && !found)
            System.out.println("The file " + name + 
                    " does not exist in SoundLib");
        else
        {
            sounds[idx].stop();
            sounds[idx].setFramePosition(0);
            sounds[idx].start();
        }
    }
    
    /**
    *   Play a random sound.
    **/
    public void playRandom()
    {
        int idx = (int)(randomNumGenerator.nextDouble() * (numFiles - 0.001));
        
        sounds[idx].stop();
        sounds[idx].setFramePosition(0);
        sounds[idx].start();
    }
}