package spaceinvaders;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import spaceinvaders.core.Game;
import spaceinvaders.core.HUD;
import spaceinvaders.core.LoadingScreen;
import spaceinvaders.obj.Alien;
import spaceinvaders.obj.AlienShot;
import spaceinvaders.obj.GameOver;
import spaceinvaders.obj.Initialiser;
import spaceinvaders.obj.Saucer;
import spaceinvaders.obj.Shield;
import spaceinvaders.obj.Ship;
import spaceinvaders.obj.ShipShot;
import spaceinvaders.obj.Title;

public class SpaceInvadersGame extends Game {

    public static Dimension screenSize = new Dimension(360, 330);
    public static Point2D screenScale = new Point2D.Double(2, 2);

    public double enemiesShootingProbability;
    public int enemiesCount;
    public long startGameTime;
    public int lives;
    public int score;
    public int hiscore;
    public int level = 1;

    public static enum State {
    	INITIALIZING,
    	LOADING_SCREEN,
    	TITLE,
    	READY,
    	PLAYING,
    	HIT, 
    	CLEAR,
    	GAME_OVER
    }
    public State state = State.INITIALIZING;

    public SpaceInvadersGame() {
    	readFileInMap();
    	loadHiscore();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            broadcastMessage("stateChanged");
        }
    }

    private static List<String> scores = new ArrayList<>();
    private static Map<Integer, String> mapOfScores = new TreeMap<>(Collections.reverseOrder());
    private String nameInput = "";

    public void enterName() {
    	nameInput = "";
    	nameInput = JOptionPane.showInputDialog("Please Enter Your Name");
    	try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("scores.txt"), true)));			
			pw.print("\n" + nameInput);
			pw.print(", " + score);	
			pw.close();    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public String getNameInput() {
    	return nameInput;
    }

    public void loadHiscore() {
    	hiscore = getNthScore(0);
    }

    public void updateHiscore() {
        if (score > hiscore)
            hiscore = score;
    }

    public void addScore(int point) {
        score += point;
    }

    public void clearScore() {
        score = 0;
    }

    public int getNthScore(int i) {
    	return (int) mapOfScores.keySet().toArray()[i];
    }
    public String getNthName(int i) {
    	return (String) mapOfScores.values().toArray()[i];
    }

	public static List<String> readFileInList() {
		List<String> lines = Collections.emptyList();
	    try {
	      lines = Files.readAllLines(Paths.get("scores.txt"));
	    } catch (IOException e) { e.printStackTrace(); }
	    return lines;
	}

	public static void readFileInMap() {
		scores = readFileInList();
        for (int i = 0; i < scores.size(); i++) {
        	String nextLine = scores.get(i);
        	String[] parts = nextLine.split(", ");
        	String name = parts[0];
        	int score = Integer.parseInt(parts[1]);
        	mapOfScores.put(score, name);
        }
	}

    private static int[] alienTypes = new int[6];

    public static int[] changeAliens(int row1, int row2, int row3, int row4, int row5) {
    	alienTypes[0] = 0;
    	alienTypes[1] = row1;
    	alienTypes[2] = row2;
    	alienTypes[3] = row3;
    	alienTypes[4] = row4;
    	alienTypes[5] = row5;
    	return alienTypes;
    }

    @Override
    public void createAllObjs() {
        objs.add(new Initialiser(this));
        objs.add(new LoadingScreen(this));
        objs.add(new HUD(this));

        // shields
        for (int id = 0; id < 4; id++) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 3; col++) {
                    if (col == 1 && row == 3) {
                        continue;
                    }
                    Shield shield = new Shield(this, id, col, row);
                    objs.add(shield);
                }
            }
        }

        List<AlienShot> alienShots = new ArrayList<AlienShot>();
        	// top row ----> bottom row
        if (SpaceInvadersObj.getGameStyle() == 2)
        	changeAliens(4, 3, 2, 2, 1);
        else
        	changeAliens(3, 3, 2, 2, 1);

       	int[] alienPoints = { 0, 40, 40, 20, 20, 10 };

        for (int row = 1; row <= 5; row++) {
            for (int col = 1; col <= 11; col++) {
                AlienShot alienShot = new AlienShot(this);
                Alien alien = new Alien(this, col, row, alienTypes[row], alienShot, alienPoints[row]);
                objs.add(alien);
                alienShots.add(alienShot);
            }
        }

        ShipShot shipShot = new ShipShot(this);
        objs.add(new Ship(this, shipShot));
        objs.add(shipShot);

        for (AlienShot alienShot : alienShots)
            objs.add(alienShot);

        objs.add(new Saucer(this));
        objs.add(new Title(this));
        objs.add(new GameOver(this));    
    }

    @Override
    public Dimension getScreenSize() {
        return screenSize;
    }

    @Override
    public Point2D getScreenScale() {
        return screenScale;
    }

	public int getLevel() {		
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

    public String getScore() {
        String scoreStr = "0000000" + score;
        scoreStr = scoreStr.substring(scoreStr.length() - 7, scoreStr.length());
        return scoreStr;
    }

    public String getHiscore() {
        String hiscoreStr = "0000000" + hiscore;
        hiscoreStr = hiscoreStr.substring(hiscoreStr.length() - 7, hiscoreStr.length());
        return hiscoreStr;
    }

    public void startGame() {
        enemiesShootingProbability = 0.001;
        enemiesCount = 11 * 5;
        startGameTime = System.currentTimeMillis();
        setState(State.READY);
    }

    public void gameCleared() {
        setState(State.CLEAR);
    }

    public void nextGame() {
        enemiesShootingProbability += 0.0005;
        enemiesCount = 11 * 5;
        startGameTime = System.currentTimeMillis();
        level++;
        if (lives < 6)
        	lives++;
        // System.out.println("Level: " + getLevel());
        setState(State.READY);
    }


}