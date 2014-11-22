package edu.virginia.rich.cs4720;

public class GameState {
	private int progress;
	private int playerTurn;
	private int winner;
	private final int PLAYERS;
	private boolean playing;
	private final int MAX_STATE;
	
	public GameState(int numPlayers, int numNodes) {
		progress = 0;
		playerTurn = 1;
		PLAYERS = numPlayers;
		playing = true;
		MAX_STATE = numNodes;
	}

	public void proceed(int amount) {
		progress += amount;
		
		if (playerTurn < PLAYERS)
			playerTurn++;
		else
			playerTurn = 1;
		
		if (progress >= MAX_STATE) {
			playing = false;
			winner = playerTurn;
		}
	}
	
	public int getStateNodeID() {
		return progress-1;
	}
	
	public double getStatePercent() {
		return ((double)(progress)/(double)(MAX_STATE))*100;
	}
	
	public int getWinner() {
		return winner;
	}
	
	public boolean isGameActive() {
		return playing;
	}
	
	public int currentTurn() {
		return playerTurn;
	}

}
