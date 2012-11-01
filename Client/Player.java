import java.util.*;
import java.io.*;
public class Player implements Serializable, Comparable<Player>
{
	String my_Name;
	int my_Chips;
	private ArrayList<Card> my_Cards = new ArrayList<Card>();
	
	public Player(String Name)
	{
		my_Name = Name;
		my_Chips = 500;
	}
	
	
	public String getName()
	{
		return my_Name;
	}
	
	public ArrayList<Card> getCards()
	{
		return my_Cards;
	}
	
	public int getChips()
	{
		return my_Chips;
	}
	
	public void BetChips(int chips)
	{	
		if (chips <= my_Chips)
			my_Chips -= chips;
	}
	
	public void GetRewards(int chips)
	{
		if(chips > 0)
			my_Chips += chips;
	}
	
	public void FlipCards()
	{
		for (int i = 0; i < 5; i++)
			this.getCards().get(i).Flip();
	}


	@Override
	public int compareTo(Player arg0) 
	{
		return arg0.getChips() - this.getChips();
	}
	
}
