package spaceinvaders.obj;

import java.awt.geom.Rectangle2D;

import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.SpaceInvadersObj;
import spaceinvaders.core.ImageManager;
import spaceinvaders.core.Sound;
import spaceinvaders.core.SoundManager;

public class Alien extends SpaceInvadersObj {

    public AlienShot alienShot;

    private int type;

	private int col, row;
    private double vx;
    private boolean enemyDown;

    public int hitPoint;
    public boolean hit;
    private long hitTime;
    private long startFrameTime = (long) (999999999 * Math.random());

    private Sound invaderKilled;

    public Alien(SpaceInvadersGame game, int col, int row, int type, AlienShot alienShot, int hitPoint) {
        super(game);
        this.col = col;
        this.row = row;
        this.type = type;
        this.alienShot = alienShot;
        this.hitPoint = hitPoint;
    }

    @Override
    public void init() {
        collider = new Rectangle2D.Double(x, y, 24, 24);
        loadFrames(
        		"alienStyle" + getGameStyle() + "/alien" + type + "_0.png", 
        		"alienStyle" + getGameStyle() + "/alien" + type + "_1.png", 
        		ImageManager.alienDestroyed
        );
        invaderKilled = SoundManager.invaderKilled;

    	reset();
    }

    @Override
    public void updateReady() {
        if (System.currentTimeMillis() - game.startGameTime > (col + 11 * row) * 50)
            visible = true;
        if (visible && row == 5 && col == 11)
            game.setState(SpaceInvadersGame.State.PLAYING);
    }    

    @Override
    public void updatePlaying() {    	
        if (!visible)
            return;
        if (hit) {
            frame = frames[2];
            if (System.currentTimeMillis() - hitTime > 100) {
                visible = false;
                if (game.enemiesCount == 0)
                    game.gameCleared();
            }
            return;
        }

        if (y > 250)
            game.setState(SpaceInvadersGame.State.GAME_OVER);

        // update vx
        int s = vx > 0 ? 1 : -1;
        switch (game.enemiesCount) {
            case 4: vx = s * 0.5; break;
            case 3: vx = s * 1; break;
            case 2: vx = s * 2; break;
            case 1: vx = s * 4; break;
            default: vx = s * 0.25; break;
        }        
        frame = frames[(int) ((startFrameTime + System.nanoTime()) * Math.abs(vx) * 0.00000001) % 2];        
        x += vx;
        if ((vx < 0 && x < 10) || (vx > 0 && x > 326))
            game.broadcastMessage("downEnemy");        
        if (alienShot.canShoot() && Math.random() < game.enemiesShootingProbability)
            alienShot.shoot(x + 15, y);
    }

    // broadcast messages

    @Override
    public void stateChanged() {
        if (game.state == SpaceInvadersGame.State.READY)
            reset();
        else if (game.state == SpaceInvadersGame.State.GAME_OVER || game.state == SpaceInvadersGame.State.CLEAR)
            visible = false;
    }    

    public void reset() {
        x = 27 * col;
        y = 15 + 24 * row;
        hit = false;
        frame = frames[0];
    }

    public void downEnemy() {
        enemyDown = true;
    }

    public void hit() {
        hit = true;
        invaderKilled.play();
        hitTime = System.currentTimeMillis();        
        game.addScore(hitPoint);
        game.enemiesCount--;        
    }

    public void frameEnd() {
        if (enemyDown) {
            enemyDown = false;
            vx = -1 * vx;
            y += 12;
        }
    }

} 