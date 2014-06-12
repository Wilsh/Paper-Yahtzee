/*
* Course:      CSC421
* Assignment:  #3
* Author:      Andrew Seligman
* Date:        May 7, 2014
* File:        RandomNum.java
*******************************************************************************/

import java.util.Random;

/**
*   RandomNum.java generates a pseudorandom number between 1 and 6. A RandomNum 
*   object can be constructed with a positive integer for testing or a negative
*   integer for unpredictable values
**/
public class RandomNum
{
    private java.util.Random randomNumGenerator;
	
	/**
    *   Constructor generates a pseudorandom number between 0 and 1
    *
    *   @param seed is a seed for a pseudorandom number generator that 
    *   represents a value on a die. Using a value < 0 results in a different
    *   number each time; using a value >= 0 allows a game to be repeated
    *   for testing purposes.
    **/
	public RandomNum(int seed)
	{
		if(seed < 0)
		{
            randomNumGenerator = new java.util.Random();
        }
		else
		{
            randomNumGenerator = new java.util.Random(seed);
        }
	}
	
	/**
    *   Get a number between 1 and 6
    *
	*   @return the generated number
    **/
	public int getNum()
	{
		return ((int)((randomNumGenerator.nextDouble() * 5.999)) + 1);
	}
}
