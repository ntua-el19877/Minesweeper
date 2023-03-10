# Minesweeper


## AUTHOR:    Loukas Angelos
## GITHUB:     https://github.com/ntua-el19877


#### 1. Overview:
This is a Minesweeper game implemented in JavaFX. The game supports different difficulty levels with specific grid dimensions, mine counts, maximum time limit, and the presence of a supermine. The game can load scenario files with different game descriptions, and there is a folder called "medialab" that contains a series of scenario files with necessary information. To add new game scenario margins navigato to ['7. Add new game scenario margins'](#Addnewgamedescription).

#### 2. Requirements:
To run this game, you need JavaFX installed on your system.
To run the game, navigate to the run_Minusweeper.bat file and replace "/path/to/javafx-sdk-19" with the path to your JavaFX SDK installation directory. Or run the Main.java inside an IDE (this project was build with eclipse).

#### 3. Gameplay:
The objective of the game is to clear the minefield without detonating any mines. The game has two base difficulty levels:
Level 1: 9x9 grid with 9-11 mines and no supermine. The player has a maximum of 120-180 seconds to find all the mines.
Level 2: 16x16 grid with 35-45 mines and a supermine. The player has a maximum of 240-360 seconds to find all the mines.

#### 4. Controls:
Left-click a cell to reveal its content.
Right-click a cell to flag it as a mine.
Scenario files:
The game can load scenario files with different game descriptions. The scenario files are named as "SCENARIO-ID.txt," where ID is a unique identifier for the scenario. 

#### 5. Scenario Syntax:
The first line contains a number to specify the difficulty level for the scenario.
The second line specifies the total number of mines for the scenario, which should be within the acceptable range for the difficulty level.
The third line specifies the maximum time limit in seconds for the scenario.
The fourth line specifies whether there is a supermine in the scenario, with "1" for presence and "0" for absence.

#### 6. Folder Structure:
The source code files are in the "src" folder.
The images used in the game are in the "icons" folder.
The scenario game margins files are in the "medialab" folder.

#### 7. Add new game scenario margins 
Make a text file named SCENARIO-*-DIFFICULTY-MARGINS.txt where * is the game difficulty of the level you will create. In the text file there will be 8 rows where at each row there will be a number. 
Row 1:  The number: * of the named file (SCENARIO-*-DIFFICULTY-MARGINS.txt)
Row 2:  The number of Rows the Minesweeper scenario will have 
Row 3:  The number of Columns the Minesweeper scenario will have
Row 4:  The minimun number of bombs
Row 5:  Te maximun number of bombs
Row 6:  The minimun number of seconds
Row 7:  The maximun number of seconds
Row 8:  1 if there is a mega bomb and 0 if there isn't

#### Enjoy the game!
