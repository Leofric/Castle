package Castle;

import java.util.ArrayList;

public class Player {
	private int phase;
	private int faceDownCount;
	private int faceUpCount;
	private ArrayList<Integer> faceDown;
	private ArrayList<Integer> faceUp;
	private ArrayList<Integer> playersHand;

	public Player() {
		phase = 0;
		faceDownCount = 4;
		faceUpCount = 4;
		faceDown = new ArrayList<Integer>();
		faceUp = new ArrayList<Integer>();
		playersHand = new ArrayList<Integer>();
	}

	//method adds input card to player hand
	public void addCard(int card) {
		playersHand.add(card);
	}

	//method adds a card from player hand to face up array
	public boolean addFaceUp(int input) {
		boolean found = false;
		
		for (int i = 0; i < playersHand.size(); i++) {
			if (playersHand.get(i) == input) {
				found = true;
				playersHand.remove(i);
				faceUp.add(input);
				break;
			}
		}
		return found;
	}

	//method adds input card to player face down array
	public void addFaceDown(int card) {
		faceDown.add(card);
	}

	//method prints the cards in the players hand to the console
	public void displayHand() {		
		System.out.print("Hand: ");
		for (int i = 0; i < playersHand.size(); i++) {
			if(playersHand.get(i) == 11){
				System.out.print("[J] ");
			}
			else if(playersHand.get(i) == 12){
				System.out.print("[Q] ");
			}
			else if(playersHand.get(i) == 13){
				System.out.print("[K] ");
			}
			else if(playersHand.get(i) == 14){
				System.out.print("[A] ");
			}
			else{
				System.out.print("["+playersHand.get(i)+"] ");
			}
		}
		System.out.print("\n");
	}

	// chooses the AI players face up cards based on their hand
	public void AIFaceUp() {
		int value = 14;
		int faceUpCount = 0;		
		
		while (faceUpCount < 4) {
			for (int i = 0; i < playersHand.size(); i++) {
				if (playersHand.get(i) == 2 || playersHand.get(i) == 10) {
					faceUp.add(playersHand.get(i));
					playersHand.remove(i);
					i--;
					faceUpCount++;
				}
				else if (playersHand.get(i) == value) {
					faceUp.add(playersHand.get(i));
					playersHand.remove(i);
					i--;
					faceUpCount++;
				}
				
				//not elegant, but needed to break in certain situations to prevent more than 4 cards being placed
				if(faceUpCount>3){
					i = playersHand.size();
				}
			}
			value--;
		}
	}
	
	//player - 2 card play
	public boolean play(int card, int frequency) {
		boolean valid = false;
		
		if (phase == 2)
			valid = false;
		else if (phase == 1 && (this.getCardFrequencyFaceup(card)>= frequency))
			valid = true;
		else if(this.getCardFrequency(card)>= frequency)
			valid = true;
		else
			valid = false;
		return valid;
	}

	//normal player - 1 card play
	public boolean play(int card) {
		boolean valid = false;
			
		if((phase == 1) && (this.getCardFrequencyFaceup(card) > 0))
			valid = true;
		else if(this.getCardFrequency(card) > 0)
			valid = true;
		else
			valid = false;	
		return valid;
	}
	
	//chooses 'best' card for the AI to play based on situation
	public int AIplay(int lastCard) {
		int bestCard = 0;
		boolean has2 = false;
		boolean has10 = false;
		ArrayList<Integer> higherCards = new ArrayList<Integer>();
		if (this.phase == 1) {
			for (int i = 0; i < faceUpCount; i++) {
				if (faceUp.get(i) >= lastCard && faceUp.get(i) != 2 && faceUp.get(i) != 10) {
					higherCards.add(faceUp.get(i));
					bestCard = faceUp.get(i);
				}
				if(faceUp.get(i) == 2){
					has2 = true;
				}
				if(faceUp.get(i) == 10){
					has10 = true;
				}
			}

			for (int j = 0; j < higherCards.size(); j++) {
				if (higherCards.get(j) < bestCard) {
					bestCard = higherCards.get(j);
				}
			}
			
			//if no moves but has a 10, play the 10
			if(bestCard == 0 && has10){
				bestCard = 10;
			}//if no moves and no 10 but has a 2, play the 2
			else if(bestCard == 0 && has2){
				bestCard = 2;
			}//otherwise no moves, pickup
			
		} else if (phase == 2) {
			bestCard = faceDown.get(0);
			if(bestCard < lastCard && bestCard != 10 && bestCard != 2){
				bestCard = 0;
			}
		} else {
			for (int i = 0; i < playersHand.size(); i++) { //iterate thru hand
				if (playersHand.get(i) >= lastCard && playersHand.get(i) != 2 && playersHand.get(i) != 10){
					higherCards.add(playersHand.get(i));
					bestCard = playersHand.get(i);
				}
				if(playersHand.get(i) == 2){
					has2 = true;
				}
				if(playersHand.get(i) == 10){
					has10 = true;
				}
			}
			
			for (int j = 0; j < higherCards.size(); j++) {
				if (higherCards.get(j) < bestCard) {
					bestCard = higherCards.get(j);
				}
			}
			
			//if no moves but has a 10, play the 10
			if(bestCard == 0 && has10){
				bestCard = 10;
			}//if no moves and no 10 but has a 2, play the 2
			else if(bestCard == 0 && has2){
				bestCard = 2;
			}//otherwise no moves, pickup

		}
		return bestCard;
	}

	//method removes card from the correct location based on the phase
	public void removeCard(int card) {
		if (phase == 2) {
			faceDown.remove(card);
			faceDownCount--;
		}
		if (phase == 1) {
			for (int i = 0; i < faceUp.size(); i++) {
				if (faceUp.get(i) == card) {
					faceUp.remove(i);
					faceUpCount--;
					break;
				}
			}
		}
		for (int i = 0; i < playersHand.size(); i++) {
			if (playersHand.get(i) == card) {
				playersHand.remove(i);
				break;
			}
		}
	}
	
	//returns the size of the player hand
	public int handSize() {
		return playersHand.size();
	}

	//controls player phase
	public void setPhase(int phase) {
		this.phase = phase;
	}

	//returns number of face down cards left
	public int getFaceDownCount() {
		return this.faceDownCount;
	}

	//returns the number of face up cards left
	public int getFaceUpCount() {
		return this.faceUpCount;
	}

	//returns current player phase
	public int getPhase() {
		return this.phase;
	}

	//return value of face down card at given index
	public int getFaceDownCard(int index) {
		return faceDown.get(index);
	}
	
	//return frequency of input card in player hand
	public int getCardFrequency(int card){
		int frequency = 0;
		
		for (int i = 0; i < playersHand.size(); i++) {
			if (playersHand.get(i) == card){
				frequency++;
			}
		}
		return frequency;
	}
	
	//return frequency of input card in player faceup cards
	public int getCardFrequencyFaceup(int card){
		int frequency = 0;
		
		for (int i = 0; i < faceUp.size(); i++) { //iterate thru hand
			if (faceUp.get(i) == card){
				frequency++;
			}
		}
		return frequency;
	}
	
	//Method prints card data to the console. 
	public void visualize(String user){
		if(user == "Player"){
			System.out.println("** YOU **");
		}
		else if(user == "AI"){
			System.out.println("** CPU **");
		}
		else{
			System.out.println("** TEST **");
		}
		
		System.out.print("   Hand			 ");
		for (int i = 0; i < playersHand.size(); i++) {
			if(user == "AI"){
				System.out.print("[?] ");
			}
			else{
				if(playersHand.get(i) == 11){
					System.out.print("[J] ");
				}
				else if(playersHand.get(i) == 12){
					System.out.print("[Q] ");
				}
				else if(playersHand.get(i) == 13){
					System.out.print("[K] ");
				}
				else if(playersHand.get(i) == 14){
					System.out.print("[A] ");
				}
				else{
					System.out.print("["+playersHand.get(i)+"] ");
				}
			}
		}
		System.out.print("\n");
		
		System.out.print("   Face Up		 ");
		for (int i = 0; i < faceUp.size(); i++) {
			if(faceUp.get(i) == 11){
				System.out.print("[J] ");
			}
			else if(faceUp.get(i) == 12){
				System.out.print("[Q] ");
			}
			else if(faceUp.get(i) == 13){
				System.out.print("[K] ");
			}
			else if(faceUp.get(i) == 14){
				System.out.print("[A] ");
			}
			else{
				System.out.print("["+faceUp.get(i)+"] ");
			}
		}
		System.out.print("\n");

		System.out.print("   Face Down		 ");
		for(int i = 0; i<faceDown.size(); i++){
			if(user == "test"){
				if(faceDown.get(i) == 11){
					System.out.print("[J] ");
				}
				else if(faceDown.get(i) == 12){
					System.out.print("[Q] ");
				}
				else if(faceDown.get(i) == 13){
					System.out.print("[K] ");
				}
				else if(faceDown.get(i) == 14){
					System.out.print("[A] ");
				}
				else{
					System.out.print("["+faceDown.get(i)+"] ");
				}
			}
			else{
				System.out.print("[?] ");
			}
		}
		System.out.print("\n\n");
	}
}
