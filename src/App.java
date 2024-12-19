import java.awt.Color;

import javax.swing.*;

public class App {
	public static void main(String[] args) {
		int boardLength = 1000;
		int boardWidth = 700;
		
		JFrame window = new JFrame("Blackjack");
		
		window.setSize(boardLength, boardWidth);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setVisible(true);
		
		Board game = new Board(boardLength, boardWidth);
		window.add(game);
		window.pack();
		
	}
}
