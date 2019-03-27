package spaceinvaders.obj;

import java.awt.Graphics2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import spaceinvaders.SpaceInvadersGame;
import spaceinvaders.SpaceInvadersObj;
import spaceinvaders.core.Sound;
import spaceinvaders.core.SoundManager;

public class GameOver extends SpaceInvadersObj {

    private int gameOverTextIndex;
    private List<String> scores = new ArrayList<>();
    private Map<Integer, String> mapOfScores = new TreeMap<>(Collections.reverseOrder());
    private String name;
    private int score;    

    private Sound first;
    private Sound top20;
    private Sound notTop20;

    public GameOver(SpaceInvadersGame game) {
        super(game);
    }

    @Override
    public void init() {
    	first = SoundManager.first;
    	top20 = SoundManager.top20;
    	notTop20 = SoundManager.notTop20;

        loopCreditMusic();
    }

    private String music;

    public void loopCreditMusic() {
    	highScoreTable();
        if (game.getState() == SpaceInvadersGame.State.GAME_OVER && game.getState() != SpaceInvadersGame.State.TITLE) {
        	if (game.score >= getNthScore(0)) {
        		first.loop(); 
        		music = "first";
        	}
        	else if (game.score >= getNthScore(19) && game.score <= getNthScore(1)) {
        		top20.loop();
        		music = "top20";
        	}
        	else if (game.score < getNthScore(19)) {
        		notTop20.loop();
        		music = "notTop20";
        	}
        }
    }

    public void stopCreditMusic(Sound music) {
    	highScoreTable();
    	music.stop();
    }    

    @Override
    public void updateGameOver() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    gameOverTextIndex = 0;
                    instructionPointer = 1;
                case 1:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 2;
                case 2:
                    if (System.currentTimeMillis() - waitTime < 200)
                        break yield;

                    if (gameOverTextIndex < 9) {
                        gameOverTextIndex++;
                        instructionPointer = 1;
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 3;
                case 3:
                    if (System.currentTimeMillis() - waitTime < 3000)
                        break yield;

                    game.enterName();
                    game.updateHiscore();
                    game.clearScore();

                   	if (music.equals("first")) 
                   		stopCreditMusic(first);
                   	else if (music.equals("top20"))
                   		stopCreditMusic(top20);
                    else if (music.equals("notTop20")) 
                    	stopCreditMusic(notTop20);

                   	game.setLevel(1);
                    game.setState(SpaceInvadersGame.State.TITLE);
                    break yield;
            }
        }
    }

    public void highScoreTable() {
        scores = readFileInList();
        for (int i = 0; i < scores.size(); i++) {
        	String nextLine = scores.get(i);
        	String[] parts = nextLine.split(", ");
        	name = parts[0];
        	// System.out.println(i);
        	score = Integer.parseInt(parts[1]);
        	mapOfScores.put(score, name);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible)
            return;

        g.scale(2, 2);
        String gameOverText = "GAME OVER".substring(0, gameOverTextIndex);
        game.drawText(g, gameOverText, 44, 15);

        g.scale(1, 1);
        String scoreTable = "HIGH SCORES";
        game.drawText(g, scoreTable, 27, 34);

        g.scale(0.7, 0.7);
        g.scale(0.6, 0.6);        
        scoreTable = "RANK  SCORE  NAME";        
        highScoreTable();

        String nameInput = game.getNameInput();        
        int tableSize = 20;

        for (int i = 0; i < tableSize; i++)
        	scoreTable += String.format("\n%-5d %-6s %-20s", i+1, getNthScore(i), getNthName(i));

        game.drawText(g, scoreTable, 110, 125);        
    	mapOfScores.put(game.score, nameInput);
    	game.drawText(g, scoreTable, 110, 125);
    }

    // values (aka. the names)
    public String getNthName(int i) {
    	return (String) mapOfScores.values().toArray()[i];
    }

    // keys (aka. the scores)
    public int getNthScore(int i) {
    	return (int) mapOfScores.keySet().toArray()[i];
    }

	public List<String> readFileInList() {
		List<String> lines = Collections.emptyList();
	    try {
	      lines = Files.readAllLines(Paths.get("scores.txt"));
	    } catch (IOException e) { 
	    	e.printStackTrace();
	    }
	    return lines;
	}

    @Override
    public void stateChanged() {
        visible = game.state == SpaceInvadersGame.State.GAME_OVER;
        if (game.getState() == SpaceInvadersGame.State.GAME_OVER || game.getState() != SpaceInvadersGame.State.TITLE) 
        	loopCreditMusic();
        if (visible)
            instructionPointer = 0;
    }

}