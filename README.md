Paper-Yahtzee
=============

A themed Yahtzee remake

![game preview](https://github.com/Wilsh/Paper-Yahtzee/blob/master/preview.png)

Scoring variations
------------------

Joker rules are not used to score multiple yahtzees. 
Instead, a player is given an additional turn when a subsequent
yahtzee is scored in the yahtzee category (if a non-zero value has been
scored first). Bonus points are given for each yahtzee after the first. 
A player may choose to discard the score for any turn.
    
Game modes
----------

'Local' allows for a single-player game or a two-player game (human
versus a computer player). 'Internet' requires the server to be running 
before starting a game and supports 2-5 human players. After connecting to 
the server, players may indicate their desire to begin the game by clicking 
the 'play now' button. If all players have clicked the 'play now' button, 
the game will begin (if at least 2 players are connected). Once 5 
players connect to the server, the game will begin automatically.

Notes
-----

Play order is determined by the order of connection to the server. In
local game mode, the human player goes first.
Click a player at the top of the screen to see that player's scoresheet.
Click on a die to hold/release it. Click the roll button during a robot
player's turn to advance the robot's turn.
An alert sound will play if a player has not completed their turn after
one minute.

Cheats (local game mode only)
------

Scores may be pre-loaded onto the scoresheet in a single player game by 
entering values at the starting screen.
Right-click a die to change its value.

Known limitations
-----------------

A human player may interfere with the die holds during a 
robot player's turn.
The number of yahtzee bonuses will not display properly once the 10th
bonus has been scored (an unlikely scenario in regular gameplay).
If a player disconnects while in the middle of an internet game, the server
will crash.

Design
------

The Yahtzee class contains the players and dice, which are passed to 
turns. The use of the turn class has greatly diminished since the action
driving the game has been moved to the GUI. Each player contains their 
own scoresheet and has subclass-specific methods for scoring decisions.
Text is displayed as a series of images with each image representing
one character. This is accomplished with the CharacterImage class, which
has methods for transforming an int or String into an array of ImageIcons.
Sounds are handled by the SoundTask and SoundLib classes. SoundTask loads a
sound file and plays it after a specified period of time. SoundLib loads one
or more sound files and plays a sound when the playSound method is called.
The server allows clients to communicate by echoing each message it receives
to all connected clients. The clients translate these messages into a series
of function calls. The html file uses the bootstrap framework for form
validation. Because bootstrap automatically resizes elements depending on 
the screen size, the elements may not display properly on a screen with low
resolution or a browser window that is not maximized.

Sounds are not included because I am not free to distribute them.
Specific server information has been removed from these files.
