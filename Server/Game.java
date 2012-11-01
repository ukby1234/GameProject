import java.util.*;



public class Game
{
	private final int PlayerNumber = 2;
	//below is to determine the type of player's cards
	public int isFlush(Player p)
	{
		ArrayList<Card> cards = p.getCards();
		for (int i = 0; i < cards.size() - 1; i++)
		{
			if (cards.get(i).getSuit() != cards.get(i + 1).getSuit())
				return -1;
		}
		return cards.get(cards.size() - 1).getNumber();
	}
	
	public int isStraight(Player p)
	{
		ArrayList<Card> cards = p.getCards();
		if (cards.get(0).getNumber() == 1 && cards.get(1).getNumber() == 10 && cards.get(2).getNumber() == 11 && cards.get(3).getNumber() == 12 && cards.get(4).getNumber() == 13)
			return 14;
		for (int i = 0; i < cards.size() - 1; i++)
		{
			if (cards.get(i).getNumber() != cards.get(i + 1).getNumber() - 1)
				return -1;
		}
		return cards.get(cards.size() - 1).getNumber();
	}
	
	public int isTwoPair(Player p)
	{
		ArrayList<Card> cards = p.getCards();
		if (cards.get(0).getNumber() == cards.get(1).getNumber() && cards.get(2).getNumber() == cards.get(3).getNumber())
			return cards.get(3).getNumber();
		if (cards.get(0).getNumber() == cards.get(1).getNumber() && cards.get(3).getNumber() == cards.get(4).getNumber())
			return cards.get(4).getNumber();
		if (cards.get(1).getNumber() == cards.get(2).getNumber() && cards.get(3).getNumber() == cards.get(4).getNumber())
			return cards.get(4).getNumber();
		return -1;
	}
	
	public int isPair(Player p)
	{
		ArrayList<Card> cards = p.getCards();
		int max = -1;
		for (int i = 0; i < cards.size() - 1; i++)
		{
			if ((cards.get(i).getNumber() == cards.get(i + 1).getNumber()) && (cards.get(i).getNumber() > max))
				max = cards.get(i).getNumber();
		}
		return max;
	}
	
	public int isFourOfKind(Player p)
	{
		ArrayList<Card> cards = p.getCards();
		if (cards.get(0).getNumber() == cards.get(1).getNumber() && cards.get(1).getNumber() == cards.get(2).getNumber() && cards.get(2).getNumber() == cards.get(3).getNumber())
			return cards.get(3).getNumber();
		if (cards.get(1).getNumber() == cards.get(2).getNumber() && cards.get(2).getNumber() == cards.get(3).getNumber() && cards.get(3).getNumber() == cards.get(4).getNumber())
			return cards.get(4).getNumber();
		return -1;
	}
	
	public int isThreeOfKind(Player p)
	{
		ArrayList<Card> cards = p.getCards();
		if (cards.get(0).getNumber() == cards.get(1).getNumber() && cards.get(1).getNumber() == cards.get(2).getNumber())
			return cards.get(2).getNumber();
		if (cards.get(1).getNumber() == cards.get(2).getNumber() && cards.get(2).getNumber() == cards.get(3).getNumber())
			return cards.get(3).getNumber();
		if (cards.get(2).getNumber() == cards.get(3).getNumber() && cards.get(3).getNumber() == cards.get(4).getNumber() && cards.get(3).getNumber() == cards.get(4).getNumber())
			return cards.get(4).getNumber();
		return -1;
	}
	
	public int isFullHouse(Player p)
	{
		ArrayList<Card> cards = p.getCards();
		if (cards.get(0).getNumber() == cards.get(1).getNumber() && cards.get(1).getNumber() == cards.get(2).getNumber() && cards.get(3).getNumber() == cards.get(4).getNumber())
			return cards.get(0).getNumber();
		if (cards.get(2).getNumber() == cards.get(3).getNumber() && cards.get(3).getNumber() == cards.get(4).getNumber() && cards.get(0).getNumber() == cards.get(1).getNumber())
			return cards.get(2).getNumber();
		return -1;
	}
	
	public int isNothing(Player p)
	{
		ArrayList<Card> cards = p.getCards();
		return cards.get(cards.size() - 1).getNumber();
	}
	
	public int isStraightFlush(Player p)
	{
		if (this.isFlush(p) > 0 && this.isStraight(p) > 0)
			return this.isStraight(p);
		return -1;
	}
	
	public boolean isPlayer1Win(Player a, Player b) //determine who wins the game
	{
		if (isStraightFlush(a) > isStraightFlush(b))
			return true;
		if (isStraightFlush(a) < isStraightFlush(b))
			return false;
		if (isFourOfKind(a) > isFourOfKind(b))
			return true;
		if (isFourOfKind(a) < isFourOfKind(b))
			return false;
		if (isFullHouse(a) > isFullHouse(b))
			return true;
		if (isFullHouse(a) < isFullHouse(b))
			return false;
		if (isFlush(a) > isFlush(b))
			return true;
		if (isFlush(a) < isFlush(b))
			return false;
		if (isStraight(a) > isStraight(b))
			return true;
		if (isStraight(a) < isStraight(b))
			return false;
		if (isThreeOfKind(a) > isThreeOfKind(b))
			return true;
		if (isThreeOfKind(a) < isThreeOfKind(b))
			return false;
		if (isTwoPair(a) > isTwoPair(b))
			return true;
		if (isTwoPair(a) < isTwoPair(b))
			return false;
		if (isPair(a) > isPair(b))
			return true;
		if (isPair(a) < isPair(b))
			return false;
		if (isNothing(a) > isNothing(b))
			return true;
		if (isNothing(a) < isNothing(b))
			return false;
		return false;
	}
	public int returnWin(ArrayList<Player> p) //determine who wins
	{
		if (p.size() == 2)
			if (isPlayer1Win(p.get(0), p.get(1)))
				return 0;
			else
				return 1;
		else if (p.size() == 3)
		{
			int t = -1;
			if (isPlayer1Win(p.get(0), p.get(1)))
				t = 0;
			else
				t = 1;
			if (isPlayer1Win(p.get(t), p.get(2)))
				return t;
			else
				return 2;
		}
		else if (p.size() == 4)
		{
			int t1 = -1; 
			int t2 = -1;
			if (isPlayer1Win(p.get(0), p.get(1)))
				t1 = 0;
			else
				t1 = 1;
			if (isPlayer1Win(p.get(2), p.get(3)))
				t2 = 2;
			else
				t2 = 3;
			if (isPlayer1Win(p.get(t1), p.get(t2)))
				return t1;
			else
				return t2;
		}
		return -1;
	}
}
