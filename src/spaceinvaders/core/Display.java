package spaceinvaders.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;


public class Display extends JPanel {
	
	// I don't know why I need this, but Java complains if I don't...
	private static final long serialVersionUID = 1L;
	
	private Game game;
    private boolean running;

    public Display(Game game) {
        this.game = game;
        addKeyListener(new Keyboard());
        Point2D screenScale = game.getScreenScale();
        Dimension screenSize = game.getScreenSize();
        setPreferredSize(new Dimension((int) (screenSize.width * screenScale.getX()), (int) (screenSize.height * screenScale.getY())));
    }

    private class MainLoop implements Runnable {
        @Override
        public void run() {
            long frameRate = 1000 / 60;
            while (running) {
                long startTime = System.currentTimeMillis();
                update();
                repaint();
                game.broadcastMessage("frameEnd");
                while (System.currentTimeMillis() - startTime < frameRate) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                    }
                }                
            }
        }        
    }

    public void start() {
        if (running)
            return;
        running = true;
        game.start();
        Thread thread = new Thread(new MainLoop());
        thread.start();
    }

    public void update() {
        game.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.BLACK);
        g2d.scale(game.getScreenScale().getX(), game.getScreenScale().getY());
        g2d.clearRect(0, 0, game.getScreenSize().width, game.getScreenSize().height);
        game.draw(g2d);
    }

}