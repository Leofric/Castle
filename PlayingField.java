package Castle;

import java.util.EmptyStackException;
import java.util.Stack;

public class PlayingField {

	private Stack<Integer> playingField = new Stack<Integer>();

	public PlayingField() {
	}

	public void push(int card){
		playingField.push(card);
	}
	
	public int pop(){
		return playingField.pop();
	}
	
	public boolean isEmpty(){
		return playingField.isEmpty();
	}
	
	public int peek(){
		int lastCard = -1;
		try{
			lastCard = playingField.peek();
		}
		catch(EmptyStackException e){
			lastCard = 0;
		}
		return lastCard;
	}
	
	public void clear(){
		playingField.clear();
	}
	
	public int size(){
		return playingField.size();
	}

	public int get(int index){
		return playingField.get(index);
	}
}
