package Castle;

import java.util.Scanner;

public class Tester {
	
	public static void main(String[] args) {
		Deck deck = new Deck();
		Player player1 = new Player();
		Scanner in = new Scanner(System.in);

				
		for (int i = 0; i < 7; i++) {
			player1.addCard(deck.drawCard());
		}	

		System.out.println("Please select 4 cards to put face up on the board.");
		player1.displayHand();
		int check = 0;
		while (check < 4) {			
			String playerInput = in.next();
			if (player1.addFaceUp(playerInput)) {
				check++;
			} else
				System.out.println("Card not in hand");
		}
		player1.visualize();

		in.close();
	}
}
