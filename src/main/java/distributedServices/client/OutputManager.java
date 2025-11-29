package distributedServices.client;

import tetris.GamePanel;
import tetris.MainFrame;
import tetris.tetrominoes.Tetromino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static tetris.Config.MILIS_PER_FRAME;

public class OutputManager extends Thread{
    OutputStream outputStream;
    MainFrame mainFrame;

    public OutputManager(OutputStream outputStream, MainFrame mainFrame){
        this.outputStream = outputStream;
        this.mainFrame = mainFrame;
    }

    @Override
    public void run() {
        try{
            while(!mainFrame.isGameOver()){
                Tetromino tetromino = mainFrame.getFallingTetromino();
                outputStream.write(tetromino.getX());
                outputStream.write(tetromino.getY());
                outputStream.write(tetromino.getRotationIndex());
                outputStream.flush();
                Thread.sleep(MILIS_PER_FRAME);
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
