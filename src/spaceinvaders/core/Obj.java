package spaceinvaders.core;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public abstract class Obj<T extends Game> {

    public T game;
    public boolean visible;
    public double x, y;
    public Rectangle2D collider;
    public BufferedImage frame;
    public BufferedImage[] frames;

    public Obj(T game) {
        this.game = game;
    }

    public void init() {
    }

    public void update() {
    }

    public void draw(Graphics2D g) {
        if (!visible)
            return;

        if (frame != null)
            g.drawImage(frame, (int) x, (int) y, null);

        if (collider != null) {
            collider.setRect(x, y, collider.getWidth(), collider.getHeight());
            // g.draw(collider);
        }
    }

    public void updateCollider() {
        if (collider != null)
            collider.setRect(x, y, collider.getWidth(), collider.getHeight());
    }

    public void loadFrames(String ... res) {
        frames = new BufferedImage[res.length];
        for (int i = 0; i < frames.length; i++) {
            try {             	
                frames[i] = ImageIO.read(getClass().getResourceAsStream("/res/" + res[i]));
            } catch (IOException ex) {
                Logger.getLogger(Obj.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
        }
        frame = frames[0];
    }
}