package spaceinvaders.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.SpaceInvadersObj;

public class LoadingScreen extends SpaceInvadersObj {

    private BufferedImage image;
    private int currentLine = 1;

    public LoadingScreen(SpaceInvadersGame game) {
        super(game);
    }

    @Override
    public void updateLoadingScreen() {
        image = new BufferedImage(300, 22, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.BLACK);
        g.scale(0.9, 0.9);
        g.fillRect(0, 0, 1000, 1000);
        game.drawText(g, "Steele Studios presents...", 45, 6);

        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 2) {
                        break yield;
                    }
                    if (currentLine >= 0) {
                        currentLine--;
                        instructionPointer = 0;
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 2;
                case 2:
                    if (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    visible = false;
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 3;
                case 3:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    game.setState(SpaceInvadersGame.State.TITLE);
                    break yield;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible) {
            return;
        }
        g.drawImage(image, 48, 140, 262, 22, null);
        g.drawImage(image, 48, 0, 310, 140 + currentLine, 0, currentLine, 262, currentLine + 1, null);
    }


    // broadcast messages

    @Override
    public void stateChanged() {
        visible = game.state == SpaceInvadersGame.State.LOADING_SCREEN;
        System.out.println(game.state);
    }


}