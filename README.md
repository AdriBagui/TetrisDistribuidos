Tetris Distribuidos

Tetris Distribuidos is a robust, Java-based Tetris implementation featuring a custom Swing UI, distinct game modes (Modern vs. Classic NES), and full multiplayer capabilities (Local & Online).

The project demonstrates a distributed system architecture using TCP sockets for online matchmaking and real-time gameplay synchronization.

🎮 Features

Game Modes

Modern Tetris:

Mechanics: Super Rotation System (SRS), Hard Drop, Hold Piece, 7-Bag Random Generator.

Garbage System: Competitive play where clearing lines sends garbage rows to the opponent.

Classic (NES) Tetris:

Mechanics: Nintendo Rotation System (no wall kicks), Classic gravity curve, NES Random Generator, no Hold piece.

Goal: Score-based competition.

Multiplayer Types

Local Multiplayer: Play 1v1 on the same keyboard/screen.

Online Multiplayer:

Quick Match: Automatically pair with a random opponent.

Private Lobby: Host a game and share the Room ID with a friend.

Join Lobby: Connect to a specific room via ID.

Technical Highlights

Custom UI: A dark-themed, specialized Swing interface with custom components (ModernButton, ModernScrollBar).

Input System: Configurable key bindings saved to XML with KeyInputHandler supporting DAS (Delayed Auto Shift) and ARR (Automatic Repeat Rate).

Physics Engine: Accurate implementation of gravity, lock delays, and rotation rules (SRS & NES).

Networking: Multithreaded server handling matchmaking queues and relaying game state between clients.

🚀 Getting Started

Prerequisites

Java Development Kit (JDK) 17 or higher.

Maven or Gradle (optional, if building from source).

Running the Application

This project consists of two main entry points: the Server (for online play) and the Client (the game itself).

1. Start the Server

If you want to play online, the server must be running first.

# Compile and run the Server class
java server.Server


The server listens on port 7777 by default.

2. Start the Client

Run the main client application. You can optionally pass the Server IP and Port as arguments.

# Run the Client (defaults to localhost:7777)
java client.Main

# Or specify a remote server IP
java client.Main 192.168.1.50 7777


🕹️ Controls

Default controls can be customized in the Settings menu.

Player 1 (Default)

Action

Key

Move Left

A

Move Right

D

Soft Drop

S

Hard Drop

W

Rotate Left

J

Rotate Right

K

Flip (180°)

L

Hold Piece

Shift

Player 2 (Local Multiplayer)

Action

Key

Move Left

Left Arrow

Move Right

Right Arrow

Soft Drop

Down Arrow

Hard Drop

Up Arrow

Rotate Left

Numpad 1

Rotate Right

Numpad 2

Flip (180°)

Numpad 3

Hold Piece

Ctrl

Global Controls:

Back to Menu: Enter

🏗️ Architecture

Package Structure

client: Contains the Swing GUI (MainFrame, MainPanel), input handling, and the client-side logic for connecting to the server.

server: Contains the Server socket listener, MatchmakingHandler (queues and lobbies), and GameCommunicationHandler (relays input between players).

tetris: The core game library.

boards: Logic for the game grid, including BoardWithPhysics (local simulation) and ReceiverBoard (network replication).

tetrominoes: Definitions for the 7 pieces (I, J, L, O, S, T, Z) and generation logic.

physics: Gravity calculations, rotation systems (SRS/NES), and garbage handling.

Networking Protocol

The game uses a custom byte-based protocol for efficiency:

Handshake: Client sends ConnectionMode (Quick Play, Host, etc.).

Game Start: Server sends a synchronized random seed (long) to both clients.

Gameplay Loop:

Movement: [X, Y, Rotation, LockedStatus] is broadcasted to the opponent.

Attacks: [Lines, HoleIndex] sent when clearing lines in Modern mode.

⚙️ Customization

Key bindings are stored in src/main/resources/config/keybindings.xml. If this file is missing, the application generates it with default values on startup.