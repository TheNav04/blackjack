import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener, KeyListener {		
	String[] suitType = {"S", "C", "D", "H"}; 
	String[] numType = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"}; 
	
	//board properties
	private int boardHeight = 700;
	private int boardLength = 1200;
	private JButton hit;	//need this.setLayout(null) in order to move around JButton
	private JButton stand;
	private boolean noMoreStand = false;
	//button capable of restart
	
	class Person {
		int score = 0;
		boolean win;
		boolean hiddenCard;
		String name;
		ArrayList<Card> hand = new ArrayList<Card>(); 
		
		public Person(String theName) {
			this.name = theName;
		}		
	}
	
	Person dealer;
	Person player;
	
	private Random rand = new Random(); //pick index in arrayList at random
	int shuffleCard;
	
	ArrayList<Card> cardSet = new ArrayList<Card>();
	
	public Board(int boardH, int boardL) {
		this.boardHeight = boardH;
		this.boardLength = boardL;
		
		setFocusable(true);
		setPreferredSize(new Dimension(this.boardHeight, this.boardLength));
		setBackground(new Color(53, 101, 77));
		this.setLayout(null);	//invalidates component  hierarchy, allows buttons to change position 
		
		hit = new JButton("hit");
		hit.setBounds(250, boardLength - 50, 90, 50);
		this.add(hit);
		hit.addActionListener(this);
		
		stand = new JButton("Stand");
		stand.setBounds(350, boardLength - 50, 90, 50);
		this.add(stand);
		stand.addActionListener(this);
		
		dealer = new Person("Dealer");
		player = new Person("Player");
		startDeck();
		
		pickUpCard(dealer);
		pickUpCard(dealer);
			
		pickUpCard(player);
		pickUpCard(player);
		}
	

	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		//line separating dealer and player
		g.setColor(Color.white);
		g.drawLine(0, boardLength/2 - 25, boardHeight, boardLength/2 - 25);
		
		//Dealer side text
		g.setFont(new Font("Ariel", Font.PLAIN, 20));
		g.setColor(Color.black);
		if(noMoreStand == false) {
			g.drawString("Dealer: Unkown", 0, 20);
		}
		else {
			g.drawString("Dealer: " + dealer.score, 0, 20);
		}
		
		//Player side text
		g.drawString("Player: " + player.score, 0, boardLength/2);
		g.setColor(Color.white);
		
		//dealer cards //x -> n * 170, y -> 25
		for(int i = 0; i < dealer.hand.size(); i++) {
			if(i == 0 && noMoreStand == false) {
				dealer.hiddenCard = true;
				cardDrawing(g, 170*i, 25, dealer, i);
				dealer.hiddenCard = false;
			}
			else{
				cardDrawing(g, 170*i, 25, dealer, i);
			}
		}
		
		//Player cards //x -> n * 170, y -> 375
		for(int i = 0; i < player.hand.size(); i++) {
			cardDrawing(g, 170*i, 375, player, i);
		}
		
		//win conditions
		g.setColor(Color.black);
		if(player.win == false && dealer.win == false && noMoreStand == true) {
			g.drawString("You Tied" , boardLength/2 , boardLength/2);
		}
		else if(player.win == false && dealer.win == true) {
			g.drawString("You lose!" , boardLength/2 , boardLength/2);
		}
		else if(player.win == true && dealer.win == false) {
			g.drawString("You Win!" , boardLength/2 , boardLength/2);
		}
	}
	
	public void cardDrawing(Graphics g, int xPlace, int yPlace, Person guy, int cardIndex) {
		g.fillRect(xPlace, yPlace, 160, 250);
		g.setColor(Color.black);
		String cardData = guy.hand.get(cardIndex).toString(); 
		
		if(guy.hiddenCard == true) {
			g.drawString("Hidden Card", xPlace + 5, yPlace + 25);
		}
		else {
			g.drawString(cardData, xPlace + 5, yPlace + 25);
		}
		
		g.setColor(Color.white);
		
	}
	
	public void startDeck() {
		if(cardSet != null) { 
			cardSet.clear();	
		}	
		for(int i = 0; i < suitType.length; i++) {
			String suit = suitType[i];
			for(int j = 0; j < numType.length; j++) {
				String cardValue = numType[j];
				cardSet.add(new Card(cardValue, suit));
			}
		}	
	}
	
	//interface methods
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == hit && noMoreStand == false) {
			pickUpCard(player);
			//case for when player over 21
			if(player.score > 21) {
				player.win = false;
				dealer.win = true;
				System.out.println("You have lost!");
				noMoreStand = true;
				
			}
		}
		if(e.getSource() == stand) {
			noMoreStand = true;
			repaint();
			//dealer pick up cards when player no longer pick up cards
			//dealer pick up if less than player
			//dealer pick up if less than 17
			while(dealer.score < 17 && (player.score > dealer.score)) {
				pickUpCard(dealer);
			}
			//if conditions once no more cards can be picked up
			if(dealer.score > 21 || dealer.score < player.score) {
				player.win = true;
				dealer.win = false;
				System.out.println("Player win");
			}
			else if(dealer.score > player.score) {
				player.win = false;
				dealer.win = true;
				System.out.println("Dealer win");
			}
			else if(player.score == dealer.score) {
				player.win = false;
				dealer.win = false;
				System.out.println("Tie");
			}
			
		}
	}
	
	public void pickUpCard(Person person) {
		//selects random index in ArrayList
		shuffleCard = rand.nextInt(cardSet.size() - 1);
		
		Card cardPicked = cardSet.get(shuffleCard);
		System.out.println(cardPicked); //used for debugging
		person.hand.add(cardPicked);
		person.score += cardPicked.getScore();
		System.out.println("Score for " + person.name + " is: " + person.score); //used for debugging
		cardSet.remove(shuffleCard);		
		//update text and images when card is picked up
		repaint();
	}
	
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

}
