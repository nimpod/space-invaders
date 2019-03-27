package spaceinvaders.obj;

import java.awt.geom.Rectangle2D;
import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.SpaceInvadersObj;

public class AlienShot extends SpaceInvadersObj {

    public boolean hit;
    public long hitTime;

    public AlienShot(SpaceInvadersGame game) {
        super(game);
    }

    @Override
    public void init() {
        x = 0;
        y = 0;
        collider = new Rectangle2D.Double(x, y, 6, 10);
        loadFrames("alien_shot_0.png", "alien_shot_1.png", "alien_shot_destroyed.png");
    }

    @Override
    public void updatePlaying() {
        if (!visible) {
            return;
        }
        if (hit) {
            frame = frames[2];
            if (System.currentTimeMillis() - hitTime > 100)
                visible = false;
            return;
        }
        frame = frames[(int) (System.nanoTime() * 0.0000001) % 2];
        y += 3;
        Shield shield =  game.checkCollision(this, Shield.class);
        if (shield != null && !shield.hit) {
            shield.hit();
            visible = false;
            return;
        }

        Ship ship =  game.checkCollision(this, Ship.class);
        if (ship != null && !ship.hit) {
            //ship.hit();
            visible = false;
            game.setState(SpaceInvadersGame.State.HIT);
            return;
        }

        if (y > 300)
            hit();
    }

    public Boolean canShoot() {
        return !visible;
    }

    public void shoot(Double x, Double y) {
        this.x = x;
        this.y = y;
        visible = true;
        hit = false;
    }

    // broadcast messages

    @Override
    public void stateChanged() {
        if (game.state != SpaceInvadersGame.State.PLAYING) {
            visible = false;
        }
    }

    public void hit() {
        hit = true;
        hitTime = System.currentTimeMillis();
    }

} 