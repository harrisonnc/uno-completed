public class Turn {
	
	public static final int CLOCKWISE = 1;
	public static final int COUNTER_CLOCKWISE = -1;
	
	public Turn(int startingPlayer, int numPlayers, int direction) {
		
		if (startingPlayer >= numPlayers)
			throw new IllegalArgumentException("Starting player is out of the range of players");
		
		if (direction != CLOCKWISE && direction != COUNTER_CLOCKWISE) 
			throw new IllegalArgumentException("Direction is not right.");
			
		playerInTurn    = startingPlayer;
		this.numPlayers = numPlayers;
		this.direction  = direction;
	}
	
	public int showCurrentTurn() {
		return playerInTurn;
	}
	
	public int showPrevTurn() {
		return checkBoundaries(playerInTurn - direction);
	}
	
	public int showNextTurn() {
		return checkBoundaries(playerInTurn + direction);
	}
	
	public void nextTurn() {
		playerInTurn += direction;
		playerInTurn = checkBoundaries(playerInTurn);
	}
	
	public void changeDirection() {
		direction *= -1;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void retirePlayer() {
		if (numPlayers == 1) return;
		numPlayers--;
		checkBoundaries(playerInTurn);
	}
	
	public void addNewPlayer() {
		numPlayers++;
	}
	
	private int checkBoundaries(int position) {
		if (position >= numPlayers) return 0;
		else if (position < 0) return numPlayers - 1;
		else return position;
	}
	
	private int playerInTurn;
	private int numPlayers;
	private int direction;
}