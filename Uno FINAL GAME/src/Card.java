
public class Card implements Comparable {

	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	public static final int YELLOW = 3;
	public static final int NULL = 4;
	
	public static final int REVERSE = 10;
	public static final int SKIP = 11;
	public static final int DRAW_TWO = 12;
	public static final int WILD = 13;
	public static final int WILD_FOUR = 14;
	
	public Card(int color, int card) 
        {
		
		if (card < 0 && card < 14) throw new RuntimeException("The card you especified is not valid.");
		this.card = card;
		this.color = color;
		
		if (card == WILD || card == WILD_FOUR) 
                {
			this.color = NULL;
		}
	}
	
	public int getColor() 
        {
		return color;
	}
	
	public int getValue() 
        {
		return card;
	}
	
	public boolean isNumber() 
        {
		return (card >= 0 && card < 10);
	}
	
	public boolean isSkip() 
        {
		return (card == SKIP);
	}
	
	public boolean isReverse() 
        {
		return (card == REVERSE);
	}
	
	public boolean isDrawTwo() 
        {
		return (card == DRAW_TWO);
	}
	
	public boolean isWild() 
        {
		return (color == NULL);
	}
	
	public boolean isWildFour() 
        {
		return (card == WILD_FOUR);
	}
	
	public String toString() 
        {
		String nameColor = "";
		String number = "";
		
		switch (color) 
                {
			case RED:    nameColor = "Red ";    break;
			case GREEN:  nameColor = "Green ";  break;
			case BLUE:   nameColor = "Blue ";   break;
			case YELLOW: nameColor = "Yellow "; break;
			case NULL:   nameColor = "";       break;
		}
		
		switch (card) 
                {
			case REVERSE:   number = "Reverse";           break;
			case SKIP:      number = "Skip";              break;
			case DRAW_TWO:  number = "Draw Two";          break;
			case WILD:      number = "Wild";               break;
			case WILD_FOUR: number = "Wild \"Draw Four\""; break;
			default:        number = "" + card;            break;
		}
		
		return (nameColor + number);
	}
	
	public int compareTo(Object objeto) 
        {
		
		if (!(objeto instanceof Card)) throw new ClassCastException("Object is not a card");
		else 
                {
			Card carta = (Card)objeto;
			if (this.color > carta.color)      
                            return  10;
			else if (this.color < carta.color) 
                            return -10;
			else 
                        {
				if (this.card > carta.card)      return  10;
				else if (this.card < carta.card) return -10;
				else return 0;	
			}
		}
	}
	
	private int color;
	private int card;
}