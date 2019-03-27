package spaceinvaders.core;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import spaceinvaders.core.BitmapFontRenderer;

public abstract class Game {

    @SuppressWarnings("rawtypes")
	public List<Obj> objs = new ArrayList<Obj>();
    public BitmapFontRenderer bitmapFontRenderer = new BitmapFontRenderer("/res/font10x10.png", 16, 16);


    public void start() {
        createAllObjs();
        initAllObjs();
    }

    public abstract Dimension getScreenSize();
    public abstract Point2D getScreenScale();
    public abstract void createAllObjs();

    public void initAllObjs() {
        for (Obj obj: objs) {
            obj.init();
        }
    }

    public void update() {
        for (Obj obj: objs) {
            obj.update();
        }

    }

    public void draw(Graphics2D g) {
        for (Obj obj: objs) {
            obj.draw(g);
        }
    }

    public <T> T checkCollision(Obj o1, Class<T> type) {
        o1.updateCollider();
        for(Obj o2 : objs) {
            o2.updateCollider();
            if (o1 != o2 
                && type.isInstance(o2)
                && o1.collider != null && o2.collider != null
                && o1.visible && o2.visible
                && o2.collider.intersects(o1.collider)) {
                    return type.cast(o2);
            }
        }
        return null;
    }

    public void broadcastMessage(String message) {
        for (Obj obj : objs) {
            try {
                Method method = obj.getClass().getMethod(message);
                if (method != null) {
                    method.invoke(obj);
                }
            } catch (Exception ex) {
            }
        }
    }

    public void drawText(Graphics2D g, String text, int x, int y) {
        bitmapFontRenderer.drawText(g, text, x, y);
    }

} 