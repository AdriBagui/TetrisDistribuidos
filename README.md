# **Tetris Distribuidos 🎮**

**Tetris Distribuidos** is a Java-based implementation of the classic puzzle game, designed with a focus on Distributed Systems concepts. It features a dual-engine core supporting both **Modern Tetris** (SRS, Hold, Garbage) and **Classic NES Tetris** mechanics, playable in Single Player, Local Multiplayer, and Online Multiplayer modes.

## **✨ Features**

### **🕹️ Game Modes**

* **Modern Tetris:**  
  * **Super Rotation System (SRS):** Accurate wall-kicks and movement logic using standard lookup tables.  
  * **7-Bag Generator:** Ensures balanced piece distribution where every 7 pieces contains one of each type.  
  * **Hold Piece:** Strategic storage of tetrominoes to swap later.  
  * **Ghost Piece:** Visual guide showing where the piece will land.  
  * **Garbage System:** Clearing lines sends garbage rows to the opponent in multiplayer.  
* **Classic (NES) Tetris:**  
  * **Authentic Physics:** Replicates the specific gravity curve, entry delays, and lack of lock delay found in the NES version.  
  * **Nintendo Rotation System:** Strict rotation rules without wall-kicks.  
  * **Retro Randomizer:** Traditional biased random generation (Roll \+ Reroll).

### **🌐 Multiplayer Capabilities**

* **Local Multiplayer:**  
  * Two players on one keyboard using split-screen.  
  * Uses Java Pipes (PipedInputStream/PipedOutputStream) to simulate network latency and communication between boards locally, treating local games as a "networked" session within a single process.  
* **Online Multiplayer:**  
  * **Client-Server Architecture:** Centralized server handling matchmaking and game relays.  
  * **Matchmaking:**  
    * **Quick Play:** Queues for both Modern and NES modes to find random opponents.  
    * **Private Lobbies:** Host and Join system using unique Room IDs for private matches.  
  * **Synchronization:** Deterministic gameplay via shared random seeds sent by the server at match start.

### **⚙️ Technical Highlights**

* **Custom UI Engine:** Built entirely in Java Swing with custom painting (no external game engines). Features custom components like ModernButton and ModernScrollBar.  
* **Input Handling:** Supports DAS (Delayed Auto Shift) and ARR (Auto Repeat Rate) for responsive controls.  
* **Persistence:** Key bindings are saved and loaded via XML configuration (keybindings.xml) with DTD validation.  
* **Thread Safety:** Robust threading model separating the Event Dispatch Thread (UI), Game Loops (TimerTasks), and Network I/O (Reader/Writer threads).

## **🛠️ Project Structure**

The project is organized into three main packages:

* **client**: Handles the User Interface (Swing), Input processing, and client-side logic.  
  * keyMaps: Key binding logic and XML serialization.  
  * userInterface: Panels (Game, Menu, Settings), Dialogs, and Rendering.  
* **server**: Manages connections, matchmaking, and data relay.  
  * gameModeHandlers: Logic for Queues (QuickPlayHandler) and Lobbies (LobbiesHandler).  
  * playerHandlers: Threads for bidirectional socket communication (GameCommunicationHandler, PlayerCommunicationHandler).  
* **tetris**: The core game engine (View-Model hybrid).  
  * boards: Logic for the grid, scoring, and state. Subclasses for SenderBoard and ReceiverBoard handle network synchronization.  
  * physics: Gravity, Rotation Systems (SRS/NES), and Input logic.  
  * tetrominoes: Definitions of shapes, colors, and generators.

## **🚀 Getting Started**

### **Prerequisites**

* Java Development Kit (JDK) 17 or higher.  
* Maven or Gradle (optional, if building via CLI).

### **Running the Application**

1. Start the Server:  
   Run the server.Server class. By default, it listens on port 7777\.  
   java server.Server \[port\]

2. Start the Client:  
   Run the client.Main class.  
   \# Connects to localhost:7777 by default  
   java client.Main

   \# Or specify IP and Port explicitly  
   java client.Main 127.0.0.1 7777

## **⌨️ Controls**

Controls are fully customizable in the **Settings** menu. The default configuration is:

### **Player 1 (Local & Online)**

| Action | Key |
| :---- | :---- |
| **Move Left** | A |
| **Move Right** | D |
| **Soft Drop** | S |
| **Hard Drop** | W |
| **Rotate Left** | J |
| **Rotate Right** | K |
| **Flip (180°)** | L |
| **Hold Piece** | Shift |
| **Menu / Back** | Enter |

### **Player 2 (Local Multiplayer)**

| Action | Key |
| :---- | :---- |
| **Move Left** | Left Arrow |
| **Move Right** | Right Arrow |
| **Soft Drop** | Down Arrow |
| **Hard Drop** | Up Arrow |
| **Rotate Left** | Numpad 1 |
| **Rotate Right** | Numpad 2 |
| **Flip (180°)** | Numpad 3 |
| **Hold Piece** | Control |

## **📡 Network Protocol**

The game uses a custom byte-based protocol over TCP sockets (DataInputStream/DataOutputStream):

1. **Handshake:** Client sends Game Mode ID \-\> Server routes to Queue or Lobby.  
2. **Initialization:** Server generates a random long seed and broadcasts it to both players.  
3. **Gameplay Loop (Packet Structure):**  
   * **Movement Updates:** \[BYTE: UPDATE\_FALLING\_TETROMINO\] \[X\] \[Y\] \[Rotation\] \[LockedStatus\]  
   * **Garbage Attacks:** \[BYTE: SEND\_GARBAGE\_ROWS\] \[LineCount\] \[HoleIndex\] (Modern Mode only)  
   * **State Signals:** \[BYTE: UPDATE\_TETROMINO\_HOLDER\] (No payload)

## **🎨 Visual Style**

The application features a modern "Dark Mode" aesthetic with:

* Rounded buttons with hover effects.  
* Custom scrollbars tailored for the dark theme.  
* Semi-transparent overlays for "Game Over" and "Waiting" screens.  
* Beveled block rendering (TetrominoCell.drawClassicCell) for a classic 3D look.

## **📄 License**

This project is for educational purposes as part of a Distributed Systems course.
