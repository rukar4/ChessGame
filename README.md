# Chess Game
This is a complete Java implementation of a chess game that can be played in the terminal. The server is equipped to 
handle multiple clients playing multiple games simultaneously. It also allows for clients to watch other games in 
progress. The game supports all standard chess rules, including castling, en passant, and promotion. It is also equipped
with a persistent database that stores games and players, allowing for players to resume games later. Players can log 
into their account with a simple username and password, or register a new user account.

[//]: # (## Running the Application Locally)

[//]: # (WIP)

## Chess Client
This is a terminal chess client that allows users to play chess against each other and watch other games in progress.
The client starts with a simple login/register repl, then enters a game select repl. Finally, when the player joins a 
game, it starts a game repl. The game repl includes the chess board and allows the player to highlight valid moves and 
to move pieces. It also employs web sockets to notify the player when someone joins the game to watch/play or when a 
move is made by the opponent.

## Chess Server
The server manages all the chess logic and data management. It uses web-sockets to listen and respond to client actions
with the respective notifications. It also contains the APIs to login/register, get valid moves, and make moves in a 
specific game. The server communicates with a MySQL database using DAOs to fetch user and game data. 