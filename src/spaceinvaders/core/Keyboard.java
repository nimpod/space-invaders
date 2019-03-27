package spaceinvaders.core;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Keyboard extends KeyAdapter {

    public static boolean[] keyPressed = new boolean[256];
    public static boolean[] keyPressedConsumed = new boolean[256];

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed[e.getKeyCode()] = true;
        keyPressedConsumed[e.getKeyCode()] = false;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed[e.getKeyCode()] = false;
    }

}