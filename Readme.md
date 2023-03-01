#title of project
#explanation
#installation guide
Minesweeper

AUTHORS:    Loukas


Overview:
This is a Minesweeper game implemented in JavaFX. The game supports two difficulty levels with specific grid dimensions, mine counts, maximum time limit, and the presence of a supermine. The game can load scenario files with different game descriptions, and there is a folder called "medialab" that contains a series of scenario files with necessary information. To add new game descriptions . The scenario files are named as "SCENARIOID.txt" and have a predefined format.

Requirements:
To run this game, you need JavaFX 16 installed on your system.

Gameplay:
The objective of the game is to clear the minefield without detonating any mines. The game has two difficulty levels:
Level 1: 9x9 grid with 9-11 mines and no supermine. The player has a maximum of 120-180 seconds to find all the mines.
Level 2: 16x16 grid with 35-45 mines and a supermine. The player has a maximum of 240-360 seconds to find all the mines.

Controls:

Left-click a cell to reveal its content.
Right-click a cell to flag it as a mine.
Double-click a cell to reveal its neighboring cells if it has the same number of flags as mines.
Scenario files:
The game can load scenario files with different game descriptions. The scenario files are named as "SCENARIOID.txt," where ID is a unique identifier for the scenario. A scenario file consists of four lines with a predefined syntax:

The first line contains a 1 or 2 to specify the difficulty level for the scenario.
The second line specifies the total number of mines for the scenario, which should be within the acceptable range for the difficulty level.
The third line specifies the maximum time limit in seconds for the scenario.
The fourth line specifies whether there is a supermine in the scenario, with "1" for presence and "0" for absence.
Folder Structure:

The source code files are in the "src" folder.
The images used in the game are in the "images" folder.
The scenario files are in the "medialab" folder.
To run the game, navigate to the src folder and execute the following command:
javac --module-path /path/to/javafx-sdk-16/lib --add-modules javafx.controls Minesweeper.java
java --module-path /path/to/javafx-sdk-16/lib --add-modules javafx.controls Minesweeper

Replace "/path/to/javafx-sdk-16" with the path to your JavaFX SDK 16 installation directory.

Enjoy the game!