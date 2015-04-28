public abstract class AIPlayer {
	
	protected UNOGame thisGame;
	
	public AIPlayer(UNOGame game) {
		thisGame = game;
	}
	
	public abstract int playMyTurn(UNODeck myDeck);
	public abstract int chooseColor(UNODeck myDeck);
}