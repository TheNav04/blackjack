import java.awt.Image;

public class Card {
	private String value;
	private String suit;
	private boolean isVisible = true;
	
	public Card(String value, String suit) {
		this.value = value;
		this.suit = suit;
	}
	
	public int getScore() {
		if("JQKA".contains(value)) {
			return 10;
		}
		else {
			return Integer.parseInt(value);
		}	
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getSuit() {
		return this.suit;
	}
	
	public String toString() {
		
		return value + " of " + suit;
	}
	
}
