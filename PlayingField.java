package Castle;

import java.util.ArrayList;

public class PlayingField {

	private ArrayList<Integer> playingField = new ArrayList<Integer>();

	public PlayingField() {
	}

	public void addCard(int card) {
		playingField.add(card);
	}

	public int getLastCard() {
		int returns;
		int lastCard = playingField.size();
		if (playingField.size() > 0) {
			returns = playingField.get(lastCard - 1);
		} else
			returns = 0;

		return returns;
	}

	public int empty() {
		int card = playingField.get(0);
		playingField.remove(0);
		return card;

	}

	public int getSize() {
		return playingField.size();
	}

	public void print() {
		for (int i = 0; i < playingField.size(); i++) {
			System.out.println(playingField.get(i));
		}
	}

	public boolean fourOfAKind() {
		boolean clear = false;
			if (playingField.get(playingField.size()-1) == playingField.get(playingField.size()-2)
				&& playingField.get(playingField.size()-1) == playingField.get(playingField.size()-3)
				&& playingField.get(playingField.size()-1) == playingField.get(playingField.size()-4)) 

			{
				clear = true;
			}
		return clear;
	}

}
