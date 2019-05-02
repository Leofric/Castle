package Castle;

public class Tester {

	//BUG
	//Hand 				[9 9 7 7 3 3 3]
	//Expected outpupt 	[9 9 7 7]
	//Actual output 	[9 9 7 3]
	
	public static void main(String[] args) {
		Deck deck = new Deck();
		Player player1 = new Player();
		
		//Deal
		for (int i = 0; i < 4; i++) {
			player1.addFaceDown(deck.drawCard());
		}
		
		for (int i = 0; i < 7; i++) {
			player1.addCard(deck.drawCard());
		}	
		
		player1.AIFaceUp();
		player1.visualize();


		
	}

}
