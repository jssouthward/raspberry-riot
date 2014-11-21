package edu.virginia.rich.cs4720;

public class GameState {
	private double progress;
	private int playerTurn;
	private final int PLAYERS;
	private boolean playing;
	private final int MAX_STATE;
	
	public GameState(int numPlayers, int numNodes) {
		progress = 0.0;
		playerTurn = 1;
		PLAYERS = numPlayers;
		playing = true;
		MAX_STATE = numNodes;
	}

	public void proceed(double amount) {
		progress += amount;
		
		if (progress > MAX_STATE)
			playing = false;
		
		if (playerTurn < PLAYERS)
			playerTurn++;
		else
			playerTurn = 1;
	}
	
	public double getState() {
		return progress;
	}
	
	public boolean isGameActive() {
		return playing;
	}

}
