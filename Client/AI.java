import java.io.*;
import java.net.*;
import java.util.*;
public class AI 
{

	public static void main(String[] args) throws Exception 
	{
		Socket s = null;
		ObjectOutputStream oou = null;
		ObjectInputStream oin = null;
		ArrayList<Player> players = null;
		try
		{
			s = new Socket("localhost", 3500);
			oou = new ObjectOutputStream(s.getOutputStream());
			oin = new ObjectInputStream(s.getInputStream());
			oou.writeObject(new String("GetPlayers"));
			oou.flush();
			players = (ArrayList<Player>)oin.readObject();
		} catch (Exception e) 
		{
			players = new ArrayList<Player>();
		}
		System.out.println("Which player do you want to use? ");
		for (int i = 0; i < players.size(); i++)
			System.out.println((i+1) + ". " + players.get(i).getName());
		System.out.println((players.size() + 1) + ". Create a new player");
		Scanner in = new Scanner(System.in);
		int option = -1;
		while (true)
		{
			try
			{
				option = in.nextInt();
			}catch(Exception e)
			{}
			if (option > 0 && option <= players.size() + 1)
				break;
		}
		Player p = null;
		if (option == players.size() + 1)
		{
			System.out.println("Enter players name: ");
			String name = in.next();
			oou.writeObject(new String("Create"));
			p = new Player(name);
			oou.writeObject(p);
			oou.writeObject(new String("GetPlayers"));
			oou.flush();
			players = (ArrayList<Player>)oin.readObject();
			for (int i = 0; i < players.size(); i++)
				if (players.get(i).getName().compareTo(name) == 0)
				{
					oou.writeObject(new String("Ready"));
					oou.writeInt(i);
					oou.flush();
					p = (Player) oin.readObject();
					break;
				}
		}
		else
		{
			oou.writeObject(new String("Ready"));
			oou.writeInt(option - 1);
			oou.flush();
			p = (Player) oin.readObject();
			System.out.println(p.getName());
		}
		
		int status = -1;
		double original = p.getChips() + 0.0;
		int PlayerNumber = -1;
		int GameStatus = -1;
		oou.writeObject(new String("PlayerNumber"));
		PlayerNumber = oin.readInt();
		boolean flag = true;
		while (true)
		{
			oou.writeObject(new String("Status"));
			status = oin.readInt();
			String tmp;
			if (status == 1)
			{
				tmp = (String)oin.readObject();
				if (tmp.compareTo(p.getName()) == 0 && flag)
				{
					System.out.println("You Win!");
					flag = false;
				}
				else if (tmp.compareTo(p.getName()) != 0 && flag)
				{
					System.out.println("You Lose!");
					flag = false;
				}
			}
			if (status == 0)
			{
				oou.writeObject(new String("GameStatus"));
				GameStatus = oin.readInt();
				if (PlayerNumber == 0 && GameStatus == 0) //You are the first player. Bet something
				{
					oou.writeObject(new String("Bet"));
					int bet = (int) (p.getChips() * 0.1);
					oou.writeInt(bet);
					oou.flush();
					System.out.println("Bet " +  bet);
					p = (Player) oin.readObject();
				}
				
				if (GameStatus == 0 && p.getChips()/original >= 0.5) //You still have money to bet
				{
					oou.writeObject(new String("Bet"));
					int bet = (int) (p.getChips() * 0.1);
					oou.writeInt(bet);
					oou.flush();
					System.out.println("Bet " +  bet);
					p = (Player) oin.readObject();
				}
				
				if (GameStatus == 0 && p.getChips()/original < 0.5) //You better not bet
				{
					oou.writeObject(new String("Check"));
					oou.flush();
					System.out.println("Check");
				}
				
				if (GameStatus == 1 && p.getChips()/original >= 0.7) //More money to raise
				{
					oou.writeObject(new String("Raise"));
					int bet = (int) (p.getChips() * 0.1);
					oou.writeInt(bet);
					oou.flush();
					System.out.println("Raise " +  bet);
					p = (Player) oin.readObject();
				}
				else if (GameStatus == 1 && p.getChips()/original >= 0.3) //Not that enough money to raise
				{
					oou.writeObject(new String("Call"));
					oou.flush();
					System.out.println("Call");
					p = (Player) oin.readObject();
				}
				else if (GameStatus == 1 && p.getChips()/original < 0.3) //Low money to fold
				{
					System.out.println("Fold");
					oou.writeObject(new String("Fold"));
					oou.flush();
				}
			}
		}
	}

}
