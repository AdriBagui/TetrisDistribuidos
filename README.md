# **Tetris Project Documentation**

## **1\. Project Overview**

This is a clone of Tetris built using **Java Swing** for the UI. It features:

* **Single/Local Multiplayer:** Two boards rendered side-by-side.  
* **Online Multiplayer:** A Host/Client TCP architecture.  
* **Mechanics:** Implements the Super Rotation System (SRS) and NES-style gravity/scoring.

## **2\. Architecture & Package Structure**

The project is divided into logical layers separating the View (UI) from the Model (Logic/Physics).

### **src (Root)**

* **Main.java**: The entry point. It initializes the MainFrame on the Swing Event Dispatch Thread (EDT).  
* **MainFrame.java**: The top-level JFrame window.  
* **MainPanel.java**: Acts as the central controller using a CardLayout. It switches between the Menu, Local Game, and Online Game panels. It also handles the initial TCP ServerSocket and Socket connection setup.

### **menus**

* **StartMenuPanel.java**: Simple UI for selecting game modes (Local, Host, Client).

### **tetris**

* **Config.java**: A centralized configuration file containing constants for:  
  * **Facts:** Tetromino IDs.  
  * **Game Loop:** FPS (62.5), Frame timing.  
  * **Dimensions:** Board rows/cols, cell sizes.  
  * **Physics:** Gravity calculations, DAS (Delayed Auto Shift), ARR (Auto Repeat Rate).

### **tetris.boards (The Data Model)**

* **Board.java (Abstract)**: Represents the game state (the grid, score, level, current piece). It handles the drawing of the grid and UI overlays.  
* **ClassicBoard.java**: Extends Board to add standard Tetris logic (shadows, specific garbage calculation, key input handling).  
* **LocalBoard.java**: Extends ClassicBoard. Used by the local player. It **sends** game state updates to the output stream (for online play).  
* **OnlineBoard.java**: Extends Board. Used to represent the *opponent*. It **receives** state updates from an input stream.  
* **TetrominoesQueue.java**: Manages the "Next Piece" preview using a generator.  
* **TetrominoHolder.java**: Manages the "Hold" mechanic.

### **tetris.panels (The View/Controller)**

* **TwoPlayerTetrisPanel.java**: Base class for rendering two boards. Contains the **Game Loop** (java.util.Timer).  
* **LocalTwoPlayerTetrisPanel.java**: Handles input for two players on the same keyboard (WASD vs Arrow Keys).  
* **OnlineTwoPlayerTetrisPanel.java**: Handles input for one local player and updates the second board based on network data.

### **tetris.boards.physics (The Logic Engine)**

* **BoardPhysics.java**: Abstract base for movement logic.  
* **LocalBoardPhysics.java**: Calculates movement, collision, and gravity for the player currently controlling the keyboard.  
* **OnlineBoardPhysics.java**: Instead of calculating physics, this class **decodes** the network stream to update the opponent's piece position blindly.  
* **CollisionDetector.java**: Static utility to check if a piece overlaps with the grid or boundaries.  
* **Gravity.java**: Handles the falling speed, "Soft Drop", and "Hard Drop" calculations.  
* **InputMovement.java**: Handles the complex DAS/ARR (key repeat) logic for smooth movement.

### **tetris.tetrominoes**

* **Tetromino.java**: Abstract definition of a piece (shape, color, rotation state, coordinates).  
* **TetrominoCell.java**: Static helper to draw individual blocks (squares) using Graphics2D.  
* **TetrominoFactory.java**: Factory pattern to generate pieces based on IDs.  
* **generators/**: Contains RandomBagTetrominoesGenerator (standard 7-bag randomization) and CompletelyRandom....

## **3\. Network Protocol (Custom TCP)**

The application uses a custom byte-based protocol to synchronize state between the Host and Client.

### **Communication Flow**

1. **Handshake:** The Host generates a seed (long) and sends it to the Client so both random number generators produce the same sequence of pieces.  
2. **Game Loop:** The LocalBoard sends bytes, and the OnlineBoard reads bytes.

### **Protocol Codes**

The protocol uses a header byte to determine the action, followed by data bytes.

| Header Byte | Action | Payload (Following Bytes) | Description |
| :---- | :---- | :---- | :---- |
| **1** | **MOVE** | \[X, Y, Rotation, Locked\] | Updates the opponent's falling piece position. X/Y: Coordinates (0-255). Rot: Rotation Index (0-3). Locked: 1 if piece locked, 0 otherwise. |
| **2** | **HOLD** | *None* | Opponent swapped the held piece. |
| **3** | **GARBAGE** | \[Lines, EmptyCol\] | Opponent cleared lines and sent garbage. Lines: Count. EmptyCol: The hole in the garbage line. |

## **4\. Key Mechanics Implementation**

* **Gravity:** Calculated as "Cells per Frame". It accumulates a decimal decimalY until it exceeds 1.0, triggering a drop.  
* **Rotation (SRS+):** The SuperRotationSystemPlus class defines "Kick Data". If a rotation results in a collision, the system tries offset positions (kicks) to find a valid spot.  
* **Garbage:** Standard "cheese" garbage. When lines are cleared, garbage is sent to the enemyBoard. The garbage rises from the bottom with a hole in a specific column.
