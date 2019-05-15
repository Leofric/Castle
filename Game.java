package Castle;
import java.util.Scanner;

public class Game {	
	public static void main(String[] args) {
		System.out.println("Welcome to the virtual Castle game! This is a 1 vs 1");

		//Initialization
		Scanner in = new Scanner(System.in);
		Deck deck = new Deck();
		PlayingField field = new PlayingField();
 
		Player player1 = new Player();
		Player player2 = new Player();

		//Deal
		for (int i = 0; i < 4; i++) {
			player1.addFaceDown(deck.drawCard());
			player2.addFaceDown(deck.drawCard());
		}
		
		for (int i = 0; i < 7; i++) {
			player1.addCard(deck.drawCard());
			player2.addCard(deck.drawCard());
		}		

		// Choose face up cards. Set up Phase
		player2.AIFaceUp();
		System.out.println("Please select 4 cards to put face up on the board.");
		player1.displayHand();
		
		int check = 0; //allows player to try again if they make a mistake
		while (check < 4) {			
			int card = Game.convertInput(in.nextLine());
			if (player1.addFaceUp(card)) {
				check++;
			} else
				System.out.println("Sorry that card is not in your hand, try again");
		}
		
		//Starting the game, Phase 1
		System.out.println("Choose a card to play to begin the game!");
		boolean win = false;

		while (!win) {
			boolean invalidMove = true;
			String arguments[] = new String[2];
			
			//Visualization of the game
			player2.visualize("AI");
			player1.visualize("Player");
			
			//Start Player 1 Turn
			//This loop represents 1 turn, invalidMove allows player to try again if they make a mistake
			while (invalidMove) {
	
				//player 1 set phase
				if (player1.handSize() > 0) {
					player1.setPhase(0);
				} else if (player1.getFaceUpCount() > 0) {
					player1.setPhase(1);
				} else {
					player1.setPhase(2);
				}
				
				String input = in.next();
				int card = 0;
				int frequency = 0;
				
				if(input.contains(",")){
					arguments = input.split(",");
					card = Game.convertInput(arguments[0]);
					frequency = Game.convertInput(arguments[1]);
				}
				else{
					card = Game.convertInput(input);
				}

				if (player1.getPhase() == 2) {
						if (card < player1.getFaceDownCount()) {
							if (player1.getFaceDownCard(card) >= field.peek()
									|| player1.getFaceDownCard(card) == 10
									|| player1.getFaceDownCard(card) == 2) {
	
								Game.printPlay(player1.getFaceDownCard(card));
								field.push(player1.getFaceDownCard(card));
	
								//handle special case card = 2
								if (player1.getFaceDownCard(card) != 2) {
										invalidMove = false;
								} 

								player1.removeCard(card);
								if(player1.getFaceDownCount() == 0){
									invalidMove = false; //double check, no reason to reloop if game is already over
								}
							} 
							else {
								field.push(player1.getFaceDownCard(card));
								Game.printPlay(player1.getFaceDownCard(card));
								player1.removeCard(card);
	
								System.out.println("You picked up");
								invalidMove = false;
	
								while (!field.isEmpty()) {
									int cards = field.pop();
									player1.addCard(cards);
								}
							}
						} else {
							System.out.println("Invalid move, please select one of the face down cards..");
						}
				}
				else if (frequency > 1) {
						if (player1.play(card, frequency) && card >= field.peek()){
							for (int i = 0; i < frequency; i++) {
								player1.removeCard(card);
								field.push(card);
							}
							Game.printPlay(card, frequency);
					
							// draw
							try {
								while (player1.handSize() < 3 && deck.cardsLeft() > 0) {
									player1.addCard(deck.drawCard());
								}
							} 
							catch (IllegalArgumentException x) {
								System.out.println("Something went wrong");
							}
							invalidMove = false;
						} 
						else {
							System.out.println("invalid selection");
						}
				} 
				else if (player1.play(card) && (card >= field.peek() || card == 2 || card == 10)) {
					player1.removeCard(card);
					field.push(card);

					//check for 2, if 2 then reloop
					if (card != 2) {
						invalidMove = false;
					}
					else if(player1.handSize() == 0 && player1.getFaceDownCount() == 0){
						invalidMove = false;
					}

					// draw
					if (player1.handSize() < 3 && deck.cardsLeft() > 0) {
						try {
							player1.addCard(deck.drawCard());
						} catch (IllegalArgumentException e) {
							System.out.println("Something went wrong");
						}
					}
					Game.printPlay(card);
				}
				else if(card == 0){ //'strategic' pick up
					System.out.println("You picked up");
					invalidMove = false;

					while (!field.isEmpty()) {
						int cards = field.pop();
						player1.addCard(cards);
					}
				}
				else if (!player1.play(card)) {
					System.out.println("invalid selection");
				} 
				else {
					System.out.println("You picked up");
					invalidMove = false;

					while (!field.isEmpty()) {
						int cards = field.pop();
						player1.addCard(cards);
					}
				}				
			} // End Player 1 Turn

			// Check for special cases 
			// Four of a kind ~ clear field
			if (field.size() > 3) {
				if(field.get(field.size()-1) == field.get(field.size()-2) && field.get(field.size()-1) == field.get(field.size()-3) && field.get(field.size()-1) == field.get(field.size()-4)){
					field.clear();
					System.out.println("4 of a kind! The playing field has been cleared!");
				}
			}

			// Special card 10 ~ clear field
			if (!field.isEmpty() && field.peek() == 10) {
				field.clear();
				System.out.println("10 cleared the field");
			}
			
			
		// Begin AI Turn
		boolean AITurn = true;
		
		//check to see if player 1 already won
		if (player1.handSize() == 0 && player1.getFaceDownCount() == 0) {
			win = true;
			AITurn = false;
			System.out.println("You won!");
		}
		
		while(AITurn){
			
			// AI set phase
			if (player2.handSize() > 0) {
				player2.setPhase(0);
			} else if (player2.getFaceUpCount() > 0) {
				player2.setPhase(1);
			} else {
				player2.setPhase(2);
			}
			
			int AIMove = 0;
			AIMove = player2.AIplay(field.peek());

			//if AIMove = 2, then you need to run previous line again
			if(AIMove == 2){
				field.push(AIMove);
			
				if(player2.getPhase() == 2)
					player2.removeCard(0);	//AI always plays the first (location 0) face down card, no effect on gameplay
				else
					player2.removeCard(AIMove); 
				Game.printAIPlay(AIMove);

				//Check win condition
				if(player2.handSize() == 0 && player2.getFaceDownCount() == 0)
					AITurn = false;
				else
					AITurn = true;
			}
			else if (AIMove > 0) {
				int frequency;
				if(player2.getPhase() == 0)
					frequency = player2.getCardFrequency(AIMove);
				else if(player2.getPhase() == 1)
					frequency = player2.getCardFrequencyFaceup(AIMove);
				else
					frequency = 1;

				//niche play?
				if(frequency > 1 && AIMove == 10 && deck.cardsLeft() > 0){
					frequency = 1;
				}
				
				if(frequency > 1){
					for (int i = 0; i < frequency ; i++) {
						player2.removeCard(AIMove);
						field.push(AIMove);
					}
					Game.printAIPlay(AIMove, frequency);
				}
				else {
					field.push(AIMove);
					
					if(player2.getPhase() == 2)
						player2.removeCard(0);
					else
						player2.removeCard(AIMove);
					
					Game.printAIPlay(AIMove);
				}
				AITurn = false;
			}
			else {	// Computer picks up
				if(player2.getPhase() == 2){
					field.push(player2.getFaceDownCard(0));
					Game.printAIPlay(player2.getFaceDownCard(0));
					player2.removeCard(0);
				}
				
				while (!field.isEmpty()) {
					int card = field.pop();
					player2.addCard(card);
					AITurn = false;
				}
				System.out.println("The computer picked up");
			}
		}
			// draw
			try {
				while (player2.handSize() < 3 && deck.cardsLeft() > 0) {
					player2.addCard(deck.drawCard());
				}
			} catch (IllegalArgumentException x) {
				System.out.println("Something went wrong");
			}

			// Check for special cases 
			// Four of a kind ~ clear field
			if (field.size() > 3) {
				if(field.get(field.size()-1) == field.get(field.size()-2) && field.get(field.size()-2) == field.get(field.size()-3) && field.get(field.size()-3) == field.get(field.size()-4)){
					field.clear();
					System.out.println("4 of a kind! The playing field has been cleared!");
				}
			}

			// Special card 10 ~ clear field
			if (!field.isEmpty() && field.peek() == 10) {
				field.clear();
				System.out.println("10 cleared the field");
			}

			// win condition
			if (player2.handSize() == 0 && player2.getFaceDownCount() == 0 && player2.getFaceUpCount() == 0) {
				win = true;
				System.out.println("You Lost!");
			}
		}
		in.close();
	}
	
		//convert input into card value, or -1 if input doesn't match. 
		public static int convertInput(String input){
			int card = 0;
			
			try {
				card = Integer.parseInt(input);
			}
			catch(IllegalArgumentException e){
				if(input.equals("J") || input.equals("j"))
					card = 11;
				else if(input.equals("Q") || input.equals("q"))
					card = 12;
				else if(input.equals("K") || input.equals("k"))
					card = 13;
				else if(input.equals("A") || input.equals("a"))
					card = 14;
				else
					card = -1;
			}
			return card;
		}
		
		//convert input 'card value' to string representation
		public static String convertOutput(int card){
			String output;
			switch(card){
			case 14:
				output = "A";
				break;
			case 13:
				output = "K";
				break;
			case 12:
				output = "Q";
				break;
			case 11:
				output = "J";
				break;
			default:
				output = Integer.toString(card); 		
			}
			return output;
		}

		//prints successful player move to the console
		public static void printPlay(int input){
			String card = Game.convertOutput(input);
			System.out.println("You played "+ card);
		}
		
		//prints successful computer move to the console
		public static void printAIPlay(int input){
			String card = Game.convertOutput(input);
			System.out.println("Computer played "+ card);
		}
		
		//prints successful player multi card moves to the console
		public static void printPlay(int input, int frequency){
			String card = Game.convertOutput(input);
			System.out.println("You played "+card+" ("+frequency+") "+" times");
		}
		
		//prints successful computer multi card moves to the console
		public static void printAIPlay(int input, int frequency){
			String card = Game.convertOutput(input);
			System.out.println("Computer played "+card+" ("+frequency+") "+" times");
		}
}
