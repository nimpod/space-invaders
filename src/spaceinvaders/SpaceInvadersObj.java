package spaceinvaders;

import spaceinvaders.core.Obj;

public class SpaceInvadersObj extends Obj<SpaceInvadersGame> {

    protected int instructionPointer;
    protected long waitTime;
    protected static int gameStyle = 2;

    public SpaceInvadersObj(SpaceInvadersGame game) {
        super(game);
    }

    @Override
    public void update() {
        switch (game.state) {
            case INITIALIZING: updateInitializing(); break;
            case LOADING_SCREEN: updateLoadingScreen(); break;
            case TITLE: updateTitle(); break;
            case READY: updateReady(); break;
            case PLAYING: updatePlaying(); break;
            case HIT: updateHit(); break;
            case CLEAR: updateClear(); break;
            case GAME_OVER: updateGameOver(); break;
        }
    }

    public void updateInitializing() {}
    public void updateLoadingScreen() {}
    public void updateTitle() {}
    public void updateReady() {}
    public void updatePlaying() {}
    public void updateHit() {}    
    public void updateClear() {}
    public void updateGameOver() {}    
    public void stateChanged() {}

    public static int getGameStyle() {
    	return gameStyle;
    }

}