import java.io.*;
public class Card implements Comparable<Card>, Serializable
{
	private int my_Number;
	private int my_Suit;
	private String my_FileName;
	private boolean FaceUp;
	
	public Card()
	{
		my_Number = 0;
		my_Suit = 0;
		my_FileName = "";
		FaceUp = false;
	}
	
	public Card(int number, int suit)
	{
		my_Number = number;
		my_Suit = suit;
		my_FileName = "cards" + System.getProperty("file.separator"); //Initialize the filename of the card
		if (number == 1)
			my_FileName += "a";
		else if (number == 10)
			my_FileName += "t";
		else if (number == 11)
			my_FileName += "j";
		else if (number == 12)
			my_FileName += "q";
		else if (number == 13)
			my_FileName += "k";
		else
			my_FileName += number;
		if (suit == 1)
			my_FileName += "c";
		else if (suit == 2)
			my_FileName += "d";
		else if (suit == 3)
			my_FileName += "h";
		else
			my_FileName += "s";
		my_FileName += ".gif";
		FaceUp = true;
	}
	
	public String getFileName()
	{
		return my_FileName;
	}
	
	public int getNumber()
	{
		return my_Number;
	}
	
	public int getSuit()
	{
		return my_Suit;
	}
	
	public int compareTo(Card o) //Implements Comparable so that it is sortable
	{
		if (this.my_Number != o.my_Number)
			return this.my_Number - o.my_Number ;
		else
			return this.my_Suit - o.my_Suit;
	}	
	
	public void Flip()
	{
		FaceUp = !FaceUp;
	}
}
