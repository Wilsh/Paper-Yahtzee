Paper-Yahtzee
=============

A themed Yahtzee remake

Sounds are not included because I am not free to distribute them.
Specific server information has been removed from these files.

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
resolution or a browser that is not maximized.
    
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
The required drawn object is the black border surrounding the player's 
name at the top of the screen. This is performed by overriding the 
paintComponent method in the player class. The border is given to the
player in control of the applet (never for a computer player).
An alert sound will play if a player has not completed their turn after
one minute.

Cheats
------

Scores may be pre-loaded onto the scoresheet in a single player game
(local game mode only) by entering values at the starting screen. I had
a little trouble with selecting a textArea during the presentation (using 
chrome; an older version, perhaps, since it works fine on my desktop), but 
after clicking around a bit I was able to select it. You may tab through
the textAreas once you get the cursor in one of them. If there is no value
in the textArea, the game will crash (each textArea is initially populated 
with a 0).
Right-click a die to change its value.

Known limitations
-----------------

A human player may interfere with the die holds during a 
robot player's turn.
The number of yahtzee bonuses will not display properly once the 10th
bonus has been scored (an unlikely scenario in regular gameplay).
The potential score column is not updated when a player cheats by selecting
individual die values since the cheat mechanism is localized to a Die
object and has no way to signal the Scoresheet that the value has changed.
If a player disconnects while in the middle of an internet game, the server
will crash.
Replaying a game works in local mode only. If replaying after a game versus
the computer, you must select the vs computer button and then the single
player button in order to switch to a single player game.
The applet will crash after closing the end-of-game popup in internet mode.
The client shows only a blank screen if the server is not available.
Die holds are not sent to the server during an internet game, so other
players' holds are not visible. The state of their dice after a roll is 
visible, however.
During an internet game, the score underneath a non-local player's name may
not accurately reflect their yahtzee bonus points.
The game will not end if a non-zero score is entered for each category at the 
single player starting screen.

Scoring variations
------------------

Joker rules are not used to score multiple yahtzees. 
Instead, a player is given an additional turn when a subsequent
yahtzee is scored in the yahtzee category (if a non-zero value has been
scored first). Bonus points are given for each yahtzee after the first. 
A player may choose to discard the score for any turn.
