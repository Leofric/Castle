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

	public boolean addFaceUp(String input) {
		boolean found = false;
		int card = 0;
		
		try{
			card = Integer.parseInt(input);
		}
		catch(IllegalArgumentException e){
			if(input.equals("J")){
				card = 11;
			}
			else if(input.equals("Q")){
				card = 12;
			}
			else if(input.equals("K")){
				card = 13;
			}
			else if(input.equals("A")){
				card = 14;
			}
			else{
				card = 0;
			}
		}
		finally{
			for (int i = 0; i < playersHand.size(); i++) {
				if (playersHand.get(i) == card) {
					found = true;
					playersHand.remove(i);
					faceUp.add(card);
					break;
				}
			}
		}
		return found;
	}

	public void addFaceDown(int card) {
		faceDown.add(card);
	}

//	//Legacy code, replaced this method when I created the Visualize() method, use this as reference to rewrite that one
//	// can probably delete this afterwards.
//	public void displayFaceUp() {
//		String output = "Your Face-Up Cards: ";
//		for (int i = 0; i < faceUp.size(); i++) {
//			if(faceUp.get(i) == 11){
//				output += "J";
//			}
//			else if(faceUp.get(i) == 12){
//				output += "Q ";
//			}
//			else if(faceUp.get(i) == 13){
//				output += "K ";
//			}
//			else if(faceUp.get(i) == 14){
//				output += "A ";
//			}
//			else{
//				output += faceUp.get(i)+" ";
//			}
//		}
//		System.out.println(output);
//	}

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

//	//Legacy code, replaced this method when I created the Visualize() method; same code pretty much, can probably delete this
//	public void displayFaceDown() {
//		String output = "Your Face-Down Cards: [";
//		for (int i = 0; i < faceDown.size(); i++) {
//			if (i == faceDown.size() - 1) {
//				output += "?]";
//			} else
//				output += "? ";
//		}
//		System.out.println(output);
//	}

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
	//fix here to allow player to play 2 of the same even if they have 3
	public boolean play(int card, int frequency) {
		boolean found = false;
		int count = 0;
		if (phase == 2) {
			found = false; // you cant play 2 face down cards at a time (unless you get lucky with a 2 but that is handled elsewhere)
		} else if (phase == 1) {
			for (int i = 0; i < faceUp.size(); i++) {
				if (faceUp.get(i) == card) {
					count++;
				}
			}
			if (count >= frequency) {
				found = true;
			}
		} else {
			for (int i = 0; i < playersHand.size(); i++) {
				if (playersHand.get(i) == card) {
					count++;
				}
			}
			if (count >= frequency) {
				found = true;
			}
		}
		return found;
	}

	// phase 0, 1, and 2 might not need play for phase 2, i can just do it
	// manually in the gameloop
	public boolean play(int card) {
		boolean found = false;
		if (phase == 1) {
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
	
	//Fixed, but is there a way to condense the code? 
	public void visualize(){
		System.out.println("** YOU **");
		System.out.print("   Hand			 ");
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
			System.out.print("[?] ");
		}
		System.out.print("\n\n");
	}
	
	//Fixed, but is there a way to condense the code? 
	public void visualizeAI(){
		System.out.println("** CPU **");
		System.out.print("   Hand			 ");
		for(int i = 0; i<playersHand.size(); i++){
			System.out.print("[?] ");
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
			System.out.print("[?] ");
		}
		System.out.print("\n\n");
	}

	//Fixed, but is there a way to condense the code? 
	public void visualizeAll(){
		System.out.print("   Hand			 ");
		for (int i = 0; i < playersHand.size(); i++) {
			if(playersHand.get(i) == 11){
				System.out.print("J ");
			}
			else if(playersHand.get(i) == 12){
				System.out.print("Q ");
			}
			else if(playersHand.get(i) == 13){
				System.out.print("K ");
			}
			else if(playersHand.get(i) == 14){
				System.out.print("A ");
			}
			else{
				System.out.print(playersHand.get(i)+" ");
			}
		}
		System.out.print("\n");
		
		System.out.print("   Face Up		 ");
		for (int i = 0; i < faceUp.size(); i++) {
			if(faceUp.get(i) == 11){
				System.out.print("J ");
			}
			else if(faceUp.get(i) == 12){
				System.out.print("Q ");
			}
			else if(faceUp.get(i) == 13){
				System.out.print("K ");
			}
			else if(faceUp.get(i) == 14){
				System.out.print("A ");
			}
			else{
				System.out.print(faceUp.get(i)+" ");
			}
		}
		System.out.print("\n");
	
		System.out.print("   Face Down		 ");
		for (int i = 0; i < faceDown.size(); i++) {
			if(faceDown.get(i) == 11){
				System.out.print("J ");
			}
			else if(faceDown.get(i) == 12){
				System.out.print("Q ");
			}
			else if(faceDown.get(i) == 13){
				System.out.print("K ");
			}
			else if(faceDown.get(i) == 14){
				System.out.print("A ");
			}
			else{
				System.out.print(faceDown.get(i)+" ");
			}
		}
		System.out.print("\n\n");
	}
}