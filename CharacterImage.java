/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        CharacterImage.java
*******************************************************************************/

import javax.swing.*;
import java.awt.*;

/**
*   CharacterImage.java transforms alphanumeric characters into arrays of 
*   JLabels that contain the characters' representations in images.
**/
public class CharacterImage
{
    private int number;
    private ImageIcon[] Numbers;
    private ImageIcon[] LowLetters;
    private ImageIcon[] SpecialChar;
    private boolean held = false;
	
	/**
    *   Constructor initializes the ImageIcons that will be placed into
    *       JLabel arrays.
    **/
	public CharacterImage()
	{try{
        Numbers = new ImageIcon[11];
        LowLetters = new ImageIcon[26];
        SpecialChar = new ImageIcon[6];
        
        Numbers[0] = new ImageIcon(getClass().getResource("0.png"));
        Numbers[1] = new ImageIcon(getClass().getResource("1.png"));
        Numbers[2] = new ImageIcon(getClass().getResource("2.png"));
        Numbers[3] = new ImageIcon(getClass().getResource("3.png"));
        Numbers[4] = new ImageIcon(getClass().getResource("4.png"));
        Numbers[5] = new ImageIcon(getClass().getResource("5.png"));
        Numbers[6] = new ImageIcon(getClass().getResource("6.png"));
        Numbers[7] = new ImageIcon(getClass().getResource("7.png"));
        Numbers[8] = new ImageIcon(getClass().getResource("8.png"));
        Numbers[9] = new ImageIcon(getClass().getResource("9.png"));
        Numbers[10] = new ImageIcon(getClass()
                .getResource("blankNum.png"));
                
        LowLetters[0] = new ImageIcon(getClass().getResource("a.png"));
        LowLetters[1] = new ImageIcon(getClass().getResource("b.png"));
        LowLetters[2] = new ImageIcon(getClass().getResource("c.png"));
        LowLetters[3] = new ImageIcon(getClass().getResource("d.png"));
        LowLetters[4] = new ImageIcon(getClass().getResource("e.png"));
        LowLetters[5] = new ImageIcon(getClass().getResource("f.png"));
        LowLetters[6] = new ImageIcon(getClass().getResource("g.png"));
        LowLetters[7] = new ImageIcon(getClass().getResource("h.png"));
        LowLetters[8] = new ImageIcon(getClass().getResource("i.png"));
        LowLetters[9] = new ImageIcon(getClass().getResource("j.png"));
        LowLetters[10] = new ImageIcon(getClass().getResource("k.png"));
        LowLetters[11] = new ImageIcon(getClass().getResource("l.png"));
        LowLetters[12] = new ImageIcon(getClass().getResource("m.png"));
        LowLetters[13] = new ImageIcon(getClass().getResource("n.png"));
        LowLetters[14] = new ImageIcon(getClass().getResource("o.png"));
        LowLetters[15] = new ImageIcon(getClass().getResource("p.png"));
        LowLetters[16] = new ImageIcon(getClass().getResource("q.png"));
        LowLetters[17] = new ImageIcon(getClass().getResource("r.png"));
        LowLetters[18] = new ImageIcon(getClass().getResource("s.png"));
        LowLetters[19] = new ImageIcon(getClass().getResource("t.png"));
        LowLetters[20] = new ImageIcon(getClass().getResource("u.png"));
        LowLetters[21] = new ImageIcon(getClass().getResource("v.png"));
        LowLetters[22] = new ImageIcon(getClass().getResource("w.png"));
        LowLetters[23] = new ImageIcon(getClass().getResource("x.png"));
        LowLetters[24] = new ImageIcon(getClass().getResource("y.png"));
        LowLetters[25] = new ImageIcon(getClass().getResource("z.png"));
                
        SpecialChar[0] = new ImageIcon(getClass()
                .getResource("blankLet.png"));
        SpecialChar[1] = new ImageIcon(getClass()
                .getResource("colon.png"));
        SpecialChar[2] = new ImageIcon(getClass()
                .getResource("apostrophe.png"));
        SpecialChar[3] = new ImageIcon(getClass()
                .getResource("pound.png"));
        SpecialChar[4] = new ImageIcon(getClass()
                .getResource("unknown.png"));
        SpecialChar[4] = new ImageIcon(getClass()
                .getResource("exclamation.png"));
        }catch (Exception e) { e.printStackTrace();}
    }
    
    /**
    *   Transforms an int to an array of JLabels with images representing 
    *       that number. A number with fewer than five digits is padded
    *       with blank images to create a minimum of five JLabels in 
    *       the returned array.
    *
    *   This method is used to display numbers on the scoresheet. Use
    *       getLetters() to transform numbers without any padding added.
    *
    *   @param number is the number to be transformed into an array of images
    *
    *   @return images is the array of JPanels
    **/
    public JLabel[] getNumber(int number)
    {
        String aNum = new String(String.valueOf(number));
        int length = aNum.length();
        JLabel[] images = new JLabel[length<5?5:length];
        int nextNum;
        int nextChar = 0;
        
        for(int idx = 0; idx < 5 || idx < length; idx++)
        {
            images[idx] = new JLabel();
            //pad a number with 1/2/3/4 digits with 4/3/2/1 blank images
            if((idx == 0 && length < 5) || (idx == 1 && length < 4) ||
                    (idx == 2 && length < 3) || (idx == 3 && length < 2))
            {
                images[idx].setIcon(Numbers[10]);
                continue;
            }
            else
            {
                nextNum = (int)aNum.charAt(nextChar) - 48;
                nextChar++;
            }

            switch(nextNum)
            {
                case 0:{
                    images[idx].setIcon(Numbers[0]);
                    break;
                }
                case 1:{
                    images[idx].setIcon(Numbers[1]);
                    break;
                }
                case 2:{
                    images[idx].setIcon(Numbers[2]);
                    break;
                }
                case 3:{
                    images[idx].setIcon(Numbers[3]);
                    break;
                }
                case 4:{
                    images[idx].setIcon(Numbers[4]);
                    break;
                }
                case 5:{
                    images[idx].setIcon(Numbers[5]);
                    break;
                }
                case 6:{
                    images[idx].setIcon(Numbers[6]);
                    break;
                }
                case 7:{
                    images[idx].setIcon(Numbers[7]);
                    break;
                }
                case 8:{
                    images[idx].setIcon(Numbers[8]);
                    break;
                }
                case 9:
                    images[idx].setIcon(Numbers[9]);
            }
        }
        return images;
    }

    /**
    *   Transforms a string of characters into an array of JLabels with images 
    *       representing those characters. Only the letters a-z (uppercase 
    *       letters are cast to lowercase), digits 0-9 , and certain special 
    *       characters [blank space, colon, apostrophe, pound sign] are 
    *       represented; any other characters are represented with a 
    *       question mark.
    *
    *   @param words is the string to be transformed into an array of images
    *
    *   @return images is the array of JPanels
    **/
    public JLabel[] getLetters(String words)
    {
        int length = words.length();
        JLabel[] images = new JLabel[length];
        char nextChar = '\0';
        
        for(int idx = 0; idx < length; idx++)
        {
            images[idx] = new JLabel();
            nextChar = Character.toLowerCase(words.charAt(idx));

            switch(nextChar)
            {
                case 'a':{
                    images[idx].setIcon(LowLetters[0]);
                    break;
                }
                case 'b':{
                    images[idx].setIcon(LowLetters[1]);
                    break;
                }
                case 'c':{
                    images[idx].setIcon(LowLetters[2]);
                    break;
                }
                case 'd':{
                    images[idx].setIcon(LowLetters[3]);
                    break;
                }
                case 'e':{
                    images[idx].setIcon(LowLetters[4]);
                    break;
                }
                case 'f':{
                    images[idx].setIcon(LowLetters[5]);
                    break;
                }
                case 'g':{
                    images[idx].setIcon(LowLetters[6]);
                    break;
                }
                case 'h':{
                    images[idx].setIcon(LowLetters[7]);
                    break;
                }
                case 'i':{
                    images[idx].setIcon(LowLetters[8]);
                    break;
                }
                case 'j':{
                    images[idx].setIcon(LowLetters[9]);
                    break;
                }
                case 'k':{
                    images[idx].setIcon(LowLetters[10]);
                    break;
                }
                case 'l':{
                    images[idx].setIcon(LowLetters[11]);
                    break;
                }
                case 'm':{
                    images[idx].setIcon(LowLetters[12]);
                    break;
                }
                case 'n':{
                    images[idx].setIcon(LowLetters[13]);
                    break;
                }
                case 'o':{
                    images[idx].setIcon(LowLetters[14]);
                    break;
                }
                case 'p':{
                    images[idx].setIcon(LowLetters[15]);
                    break;
                }
                case 'q':{
                    images[idx].setIcon(LowLetters[16]);
                    break;
                }
                case 'r':{
                    images[idx].setIcon(LowLetters[17]);
                    break;
                }
                case 's':{
                    images[idx].setIcon(LowLetters[18]);
                    break;
                }
                case 't':{
                    images[idx].setIcon(LowLetters[19]);
                    break;
                }
                case 'u':{
                    images[idx].setIcon(LowLetters[20]);
                    break;
                }
                case 'v':{
                    images[idx].setIcon(LowLetters[21]);
                    break;
                }
                case 'w':{
                    images[idx].setIcon(LowLetters[22]);
                    break;
                }
                case 'x':{
                    images[idx].setIcon(LowLetters[23]);
                    break;
                }
                case 'y':{
                    images[idx].setIcon(LowLetters[24]);
                    break;
                }
                case 'z':{
                    images[idx].setIcon(LowLetters[25]);
                    break;
                }
                case '0':{
                    images[idx].setIcon(Numbers[0]);
                    break;
                }
                case '1':{
                    images[idx].setIcon(Numbers[1]);
                    break;
                }
                case '2':{
                    images[idx].setIcon(Numbers[2]);
                    break;
                }
                case '3':{
                    images[idx].setIcon(Numbers[3]);
                    break;
                }
                case '4':{
                    images[idx].setIcon(Numbers[4]);
                    break;
                }
                case '5':{
                    images[idx].setIcon(Numbers[5]);
                    break;
                }
                case '6':{
                    images[idx].setIcon(Numbers[6]);
                    break;
                }
                case '7':{
                    images[idx].setIcon(Numbers[7]);
                    break;
                }
                case '8':{
                    images[idx].setIcon(Numbers[8]);
                    break;
                }
                case '9':{
                    images[idx].setIcon(Numbers[9]);
                    break;
                }
                case 32:{//blank space
                    images[idx].setIcon(SpecialChar[0]);
                    break;
                }
                case 58:{//colon
                    images[idx].setIcon(SpecialChar[1]);
                    break;
                }
                case 39:{//apostrophe
                    images[idx].setIcon(SpecialChar[2]);
                    break;
                }
                case 35:{//pound sign
                    images[idx].setIcon(SpecialChar[3]);
                    break;
                }
                case 33:{//exclamation point
                    images[idx].setIcon(SpecialChar[5]);
                    break;
                }
                default:
                    images[idx].setIcon(SpecialChar[4]);
            }
        }
        return images;
    }
    
}
