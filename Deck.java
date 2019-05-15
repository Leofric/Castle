package Castle;

import java.util.ArrayList;
import java.util.Random;
/**
 * This class creates a deck of cards, stored in an ArrayList upon construction. 
 * 
 * 
 * @author alexberthon
 * @version 1.0
 */
public class Deck {
	public Random rn = new Random();
	private ArrayList<Integer> deck = new ArrayList<Integer>();

	public Deck(){
		int value = 2;
		int count = 0;
		for(int i =0; i<52; i++){
			deck.add(value);
			count++;
			if (count == 4){
				value++;
				count = 0;
			}
		}
	}
	
	public int drawCard(){
		int randomIndex = rn.nextInt(Math.abs(deck.size()));
		int drawnCard = deck.get(randomIndex);
		deck.remove(randomIndex);
		
		if(deck.size() == 0){
			System.out.println("--The last card has been drawn!--");
			System.out.println(" ");
		}
		
		return drawnCard;
		}
	
	public int cardsLeft(){
		return deck.size();
	}
}
