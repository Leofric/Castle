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

	public void addCard(int card) {
		playersHand.add(card);
	}

	public boolean addFaceUp(int card) {
		boolean found = false;
		for (int i = 0; i < playersHand.size(); i++) {
			if (playersHand.get(i) == card) {
				found = true;
				playersHand.remove(i);
				faceUp.add(card);
				break;
			}
		}
		if (!found)
			System.out.println("Error: bad input");
		return found;
	}

	public void addFaceDown(int card) {
		faceDown.add(card);
	}

	public void displayFaceUp() {
		String output = "Your Face-Up Cards: [";
		for (int i = 0; i < faceUp.size(); i++) {
			if (i == faceUp.size() - 1) {
				output += faceUp.get(i) + "]";
			} else
				output += faceUp.get(i) + ", ";
		}
		System.out.println(output);
	}

	public void displayHand() {
		System.out.println("Hand: " + playersHand);
	}

	public void displayFaceDown() {
		String output = "Your Face-Down Cards: [";
		for (int i = 0; i < faceDown.size(); i++) {
			if (i == faceDown.size() - 1) {
				output += "?]";
			} else
				output += "? ";
		}
		System.out.println(output);
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
				
				//not elegant, but needed to break in certain situations to prevent more than 4 cards being picked
				if(faceUpCount>3){
					i = playersHand.size();
				}
			}
			value--;
		}
	}
	
	// only phase 0 and 1
	public boolean play(int card, int frequency) {
		boolean found = false;
		int count = 0;
		if (phase == 2) {
			found = false; // you cant play 2 face down cards at a time
		} else if (phase == 1) {
			for (int i = 0; i < faceUp.size(); i++) {
				if (faceUp.get(i) == card) {
					count++;
				}
			}
			if (count == frequency) {
				found = true;
			}
		} else {
			for (int i = 0; i < playersHand.size(); i++) {
				if (playersHand.get(i) == card) {
					count++;
				}
			}
			if (count == frequency) {
				found = true;
			}
		}
		return found;
	}

	// phase 0, 1, and 2 might not need play for phase 2, i can just do it
	// manually in the gameloop
	public boolean play(int card) {
		boolean found = false;
		if (phase == 2) {
			if (card <= faceDownCount) {
				found = true;
			}
		} else if (phase == 1) {
			for (int i = 0; i < faceUp.size(); i++) {
				if (faceUp.get(i) == card) {
					found = true;
				}
			}
		} else {
			for (int i = 0; i < playersHand.size(); i++) {
				if (playersHand.get(i) == card) {
					found = true;
				}
			}
		}
		return found;
	}
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
			
			//if no moves but has a 2, play the 2
			if(bestCard == 0 && has10){
				bestCard = 10;
			}//if no moves and no 2 but has a 10, play the 10
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
			
			//if no moves but has a 2, play the 2
			if(bestCard == 0 && has2){
				bestCard = 2;
			}//if no moves and no 2 but has a 10, play the 10
			else if(bestCard == 0 && has10){
				bestCard = 10;
			}//otherwise no moves, pickup

		}
		return bestCard;
	}

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

	public int handSize() {
		return playersHand.size();
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public int getFaceDownCount() {
		return this.faceDownCount;
	}

	public int getFaceUpCount() {
		return this.faceUpCount;
	}

	public int getPhase() {
		return this.phase;
	}

	public int getFaceDownCard(int index) {
		return faceDown.get(index);
	}
	
	public int getCardFrequency(int card){
		int frequency = 0;
		
		for (int i = 0; i < playersHand.size(); i++) { //iterate thru hand
			if (playersHand.get(i) == card){
				frequency++;
			}
		}
		
		return frequency;
	}
	
	public int getCardFrequencyP1(int card){
		int frequency = 0;
		
		for (int i = 0; i < faceUp.size(); i++) { //iterate thru hand
			if (faceUp.get(i) == card){
				frequency++;
			}
		}
		
		return frequency;
	}
	
	//an attempt at making a UI for a console based card game
	public void visualize(){
		System.out.println("** YOU **");
		System.out.print("   Hand			[");
		for(int i = 0; i<playersHand.size(); i++){
			if(i == playersHand.size()-1){
				System.out.print(playersHand.get(i)+"]");
			}
			else{
				System.out.print(playersHand.get(i)+", ");
			}
		}
		System.out.print("\n");
		
		System.out.print("   Face Up		[");
		for(int i = 0; i<faceUp.size(); i++){
			if(i == faceUp.size()-1){
				System.out.print(faceUp.get(i)+"]");
			}
			else{
				System.out.print(faceUp.get(i)+", ");
			}
		}
		System.out.print("\n");

		System.out.print("   Face Down		[");
		for(int i = 0; i<faceDown.size(); i++){
			if(i == faceDown.size()-1){
				System.out.print("?]");
			}
			else{
				System.out.print("?, ");
			}
		}
		System.out.print("\n\n");
	}
	
	public void visualizeAI(){
		System.out.println("** CPU **");
		System.out.print("   Hand			[");
		for(int i = 0; i<playersHand.size(); i++){
			if(i == playersHand.size()-1){
				System.out.print("?]");
			}
			else{
				System.out.print("?, ");
			}
		}
		System.out.print("\n");

		System.out.print("   Face Up		[");
		for(int i = 0; i<faceUp.size(); i++){
			if(i == faceUp.size()-1){
				System.out.print(faceUp.get(i)+"]");
			}
			else{
				System.out.print(faceUp.get(i)+", ");	
			}
		}
		System.out.print("\n");
		
		System.out.print("   Face Down		[");
		for(int i = 0; i<faceDown.size(); i++){
			if(i == faceDown.size()-1){
				System.out.print("?]");
			}
			else{
				System.out.print("?, ");	
			}
		}
		System.out.print("\n\n");
	}

	//For Debugging ONLY show all cards including face down
	public void visualizeAll(){
		System.out.println("** YOU **");
		System.out.print("   Hand			[");
		for(int i = 0; i<playersHand.size(); i++){
			if(i == playersHand.size()-1){
				System.out.print(playersHand.get(i)+"]");
			}
			else{
				System.out.print(playersHand.get(i)+", ");
			}
		}
		System.out.print("\n");
		
		System.out.print("   Face Up		[");
		for(int i = 0; i<faceUp.size(); i++){
			if(i == faceUp.size()-1){
				System.out.print(faceUp.get(i)+"]");
			}
			else{
				System.out.print(faceUp.get(i)+", ");
			}
		}
		System.out.print("\n");
	
		System.out.print("   Face Down		[");
		for(int i = 0; i<faceDown.size(); i++){
			if(i == faceDown.size()-1){
				System.out.print(faceDown.get(i)+"]");
			}
			else{
				System.out.print(faceDown.get(i)+", ");
			}
		}
		System.out.print("\n\n");
	}
}