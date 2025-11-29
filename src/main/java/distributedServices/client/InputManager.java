package distributedServices.client;

import tetris.MainFrame;
import tetris.tetrominoes.Tetromino;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static tetris.Config.MILIS_PER_FRAME;

public class InputManager extends Thread{
    InputStream inputStream;
    MainFrame mainFrame;

    public InputManager(InputStream inputStream, MainFrame mainFrame){
        this.inputStream = inputStream;
        this.mainFrame = mainFrame;
    }

    @Override
    public void run(){
        int x, y, rot;
        try{
            while(!mainFrame.isGameOver()){
                x = inputStream.read();
                y = inputStream.read();
                rot = inputStream.read();
                mainFrame.setTetrominoXYRotationIndex(x,y,rot);
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
