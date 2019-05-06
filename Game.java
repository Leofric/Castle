package Castle;
/*	GAME RULES ~ EXPLAINATION
 		The purpose of the game is to get rid of all of your cards before your opposition does. At the start of the game,
 		each player is dealt 4 cards face down, these cards are to remain face down until all of the players cards have
 		been played. Each player is also then dealt 7 more cards that they can look at, any cards left over after dealing
 		all players in are placed in a stack in the middle of the playing field. Once all players have recieved their 4 
 		face down cards and regular 7 card hand, each player must then select 4 cards from their hand and place them face
 		up on top of their 4 face down cards. Once all players have placed their 4 cards face up on their 4 face down cards,
 		the game can begin. Players must keep 3 cards in their hand at all times, After playing a card or cards, players
 		must draw from the extra cards to maintain the 3 card hand until all cards from the middle have been depleted. 
 		
 		Each player takes turns playing one of the cards from their hand. When playing a card, the face value of the card 
 		must be greater than or equal to the value of the card on the board. A player may not play a card that has a lower
 		value than that of which is on the board. If a player cannot beat the card on the board, then they must pick up all
 		cards on the table and are not allowed to play any cards that turn. Cards 3-9 and 11-14(J, Q, K, A) are regular
 		cards with their values corresponding in that order. 2's and 10's are special cards each with their own special
 		properties. When a 10 is played, all cards on the table are discarded and removed from the game. When a 2 is played,
 		the player may play a second card on top of it of any value, essentialy soft reseting the pile however all cards
 		below remain in play. Additionally if 4 of the same card are played, either by the same player or a combination of 
 		players, all cards on the table are discarded and removed from the game.  
 */


import java.util.Scanner;

// PROGRAM BUGS
/*	
	  
 * Dumb AI: 
 	 - AI has 2, 2, 10 - then it is better to play the 10. currently hard coded to prioritize 2's
 	 - never play a 10 (2) times unless drawing deck is empty?
 	 - 2, 2, 10 makes sense if that is all you have going into face up phase
	 
	 - doesn't play high cards when you are on last face down cards, will even play a 10 and clear the pile and 
	 	let you win, no logic
 
*/

// Quality of life changes
/*
 11 = J
 12 = Q
 13 = K
 14 = A
 -- basically just when you see a number print out letter if said number is seen?
 -- remember also that user input will be changed, need to convert letter into number as well
 -- might cause issues with scanner, expecting int but possibly getting int and char 
*/

public class Game {	
	public static void main(String[] args) {
		System.out.println("Welcome to the virtual Castle game! This is a 1 vs 1");

		// initialization
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
		int check = 0; // so if a player makes an invalid move, it goes through
					   // the loop again instead of adding a 0
		while (check < 4) {
			int playerInput = in.nextInt();
			if (player1.addFaceUp(playerInput)) {
				check++;
			} else
				System.out.println("Sorry that card is not in your hand, try again");
		}

		// Starting the game, Phase 1
		System.out.println("Choose a card to play to begin the game!");
		boolean win = false;

		while (!win) {
			boolean invalidMove = true;
			String arguments[] = new String[2];
			
			//Visualization of the game
			player2.visualizeAI();
			player1.visualize();
			
			//Debugging ONLY - show all cards
			//player2.visualizeAll();
			//player1.visualizeAll();
			
			//Start Player 1 Turn
			while (invalidMove) { // This whole loop = 1 player turn, if they make a invalid move, they can try again
	
				//player 1 set phase
				if (player1.handSize() > 0) {
					player1.setPhase(0);
				} else if (player1.getFaceUpCount() > 0) {
					player1.setPhase(1);
				} else {
					player1.setPhase(2);
				}
				String input = in.next();

				if (player1.getPhase() == 2) {
					if (Integer.parseInt(input) < player1.getFaceDownCount()) {
						if (player1.getFaceDownCard(Integer.parseInt(input)) >= field.getLastCard()
								|| player1.getFaceDownCard(Integer.parseInt(input)) == 10
								|| player1.getFaceDownCard(Integer.parseInt(input)) == 2) {

							System.out.println("You played: " + player1.getFaceDownCard(Integer.parseInt(input)));
							field.addCard(player1.getFaceDownCard(Integer.parseInt(input)));

							//handle special case card = 2
							if (player1.getFaceDownCard(Integer.parseInt(input)) == 2) {
								if(player1.getFaceDownCount() == 1){
									invalidMove = false;
								}
								else{
									invalidMove = true; //not really invalid, just allows the loop to reiterate using same var
								}
							} else {
								invalidMove = false;
							}
							player1.removeCard(Integer.parseInt(input));
							if(player1.getFaceDownCount() == 0){
								invalidMove = false;
							}
						} else {
							field.addCard(player1.getFaceDownCard(Integer.parseInt(input)));
							System.out.println("You played: " + player1.getFaceDownCard(Integer.parseInt(input)));
							player1.removeCard(Integer.parseInt(input));

							System.out.println("You picked up");
							invalidMove = false;

							while (field.getSize() > 0) {
								int card = field.empty();
								player1.addCard(card);
							}
						}
					} else {
						System.out.println("Invalid move, please select one of the face down cards..");
					}
				}
				else if (input.contains(",")) {
					arguments = input.split(",");
					if (player1.play(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]))
							&& (Integer.parseInt(arguments[0]) >= field.getLastCard())) {
						for (int i = 0; i < Integer.parseInt(arguments[1]); i++) {
							player1.removeCard(Integer.parseInt(arguments[0]));
							field.addCard(Integer.parseInt(arguments[0]));
						}
						System.out.println("You played: " + Integer.parseInt(arguments[0]) + " " + "("
								+ Integer.parseInt(arguments[1]) + ") times");
						// draw
						try {
							while (player1.handSize() < 3 && deck.cardsLeft() > 0) {
								player1.addCard(deck.drawCard());
							}
						} catch (IllegalArgumentException x) {
							System.out.println("Something went wrong");
						}
						invalidMove = false;
					} 
					else {
						System.out.println("invalid selection");
					}
				//Final check, if you play a higher card, continue, if not pick up. Also logic for special cards 10 & 2
				} 
				else if (player1.play(Integer.parseInt(input)) && (Integer.parseInt(input) >= field.getLastCard()
						|| Integer.parseInt(input) == 2 || Integer.parseInt(input) == 10)) {
					player1.removeCard(Integer.parseInt(input));
					field.addCard(Integer.parseInt(input));

					//check for 2, if 2 then reloop
					if (Integer.parseInt(input) != 2) {
						invalidMove = false;
					}

					// draw
					if (player1.handSize() < 3) {
						try {
							player1.addCard(deck.drawCard());
						} catch (IllegalArgumentException e) {
							// System.out.println("No more cards, testing ->"+
						}
					}

					System.out.println("You played: " + Integer.parseInt(input));

				}
				else if(Integer.parseInt(input)==0){ //'strategic' pick up, allows user to pick up even if they can beat it
					System.out.println("You picked up");
					invalidMove = false;

					while (field.getSize() > 0) {
						int card = field.empty();
						player1.addCard(card);
					}
				}
				else if (!player1.play(Integer.parseInt(input))) {
					System.out.println("invalid selection");
				} 
				else {
					System.out.println("You picked up");
					invalidMove = false;

					while (field.getSize() > 0) {
						int card = field.empty();
						player1.addCard(card);
					}
				}
			} // End Player 1 Turn

			// Check for special cases 
			// Four of a kind ~ clear field
			if (field.getSize() > 3) {
				if (field.fourOfAKind()) {
					while (field.getSize() > 0) {
						field.empty();
					}
					System.out.println("4 of a kind! The playing field has been cleared!");
				}
			}
			// Special card 10 ~ clear field
			if (field.getSize() > 0 && field.getLastCard() == 10) {
				while (field.getSize() > 0) {
					field.empty();
				}
				System.out.println("10 cleared the field");
			}
			// End special cases
			
			
		// Begin AI Turn
		boolean AITurn = true;
		
		//check to see if player 1 already won, no need to play this turn if game is already over
		if (player1.handSize() == 0 && player1.getFaceDownCount() == 0 && player1.getFaceUpCount() == 0) {
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
			
			int AIMove = player2.AIplay(field.getLastCard());	//this method returns the 'best' card for ai to play

			//if AIMove = 2, then you need to run previous line again
			if(AIMove == 2){
				field.addCard(AIMove);
				
				// If phase 2, need to remove from the face down deck instead of regular hand, since that one is empty
				//  so have to remove it differently to distinguish
				//  Otherwise, remove normally (regular hand)
				if(player2.getPhase() == 2){
					player2.removeCard(0);	//AI always plays the first (location 0) face down card, no effect on gameplay
				}
				else{
					player2.removeCard(AIMove); 
				}
				System.out.println("The computer played: " + AIMove);
				
				//if this is their last card, ie, no hand / face up / or face down they win, ie do not reloop
				if(player2.handSize() == 0 && player2.getFaceDownCount() == 0){
					AITurn = false;
				}
				else{
				AITurn = true;
				}
			}
			else if (AIMove > 0) {	//Computer play 
				int frequency;
				if(player2.getPhase() == 0){
					frequency = player2.getCardFrequency(AIMove);
				}
				else if(player2.getPhase() == 1){
					frequency = player2.getCardFrequencyP1(AIMove);
				}
				else{
					//this happens in phase 2, face down cards, cannot play multiples of face down bc they are hidden
					//keep an eye out to make sure comp never plays 2 face down at a time, unless it is a 2 of course
					frequency = 1;
				}
				
				if(frequency > 1){
					//multiple card play
					for (int i = 0; i < frequency ; i++) {
						player2.removeCard(AIMove);
						field.addCard(AIMove);
					}
					System.out.println("The computer played: " + AIMove + " " +"("+ frequency +")"+ " times");
				}
				else{
					//regular 1 card play							
					field.addCard(AIMove);
					
					//If it is phase 1, you can remove by passing in value of card, ie 2-14
					//If it is phase 2, you need to pass the location, since face down cards are hidden.
					if(player2.getPhase() == 2){
						player2.removeCard(0);	//AI always plays the first (location 0) face down card, just to simplify design
					}
					else{
						player2.removeCard(AIMove);
					}

					System.out.println("The computer played: " + AIMove);
				}
				AITurn = false;
			} else {	// Computer picks up
				if(player2.getPhase() == 2){
					field.addCard(player2.getFaceDownCard(0));
					System.out.println("The computer played: " + player2.getFaceDownCard(0));
					player2.removeCard(0);
				}
				
				while (field.getSize() > 0) {
					int card = field.empty();
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
				//if something breaks
				System.out.println("Something went wrong");
			}

			// special cases
			// four of a kind clear
			if (field.getSize() > 3) {
				if (field.fourOfAKind()) {
					while (field.getSize() > 0) {
						field.empty();
					}
					System.out.println("4 of a kind!!! the playing field has been cleared! Size:" + field.getSize());
				}
			}
			// special card 10 clear
			if (field.getSize() > 0 && field.getLastCard() == 10) {
				while (field.getSize() > 0) {
					field.empty();
				}
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
}
