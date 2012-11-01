import java.util.*;
import java.net.*;
import java.io.*;
public class GameServer 
{

	static ServerSocket ss = null;
	static ArrayList<Socket> s = new ArrayList<Socket>();
	static ArrayList<Thread> t = new ArrayList<Thread>();
	ArrayList<Player> players = new ArrayList<Player>();
	
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	
	public static void main(String[] args) throws IOException 
	{
		try
		{
			ss = new ServerSocket(3500); //establish the serversocket
		}catch(Exception e)
		{
			System.out.println("Port in use!");
		}
		Scanner in = new Scanner(System.in);
		System.out.println("How many players?");
		int n = -1;
		while (true)
		{
			try
			{
				n = in.nextInt();
			}catch(Exception e)
			{
			}
			if (n >= 2 && n <= 4)
				break;
		}
		for (int i = 0; i < n; i++)
		{
			s.add(ss.accept());
			ServerRunnable r = new ServerRunnable(i, s.get(i), n);
			t.add(new Thread(r));
			t.get(i).start();
		}
	}
}

