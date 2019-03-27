package spaceinvaders.core;

import java.awt.Color;
import java.awt.Graphics2D;
import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.SpaceInvadersObj;

public class HUD extends SpaceInvadersObj {

    public HUD(SpaceInvadersGame game) {
        super(game);
    }

    @Override
    public void init() {
        loadFrames("ship_lives.png");    
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible)
            return;
        game.drawText(g, "SCORE: " + game.getScore(), 10, 5);
        game.drawText(g, "HI-SCORE: " + game.getHiscore(), 180, 5);
        game.drawText(g, "LIVES: ", 10, 315);
        game.drawText(g, "LEVEL: " + game.getLevel(), 265, 315);
        for (int lives = 0; lives < game.lives; lives++)
            g.drawImage(frame, 75 + 20 * lives, 316, null);
        g.setColor(Color.GREEN);
        g.fillRect(10, 310, 340, 2);
    }

    // broadcast messages

    @Override
    public void stateChanged() {
        visible = (game.state != SpaceInvadersGame.State.INITIALIZING)
                && (game.state != SpaceInvadersGame.State.LOADING_SCREEN);
    }

}