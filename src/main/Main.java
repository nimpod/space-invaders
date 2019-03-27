package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.core.Display;
import spaceinvaders.core.Game;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Game game = new SpaceInvadersGame();
                Display view = new Display(game);
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(view);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setTitle("SPACE INVADERS");
                view.requestFocus();
                view.start();
            }
        });
    }

} 