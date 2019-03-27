package spaceinvaders.obj;


import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.SpaceInvadersObj;
import spaceinvaders.core.Obj;

public class Shield extends SpaceInvadersObj {

    private int id;
    private int col, row;
    public boolean hit;
    private Rectangle2D colliderBackup;
    private List<Shield> neighborShields = new ArrayList<Shield>();
    private boolean usingCentralBlock;

    public Shield(SpaceInvadersGame game, int id, int col, int row) {
        super(game);
        this.id = id;
        this.col = col;
        this.row = row;
    }

    @Override
    public void init() {
        x = 40 + id * 80 + 14 * col;
        y = 250 + 8 * row;
        colliderBackup = collider = new Rectangle2D.Double(x, y, 14, 8);        
        Point[] ps = new Point[] { 
        		new Point(-1, 0), 
        		new Point(1, 0), 
        		new Point(0, -1), 
        		new Point(0, 1)
        };
        for (Point p : ps) {
            int sx = p.x;
            int sy = p.y;
                int nx = col + sx;
                int ny = row + sy;
                if (nx >= 0 && nx <= 2 && ny >= 0 && ny <= 3 && !(sx == 0 && sy == 0) && !(nx == 1 && ny == 3)) {
                    for (Obj obj : game.objs) {
                        if (obj instanceof Shield) {
                            Shield shield = (Shield) obj;
                            if (shield.id == id && shield.col == nx && shield.row == ny) {
                                neighborShields.add(shield);
                            }
                        }
                    }
                }
        }

        int c = col;
        int r = row;
        if (row == 1 || row == 2) {
            c = 1;
            r = 0;
        }
        if (c == 1 && r == 0)
            usingCentralBlock = true;

        if (usingCentralBlock) {
            loadFrames("shield_" + c + "_" + r + ".png"
                    , "shield_" + c + "_" + r + "_destroyed_0.png"
                    , "shield_" + c + "_" + r + "_destroyed_1.png"
                    , "shield_" + c + "_" + r + "_destroyed_2.png");
        }
        else {
            loadFrames("shield_" + c + "_" + r + ".png", "shield_" + c + "_" + r + "_destroyed.png");
        }
    }

    private boolean allNeighborsShieldHit() {
        for (Shield shield : neighborShields)
            if (!shield.hit)
                return false;
        return true;
    }

    @Override
    public void updatePlaying() {
        if (!visible)
            return;
        if (hit && allNeighborsShieldHit())
            visible = false;
    }

    @Override
    public void stateChanged() {
        if (game.state == SpaceInvadersGame.State.READY) {
        	visible = true;
            hit = false;
            frame = frames[0];
            collider = colliderBackup;
        }
        if (game.state == SpaceInvadersGame.State.GAME_OVER) {
            visible = false;
        }
    }   

    public void hit() {
        hit = true;
        frame = frames[1];
        if (usingCentralBlock)
            frame = frames[1 + (int) (3 * Math.random())];
        collider = null;
    }

}