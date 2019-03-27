package spaceinvaders.obj;

import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.SpaceInvadersObj;

public class Initialiser extends SpaceInvadersObj {

    public Initialiser(SpaceInvadersGame game) {
        super(game);
    }

    @Override
    public void init() {
    }

    @Override
    public void updateInitializing() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    // game.setState(State.TITLE);
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    game.setState(SpaceInvadersGame.State.LOADING_SCREEN);
                    break yield;
            }
        }
    }

    // broadcast messages

    @Override
    public void stateChanged() {
    }

}