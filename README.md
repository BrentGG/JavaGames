# JavaGames

I wanted to learn Java so I decided to recreate some classic games in Java. All the games apply the model-view-controller design pattern and use Swing for the UI. The following sections contain some more info on each game as well as a demo video.

# Snake

The snake is controlled using the arrow keys. The snake gets longer when eating an apple (red square). Every apple awards 100 points. The game can be started and stopped with the buttons in the menu. Additionally, the speed of the snake can be changed. There are also a few settings that can be turned on or off:
- Walls: If this is on, the sides of the "field" will act as a wall, killing the snake when a collision occurs. Otherwise the snake will simply appear on the opposite side.
- Obstacles: If this is turned on, randomly generated obstacles will appear on the field.
- AI: If this is turned on, the game will play itself. Though the AI isn't very good, it is still amusing to watch.

https://github.com/BrentGG/JavaGames/assets/61016553/5fc5aa97-c676-4226-8f7a-f51ac271015d

# Tetris

The tetrominoes are controlled using the arrow keys. The up arrow key spins the tetromino, holding the down arrow key performs a soft drop. Pressing the space bar performs a hard drop. The points scored are as follows:
- Soft drop: 1x distance
- Hard drop: 2x distance
- Single line clear: 100
- Double line clear: 200
- Triple line clear: 300
- Tetris line clear: 800
Every 2500 points the drop speed increases (up to a certain point).

https://github.com/BrentGG/JavaGames/assets/61016553/e1708a3b-540e-44ed-aeb8-91a5a3df698e

# Pong

The left paddle is controlled using Z and S, the right paddle using O and L. AI can be activated for both sides (if both paddles are controlled by AI and they hit the ball directly to eachothers center they will of course not move). The ball physics calculations are adapted from the formulas in [this StackExchange answer](https://gamedev.stackexchange.com/a/4255).

https://github.com/BrentGG/JavaGames/assets/61016553/bb009ef5-5e2c-4f88-9e91-77c3f6181b72

# Sudoku

The sudoku puzzles are imported from a CSV file (unfortunately I don't remember where I found it). The squares are simply filled in by clicking on them and selecting the desired number. When selecting a square with a number in it already, all the squares with that same number are highlighted. When the sudoku is entirely filled in it can be checked (when this happens a popup appears telling the user whether it is correct, unfortunately this was not recorded and doesn't appear in the demo video). It is also possible to reset the puzzle, removing all the numbers that were filled in by the player.

https://github.com/BrentGG/JavaGames/assets/61016553/d6052cd8-f05b-4c60-85db-a225e3416bf0

# Tic Tac Toe

Simple two player tic tac toe. Clicking on an empty square fills it with the current player's symbol. When all squares are full a popup appears telling the players who won (which unfortunately was not recorded).

https://github.com/BrentGG/JavaGames/assets/61016553/1bd4cb47-3d3b-4205-9ee8-4531147aa8aa



