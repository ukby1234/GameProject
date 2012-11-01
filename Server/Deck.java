
import java.util.*;
public class Deck 
{
	private ArrayList<Card> my_Card = new ArrayList<Card>();
	private int my_CurrentPosition;
	public Deck()
	{
		for (int i = 1; i <= 4; i++)
			for (int j = 1; j <= 13; j++)
				my_Card.add(new Card(j, i));
		my_CurrentPosition = -1;
		Shuffle();
	}
	
	
	public void Shuffle()  //Shuffle the cards by randomly remove the organized cards
	{
		Random generator = new Random();
		ArrayList<Card> Shuffled = new ArrayList<Card>();
		int n = my_Card.size();
		while(!my_Card.isEmpty())
		{
			Shuffled.add(my_Card.remove(generator.nextInt(n)));
			n--;
		}
		my_Card = Shuffled;
	}
	
	public Card Deal() //Deal cards to the player
	{
		my_CurrentPosition++; 
		return my_Card.get(my_CurrentPosition);
	}
	
	public boolean isFull()
	{
		if (my_Card.size() - my_CurrentPosition < 6)
			return true;
		return false;
	}
	
}
