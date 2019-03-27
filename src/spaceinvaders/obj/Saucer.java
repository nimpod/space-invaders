package spaceinvaders.obj;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.SpaceInvadersObj;
import spaceinvaders.core.ImageManager;
import spaceinvaders.core.Sound;
import spaceinvaders.core.SoundManager;

public class Saucer extends SpaceInvadersObj {

    private double vx;

    private boolean showScore;
    private long showStartTime;
    private long showTime;

    private boolean hit;
    private long hitTime;
    private long showScoreTime;

    private Sound saucerSound;

    public Saucer(SpaceInvadersGame game) {
        super(game);        
    }

    @Override
    public void init() {
        vx = 1;
        collider = new Rectangle2D.Double(x, y, 24, 12);
        loadSaucer();
        x = -50;
        reset();
        saucerSound = SoundManager.saucer;
    }

    public static int randSaucer() {
    	return new Random().nextInt(6)+1;
    }

    public void loadSaucer() {
    	String randSaucer = (getGameStyle() == 1) ? "" : Integer.toString(randSaucer());
        loadFrames(
        		"alienStyle" + getGameStyle() + "/saucer"+ randSaucer +".png",
        		ImageManager.saucerDestroyed1,
        		ImageManager.saucerDestroyed2
        );
    }

    @Override
    public void updatePlaying() {
        if (!visible && System.currentTimeMillis() - showStartTime > showTime) {
            visible = true;
            if (visible || game.state == SpaceInvadersGame.State.PLAYING && game.state != SpaceInvadersGame.State.TITLE)
            	saucerSound.loop();
        }
        if (!visible) {
        	if (!Ship.paused) 
        		saucerSound.stop();
            return;
        }

        if (hit) {
        	if (game.state != SpaceInvadersGame.State.PLAYING || !Ship.paused)
        		saucerSound.stop();

            if ((System.currentTimeMillis() - showScoreTime > 1000) && showScore) {
                reset();
                visible = false;
                if (vx < 0)
                    x = -50;
                else
                    x = 360;
                vx = vx * -1;
            }
            else if (!showScore && System.currentTimeMillis() - hitTime > 100) {
                showScore = true;
                showScoreTime = System.currentTimeMillis();
                frame = frames[2];
                game.addScore(200);
            }
            else if (!showScore)
                frame = frames[1];
            return;
        }        
        x += vx;
        if ((x > 360 && vx > 0) || (x < -50 && vx < 0) ) {
            vx = vx * -1;
            reset();
            visible = false;
            loadSaucer();
        }        
    }

    @Override
    public void stateChanged() {
        if (game.state != SpaceInvadersGame.State.READY) {
            vx = 1;
            x = -50;
            reset();
        }
        else if (game.state != SpaceInvadersGame.State.PLAYING && game.state != SpaceInvadersGame.State.HIT) {
            visible = false;
    	}
        if (!visible && game.state != SpaceInvadersGame.State.READY) {
        	if (!Ship.paused) {
        		saucerSound.stop();
        	}
        }
    }    

    public void reset() {
        y = 25;
        hit = false;
        //visible = true;
        frame = frames[0];
        showStartTime = System.currentTimeMillis();
        showTime = (int) (10000 + 10000 * Math.random());
        showScore = false;
    }

    public void hit() {
        hit = true;
        hitTime = System.currentTimeMillis();
    }

}