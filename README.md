# Tetris Distribuidos

**Tetris Distribuidos** is a robust, Java-based Tetris implementation featuring a custom Swing UI, distinct game modes (Modern vs. Classic NES), and full multiplayer capabilities (Local & Online).

[cite_start]The project demonstrates a distributed system architecture using TCP sockets for online matchmaking and real-time gameplay synchronization[cite: 3].

-----

## 🎮 Features

### Game Modes

  * [cite_start]**Modern Tetris**[cite: 3]:
      * **Mechanics:** Super Rotation System (SRS), Hard Drop, Hold Piece, 7-Bag Random Generator.
      * **Garbage System:** Competitive play where clearing lines sends garbage rows to the opponent.
  * [cite_start]**Classic (NES) Tetris**[cite: 3]:
      * **Mechanics:** Nintendo Rotation System (no wall kicks), Classic gravity curve, NES Random Generator, no Hold piece.
      * **Goal:** Score-based competition.

### Multiplayer Types

  * [cite_start]**Local Multiplayer:** Play 1v1 on the same keyboard/screen[cite: 3].
  * [cite_start]**Online Multiplayer**[cite: 3]:
      * **Quick Match:** Automatically pair with a random opponent.
      * **Private Lobby:** Host a game and share the Room ID with a friend.
      * [cite_start]**Join Lobby:** Connect to a specific room via ID[cite: 4].

### Technical Highlights

  * [cite_start]**Custom UI:** A dark-themed, specialized Swing interface with custom components (ModernButton, ModernScrollBar)[cite: 4].
  * [cite_start]**Input System:** Configurable key bindings saved to XML with `KeyInputHandler` supporting DAS (Delayed Auto Shift) and ARR (Automatic Repeat Rate)[cite: 4].
  * [cite_start]**Physics Engine:** Accurate implementation of gravity, lock delays, and rotation rules (SRS & NES)[cite: 4].
  * [cite_start]**Networking:** Multithreaded server handling matchmaking queues and relaying game state between clients[cite: 4].

-----

## 🚀 Getting Started

### Prerequisites

  * [cite_start]Java Development Kit (JDK) **17 or higher**[cite: 4].
  * [cite_start]Maven or Gradle (optional, if building from source)[cite: 4, 5].

### Running the Application

[cite_start]This project consists of two main entry points: the **Server** (for online play) and the **Client** (the game itself)[cite: 5].

#### 1\. Start the Server

[cite_start]If you want to play online, the server must be running first[cite: 5].

```bash
# Compile and run the Server class
java server.Server
```

> [cite_start]**Note:** The server listens on port **7777** by default[cite: 6].

#### 2\. Start the Client

Run the main client application. [cite_start]You can optionally pass the Server IP and Port as arguments[cite: 6].

```bash
# Run the Client (defaults to localhost:7777)
java client.Main

# Or specify a remote server IP
java client.Main 192.168.1.50 7777
```

-----

## 🕹️ Controls

[cite_start]Default controls can be customized in the **Settings** menu[cite: 6].

| Action | Player 1 (Default) | Player 2 (Local Multiplayer) |
| :--- | :---: | :---: |
| **Move Left** | `A` | `Left Arrow` |
| **Move Right** | `D` | `Right Arrow` |
| **Soft Drop** | `S` | `Down Arrow` |
| **Hard Drop** | `W` | `Up Arrow` |
| **Rotate Left** | `J` | `Numpad 1` |
| **Rotate Right** | `K` | `Numpad 2` |
| **Flip (180°)** | `L` | `Numpad 3` |
| **Hold Piece** | `Shift` | `Ctrl` |

> **Global Controls:**
> [cite_start]\* **Back to Menu:** `Enter` [cite: 7]

-----

## 🏗️ Architecture

### Package Structure

  * [cite_start]**`client`**: Contains the Swing GUI (`MainFrame`, `MainPanel`), input handling, and the client-side logic for connecting to the server[cite: 7].
  * [cite_start]**`server`**: Contains the Server socket listener, `MatchmakingHandler` (queues and lobbies), and `GameCommunicationHandler` (relays input between players)[cite: 7].
  * **`tetris`**: The core game library.
      * [cite_start]**`boards`**: Logic for the game grid, including `BoardWithPhysics` (local simulation) and `ReceiverBoard` (network replication)[cite: 7].
      * [cite_start]**`tetrominoes`**: Definitions for the 7 pieces (I, J, L, O, S, T, Z) and generation logic[cite: 7].
      * [cite_start]**`physics`**: Gravity calculations, rotation systems (SRS/NES), and garbage handling[cite: 8].

### Networking Protocol

[cite_start]The game uses a custom byte-based protocol for efficiency[cite: 8]:

  * [cite_start]**Handshake:** Client sends `ConnectionMode` (Quick Play, Host, etc.)[cite: 8].
  * [cite_start]**Game Start:** Server sends a synchronized random `seed` (long) to both clients[cite: 8].
  * **Gameplay Loop:**
      * [cite_start]**Movement:** `[X, Y, Rotation, LockedStatus]` is broadcasted to the opponent[cite: 8].
      * [cite_start]**Attacks:** `[Lines, HoleIndex]` sent when clearing lines in Modern mode[cite: 8].

-----

## ⚙️ Customization

Key bindings are stored in `src/main/resources/config/keybindings.xml`. [cite_start]If this file is missing, the application generates it with default values on startup[cite: 8].

-----
