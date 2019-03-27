package spaceinvaders.obj;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.SpaceInvadersObj;
import spaceinvaders.core.ImageManager;
import spaceinvaders.core.Keyboard;
import spaceinvaders.core.Sound;
import spaceinvaders.core.SoundManager;

public class Ship extends SpaceInvadersObj {

    public ShipShot shipShot;
    public boolean hit;
    public long hitTime;

    private Sound explosionSound;
    private Sound hahaSound;

    public Ship(SpaceInvadersGame game, ShipShot shipShot) {
        super(game);
        this.shipShot = shipShot;
    }

    @Override
    public void init() {
        x = 360 / 2 - 13;
        y = 290;
        collider = new Rectangle2D.Double(x, y, 26, 16);

        loadFrames(ImageManager.ship, ImageManager.shipDestroyed1, ImageManager.shipDestroyed2);

        explosionSound = SoundManager.shipExplosion;
        hahaSound = SoundManager.shipHaha;
    }

    public static boolean paused = false;

    @Override
    public void updatePlaying() {
        if (!visible)
            return;

        // Player movement
        if (Keyboard.keyPressed[KeyEvent.VK_LEFT] || Keyboard.keyPressed[KeyEvent.VK_A])
            x -= 4;
        else if (Keyboard.keyPressed[KeyEvent.VK_RIGHT] || Keyboard.keyPressed[KeyEvent.VK_D])
            x += 4;

        // Pause game
        else if (Keyboard.keyPressed[KeyEvent.VK_P] && !paused) {
        	paused = true;
        	try {
        		while (paused) {
        			Thread.sleep(100);
        			if (Keyboard.keyPressed[KeyEvent.VK_P] && paused == true) {
        	        	paused = false;
        	        }
        		}
        	} catch(InterruptedException e) {
        		e.printStackTrace();
        	}
        }

        // Player tries shooting
        if (Keyboard.keyPressed[KeyEvent.VK_SPACE] && shipShot.canShoot())
            shipShot.shoot(x + 10, y);

        // limit x movement        
        x = x < 10 ? 10 : x;
        x = x > 324 ? 324 : x;

    }

    @Override
    public void updateClear() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1500)
                        break yield;
                    game.nextGame();
                    break yield;
            }
        }
    }

    @Override
    public void updateHit() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                    explosionSound.play();
                    hahaSound.play();

                case 1:
                    frame = frames[1 + ((int) (System.nanoTime() * 0.00000001) % 2)];
                    if (System.currentTimeMillis() - waitTime < 1000)
                        break yield;
                    visible = false;
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 2;                	

                case 2:
                    if (System.currentTimeMillis() - waitTime < 1500)
                        break yield;
                    frame = frames[0];
                    game.lives--;

                    if (game.lives == 0)
                        game.setState(SpaceInvadersGame.State.GAME_OVER);
                    else {
                        game.setState(SpaceInvadersGame.State.PLAYING);
                        visible = true;
                    }
                    break yield;
            }
        }
    }

    // broadcast messages    
    @Override
    public void stateChanged() {
        if (game.state == SpaceInvadersGame.State.TITLE)
            x = 360 / 2 - 13;
        else if (game.state == SpaceInvadersGame.State.READY)
            visible = true;
        else if (game.state == SpaceInvadersGame.State.HIT || game.state == SpaceInvadersGame.State.CLEAR)
            instructionPointer = 0;
        else if (game.state == SpaceInvadersGame.State.GAME_OVER)
            visible = false;
    }

    public void hit() {
        hit = true;
        hitTime = System.currentTimeMillis();
    }

}
