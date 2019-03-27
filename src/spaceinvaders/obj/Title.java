package spaceinvaders.obj;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.SpaceInvadersObj;
import spaceinvaders.core.Keyboard;
import spaceinvaders.core.Sound;
import spaceinvaders.core.SoundManager;

public class Title extends SpaceInvadersObj {

    private BufferedImage image;
    private boolean pushSpaceToStartVisible;

    private Sound titleMusic;

    public Title(SpaceInvadersGame game) {
        super(game);
    }

    @Override
    public void init() {
        image = new BufferedImage(282, 22, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        //g.setColor(Color.RED);
        //g.fillRect(0, 0, 282, 22);
        g.scale(1.9, 1.9);
        game.drawText(g, "SLANCE INVADERS", 0, 0);
        titleMusic = SoundManager.titleMusic;
        if (game.getState() == SpaceInvadersGame.State.TITLE || !visible)
        	titleMusic.loop();
    }

    @Override
    public void updateTitle() {
        yield:
        while (true) {        	
            switch (instructionPointer) {
                case 0:
                    pushSpaceToStartVisible = ((int) (System.nanoTime() * 0.00000001) % 2) == 0;
                    if (Keyboard.keyPressed[KeyEvent.VK_SPACE]) {
                        game.startGame();
                        titleMusic.stop();
                    }
                    break yield;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible)
            return;
        g.drawImage(image, 40, 120, 282, 22, null);
        if (pushSpaceToStartVisible)
            game.drawText(g, "PUSH SPACE TO START", 83, 170);
    }

    @Override
    public void stateChanged() {
        visible = false;
        if (game.state == SpaceInvadersGame.State.TITLE) {
            game.lives = 3;
            visible = true;
            titleMusic.loop();
        }
    }


} 