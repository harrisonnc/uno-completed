import java.util.*;


 
public class UNOGame implements GameMessages {
	
	/** This is the playing pile */
	public static final int DISCARD = 70;
	/** This is the pile where you get the cards */
	public static final int DECK = 72;

	/** This is the pile where you get the cards */
	private UNODeck   deck;
	/** This vector holds all the player piles   */
	public Vector    players;
	/** This is the playing pile */
	private UNODeck   discard;
	
	private int startingCards;
	private int numPlayers;
        public ArrayList<Object> scoreTable;
	private int playerRetired;
	public Turn turn;         //Tells the player in turn
	private int lastTurn;
	
	//These two values are determined by the public fields of the Card class
	private int currentColor;  //The color of the top card
	private int currentCard;   //The number of the top card

	private int currentCardsToDraw; //Very important, sets the cards for d2 and d4
	private boolean colorSet;       //Tells if a wild was played
	private boolean finished;       //Tells if the game ended
	
	private boolean withWildWin;
	private boolean withZeroRule;
	
	/**
	  Creates an UNO game with the desired options.
	  @see Options
	 */
	public UNOGame(Options options) {
		initGame(options);
	}
	
	/**
	  Resets the game, with the desired options
	 */
	public void initGame(Options options) {
		
		startingCards = options.getNumberCards();
		numPlayers = options.getNumberPlayers();
		playerRetired = -1;
                
		
		deck = new UNODeck(UNODeck.FULL);
		deck.shuffleDeck();	
		//Dealing the cards
		players = new Vector();
		
		for (int i = 0; i != options.getNumberPlayers(); i++) {
			players.add(new UNODeck(UNODeck.EMPTY));
			
			for (int j = 0; j != options.getNumberCards(); j++) {
				((UNODeck)players.get(i)).insertCard( deck.getCard(0) );
			}
			((UNODeck)players.get(i)).sort();
		}
		
		discard = new UNODeck(UNODeck.EMPTY);
		
		do {
			discard.insertCard( deck.getCard(0) );
			currentColor = getLastCardPlayed().getColor();
			currentCard  = getLastCardPlayed().getValue();
		} while (!discard.showCard(0).isNumber());
		
		currentCardsToDraw = 0;
		turn = new Turn((new Random().nextInt(options.getNumberPlayers())), options.getNumberPlayers(), 1);
		lastTurn = turn.showCurrentTurn();
		colorSet = true;
		finished = false;
		
		withWildWin  = options.withWildWin();
		withZeroRule = options.withZeroRule();
	}
	
	private boolean existsValidPlay() {
		//revisar que exista carta en players[turn] que sea igual en numero o color al discard deck
		
		boolean sameColor;
		boolean sameNumber;
		boolean isWild;
		
		for (int i = 0; i != ((UNODeck)players.get(turn.showCurrentTurn())).getSize(); i += 1) {
			if (isValidCard(i)) return true;
		}
		
		return false;
	}
        

        
	private void playCard(int numCard) {
		if (!isValidCard(numCard)) throw new IllegalArgumentException("This is not a valid card.");
		discard.insertCard( ((UNODeck)players.get(turn.showCurrentTurn())).getCard(numCard) );
		currentColor = getLastCardPlayed().getColor();
		currentCard  = getLastCardPlayed().getValue();
                
	}
	
	/**
	 Returns:
	n>=1 - Drawn n cards
	-1   - Had to draw cause of a D2 and didn't
	-2   - Had to draw cause of a W4 and didn't
	-3   - Can play a stacked D2 or W4 and wants to draw???
	-4   - The deck's empty
	
	*/
	private int drawCard(int times) {
		
		if (times == 1 && currentCardsToDraw != 0) return -3;
		
		if (times == 2) {
			
			currentCardsToDraw += times;
			
			if (((UNODeck)players.get(turn.showNextTurn())).exists(new Card(Card.NULL, Card.DRAW_TWO)) ||
			    ((UNODeck)players.get(turn.showNextTurn())).exists(new Card(Card.NULL, Card.WILD_FOUR))) {    
			    return -1;
			}
		} else if (times == 4) {
			currentCardsToDraw += times;
			if (((UNODeck)players.get(turn.showNextTurn())).exists(new Card(Card.NULL, Card.WILD_FOUR))) {
				return -2;
			}
		} else {
			currentCardsToDraw += times;
		}

		//The next part is the harsh reality if the next players doesn't have a d2 or a w4
		if (times != 1) turn.nextTurn();
		
		int cardsDrawn = currentCardsToDraw;
		
		if (getDeck(DECK).isEmpty()) {
			currentCardsToDraw = 0;
			return -4;
		}
		
		
		for (int i = 0; i != currentCardsToDraw; i += 1) {
			
			((UNODeck)players.get(turn.showCurrentTurn())).insertCard( deck.getCard(0) );
			
			if (deck.getSize() == 0) {
				if (discard.isEmpty()) {
					cardsDrawn = i+1;
					break;
				}
				
				while (!discard.isEmpty()) {
					deck.insertCardAtLast(discard.getCard(0));
				}
				
				discard.insertCard(deck.getCard(deck.getSize() - 1));
				
				deck.shuffleDeck();
			}
		}
		
		currentCardsToDraw = 0;
		((UNODeck)players.get(turn.showCurrentTurn())).sort();
		
		return cardsDrawn;
	}
	
	public void restartHand(int forPlayer) {
		if (((UNODeck)players.get(forPlayer)).getSize() > 1) return;
		
		for (int i = 0; i != 6; i += 1) {
			((UNODeck)players.get(forPlayer)).insertCard( deck.getCard(0) );
			
			if (deck.getSize() == 0) {
				if (discard.isEmpty()) {
					return;
				}
				
				while (!discard.isEmpty()) {
					deck.insertCardAtLast(discard.getCard(0));
				}
				
				discard.insertCard(deck.getCard(deck.getSize() - 1));
				
				deck.shuffleDeck();
			}
		}
	}
	
	public UNODeck getDeck(int numDeck) {
		
		try {
			return ((UNODeck)players.get(numDeck));
		}
		catch (Exception e) {}
		
		switch (numDeck) {
			case DECK: return deck;
			
			case DISCARD: return discard;
			
			default: throw new RuntimeException("invalid deck");
		}
	}
	
	public UNODeck getCurrentDeck() {
		return ((UNODeck)players.get(turn.showCurrentTurn()));
	}
	
	public Card getLastCardPlayed() {
		return discard.showCard(0);
	}
	
	public int getDirection() {
		return turn.getDirection();
	}

	public int getCurrentColor() {
		return currentColor;
	}

	public int getNumberPlayers() {
		return players.size();
	}
	
	public boolean withWildWin() {
		return withWildWin;
	}
	
	public boolean withZeroRule() {
		return withZeroRule;
	}

	public boolean retirePlayer(int playerNumber) {		
		if (players.size() == 1) return false;
		
		playerRetired = playerNumber;
		
		while (((UNODeck)players.get(playerNumber)).getSize() != 0)
			deck.insertCardAtLast(((UNODeck)players.get(playerNumber)).getCard(0));
		
		deck.shuffleDeck();
		
		players.remove(playerNumber);
		
		
		turn.retirePlayer();
		return true;
	}

	/**
	  Adds a new player to the field, if and only if there's
	  space for one more player (the maximum is the number
	  originally set in the options).
	  
	  Returns:
	  
	  -1 - If the maximum players has been reached
	  -2 - If there are not enough cards to give to the new player
	 >=0 - New player's number
	 */
	public int addNewPlayer() {
		if (players.size() == numPlayers) return -1;
		
		players.add(new UNODeck(UNODeck.EMPTY));
		
		if (deck.getSize() < startingCards) {
			if (discard.getSize() + deck.getSize() - 1 < startingCards) {
					return -2;
			}
				
			while (!discard.isEmpty()) {
				deck.insertCardAtLast(discard.getCard(0));
			}
				
			discard.insertCard(deck.getCard(deck.getSize() - 1));
				
			deck.shuffleDeck();
		}
		
		for (int i = 0; i != startingCards; i++)
			((UNODeck)players.get(players.size() - 1)).insertCard(deck.getCard(0));
		
		((UNODeck)players.get(players.size() - 1)).sort();
		
		turn.addNewPlayer();
		
		return players.size() - 1;
	}
	
	public boolean isValidCard(int cardNumber) {
		boolean sameColor;
		boolean sameNumber;
		boolean isWild;
		boolean isWildFour;
		boolean isDrawTwo;
		
		sameColor  = getCurrentDeck().showCard(cardNumber).getColor() == currentColor;
		sameNumber = getCurrentDeck().showCard(cardNumber).getValue() == currentCard;
			
		isWild = getCurrentDeck().showCard(cardNumber).isWild();
		isWildFour = getCurrentDeck().showCard(cardNumber).isWildFour();
		isDrawTwo = getCurrentDeck().showCard(cardNumber).isDrawTwo();
		
		if (currentCardsToDraw != 0) { //Check this!
			if (getLastCardPlayed().isWildFour()) {
				return (isWildFour);
			} else {
				return (isDrawTwo || isWildFour);
			}
		}
		
		if (!withWildWin) {
			return ((sameColor || sameNumber || isWild) && !(isWild && getCurrentDeck().getSize() == 1));
		}
		
		return (sameColor || sameNumber || isWild);
		
	}
	
	private int switchHands() {
		if (!withZeroRule) return 0;
		
		Turn i = new Turn(turn.showCurrentTurn(), getNumberPlayers(), turn.getDirection()*-1);
		
		UNODeck first = ((UNODeck)players.get(i.showCurrentTurn()));
		UNODeck temp;
		
		for (int j = 0; j != getNumberPlayers()-1; j += 1) {
			players.set(i.showCurrentTurn(), ((UNODeck)players.get(i.showNextTurn())));
			i.nextTurn();
		}
		players.set(i.showCurrentTurn(), first);
		
		return 6;
	}
	
	public int getCurrentPlayer() {
		return turn.showCurrentTurn();
	}
	
	public int getNextPlayer() {
		return turn.showNextTurn();
	}
	
	public int getPrevPlayer() {
		return turn.showPrevTurn();
	}
	
	public int getLastPlayer() {
		return lastTurn;
	}
	
	public void setColor(int color) {
		if (colorSet)
			throw new RuntimeException("You can only set a color with a previous Wild");
		
		if (color < 0 || color > 3)
			throw new RuntimeException("Invalid color chosen.");
		
		currentColor = color;
		
		colorSet = true;
	}
	
	
	public int playCard(int player, int cardNumber) {

		if (finished) return  GAME_FINISHED;
		if (!colorSet) return SELECT_COLOR_FIRST;
		
		if (playerRetired != -1) { //If a player has retired
			int temp = playerRetired;
			playerRetired = -1;
			return PLAYER_RETIRED + temp;
		}
		
		int result = 0;
		
		//Just a little add on for cheaters :)
		if (player != turn.showCurrentTurn()) return -6;
		
		lastTurn = turn.showCurrentTurn();
		
		//The player decides to draw a card
		if (cardNumber == getCurrentDeck().getSize()) {
			
			int drawn = drawCard(1);
			
			((UNODeck)players.get(turn.showCurrentTurn())).sort();
			
			if (drawn > 0)
				if (existsValidPlay()) return -3;
			else if (drawn == -3) {
				if (existsValidPlay()) return -1;
			} else if (drawn == -4) {
				return -4;
			}
			
			if (!existsValidPlay()) {
				turn.nextTurn();
				return -2;
			}
		}
		
		//The player decides to play a normal card
		result = playTurn(cardNumber);
			
		return result;
	}
	
	private int playTurn(int numCard) {
		int c = 0;
		
		try {
			playCard(numCard);
		}
		catch (IllegalArgumentException e) {
			return -1;
		}
		
		if (((UNODeck)players.get(turn.showCurrentTurn())).getSize() == 0) {
			c += 35;
			finished = true;
		}
		
		if (((UNODeck)players.get(turn.showCurrentTurn())).getSize() == 1)
			c += 20;
		
		int cardsDrawn = 0;
		
		switch (getLastCardPlayed().getValue()) {
			
			default:
				c += 0;
				break;
			case 0:
				c += switchHands();
				break;
			case Card.REVERSE:
				c += 1;
				turn.changeDirection();
				if (getNumberPlayers() == 2) turn.nextTurn();
				break;
				
			case Card.SKIP:
				c += 2;
				turn.nextTurn();
				break;
				
			case Card.DRAW_TWO:
				c += 3;
				cardsDrawn = drawCard(2);
				if (cardsDrawn == -1 || cardsDrawn == -2) c += 4;
				
				break;
				
			case Card.WILD:
				c += 4;
				colorSet = false;
				break;
				
			case Card.WILD_FOUR:
				c += 5;
				colorSet = false;
				cardsDrawn = drawCard(4);
				if (cardsDrawn == -1 || cardsDrawn == -2) c += 3;
				break;
		}
		
		turn.nextTurn();
		
		return c;
	}


}
