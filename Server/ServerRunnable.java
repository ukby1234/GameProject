import java.io.*;
import java.net.*;
import java.util.*;

public class ServerRunnable implements Runnable
{
	int PlayerNumber;
	Socket s;
	ArrayList<Player> players;
	static int status = 2;
	static boolean flag = true;
	int PlayerPosition = -1;
	static int PlayerCount = 0;
	static int CurrentPlayerCount = 0;
	static ArrayList<Integer> CurrentPlayers = new ArrayList<Integer>();
	static ArrayList<Boolean> GameStarted = new ArrayList<Boolean>();
	static ArrayList<Integer> AmountPlayerBet = new ArrayList<Integer>();
	static ArrayList<String> Choice = new ArrayList<String>();
	static Deck d = new Deck();
	static Game g = new Game();
	static boolean NoBet = true;
	static int lastBet = -1;
	static int pool = 0;
	static int NumberOfPlayer;
	static String Winner = "";
	boolean f = false;
	ServerRunnable(int i, Socket s, int n)
	{
		this.PlayerNumber = i; //initialize the thread for each player
		this.s = s;
		NumberOfPlayer = n;
		GameStarted.add(false);
		AmountPlayerBet.add(0);
		Choice.add("");
		CurrentPlayers.add(-1);
	}
	
	public int CalculateGap(int i) //if someone folds, this player must be skipped
	{
		int s = 0;
		int j = i + s + 1;
		if (i == NumberOfPlayer - 1)
			j = 0;
		while (j < NumberOfPlayer && !GameStarted.get(j))
		{
			s++;
			j++;
			if (i == NumberOfPlayer - 1)
				j = 0;
		}
		return s + 1;
	}
	public void readPlayers() //read players from file
	{
		try
		{
			FileInputStream file = new FileInputStream( "Player" );
		    ObjectInputStream input = new ObjectInputStream ( file );
		    try
		    {
		        players = (ArrayList<Player>)input.readObject();
		    }
		    finally
		    {
		        input.close();
		        file.close();
		    }
		}
		catch(Exception e)
		{
			players = new ArrayList<Player>();
		}	
	}
	
	public void WritePlayer() //write players to file
	{
		try
		{
			FileOutputStream file = new FileOutputStream( "Player" );
		    ObjectOutputStream output = new ObjectOutputStream (file );
		    try
		    {
		        output.writeObject(players);
		        output.flush();
		    }
		    finally
		    {
		        output.close();
		        file.close();
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isBetEqualAmount() //test the amout bet
	{
		for (int i = 0; i < NumberOfPlayer - 1; i++)
		{
			if (AmountPlayerBet.get(i) != AmountPlayerBet.get(i + 1))
				return false;
		}
		return true;
	}
	
	public boolean isSameChoice(int number) //test the choice
	{
		ArrayList<String> newChoice = new ArrayList<String>();
		for (int i = 0; i < number; i++)
			if (GameStarted.get(i))
				newChoice.add(Choice.get(i));
		for (int i = 0; i < newChoice.size() - 1; i++)
			if (newChoice.get(i).compareTo(newChoice.get(i + 1)) != 0)
				return false;
		return true;
	}
	
	public void newGame() //ready for start a new game
	{
		for (int i = 0; i < NumberOfPlayer; i++)
		{
			CurrentPlayers.set(i, -1);
			GameStarted.set(i, false);
			AmountPlayerBet.set(i, -1);
			Choice.set(i, "");
			NoBet = true;
			lastBet = -1;
			pool = 0;
			status = 2;
			PlayerPosition = -1;
			PlayerCount = 0;
			CurrentPlayerCount = 0;
			f = false;
		}
	}
	@SuppressWarnings("unchecked")
	public void run()
	{
		ObjectInputStream oin = null;
		ObjectOutputStream oou = null;
		try
		{
			oou = new ObjectOutputStream(s.getOutputStream());
			oin = new ObjectInputStream(s.getInputStream());
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		while (true)
		{
			String option = ""; 
			try
			{
				option = (String) oin.readObject();
			} catch (Exception e) 
			{
				System.exit(0);
			}
			if (!GameStarted.get(PlayerNumber) && option.compareTo("Create") == 0)
			{
				try 
				{
					Player p = (Player)oin.readObject();
					players.add(p);
					System.out.println("Readed!");
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
				WritePlayer();
				System.out.println("Writed");
				
			}
			if (!GameStarted.get(PlayerNumber) && option.compareTo("GetPlayers") == 0)
			{
				try 
				{
					readPlayers();
					oou.writeObject(players);
					oou.flush();
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			if (option.compareTo("Ready") == 0)
			{
				System.out.println("Tested");
				try 
				{
					readPlayers();
					PlayerPosition = oin.readInt();
					System.out.println("R");
					if (!GameStarted.get(PlayerNumber))
					{
						flag = true;
						Winner = "";
						CurrentPlayers.set(PlayerNumber, PlayerPosition);
						Player p = players.get(PlayerPosition);
						p.getCards().removeAll(p.getCards());
						for (int i = 0; i < 5; i++)
							p.getCards().add(d.Deal());
						Collections.sort(p.getCards()); //give players cards
						System.out.println("Before Write");
						WritePlayer();
						readPlayers();
						oou.writeObject(players.get(PlayerPosition));
						oou.flush();
						System.out.println("After Write");
						PlayerCount++;
						System.out.println(p.getName());
						GameStarted.set(PlayerNumber, true);
					}
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (option.compareTo("Status") == 0) //determine whether the player in in the current round
			{
				readPlayers();
				try
				{
					oou.reset();
					if (flag)
					{
						if (PlayerNumber == CurrentPlayerCount % NumberOfPlayer)
							oou.writeInt(0);
						else
							oou.writeInt(2);
					}	
					else
					{
						oou.writeInt(1);
						oou.flush();
						oou.writeObject(Winner);
					}
					oou.flush();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			if (option.compareTo("PlayerNumber") == 0)
			{
				readPlayers();
				try
				{
					oou.writeInt(PlayerNumber);
					oou.flush();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			if (option.compareTo("GameStatus") == 0)
			{
				readPlayers();
				try
				{
					oou.reset();
					if (!isBetEqualAmount() || !NoBet)
						oou.writeInt(1);
					else
						oou.writeInt(0);
					oou.flush();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			if (PlayerCount == NumberOfPlayer && GameStarted.get(PlayerNumber))
			{
				System.out.println(PlayerNumber);
				System.out.println(CurrentPlayerCount % NumberOfPlayer);
				readPlayers();
				if (PlayerNumber == CurrentPlayerCount % NumberOfPlayer && flag)
				{
					if (option.compareTo("Fold") == 0) //deal with fold
					{
						System.out.println("Executed!");
						if (!isBetEqualAmount() || !NoBet)
						{
							GameStarted.set(PlayerNumber, false);
							f = true; //deal with race condition for AI
							//CurrentPlayerCount += CalculateGap(PlayerNumber);
						}
						int count = 0, pos = -1;
						for (int i = 0; i < NumberOfPlayer; i++)
							if (GameStarted.get(i) == true)
							{
								count++;
								pos = i;
							}
						if (count == 1)
						{
							Winner = players.get(CurrentPlayers.get(pos)).getName();
							System.out.println(players.get(CurrentPlayers.get(pos)).getName() + "Win!");
							players.get(CurrentPlayers.get(pos)).GetRewards(pool);
							WritePlayer();
							readPlayers();
							status = 1;
							flag = false;
						}
						Choice.set(PlayerNumber, "F");
					}
					
					if (option.compareTo("Check") == 0 && isBetEqualAmount() && NoBet)
					{
						//CurrentPlayerCount++;
						//CurrentPlayerCount += CalculateGap(PlayerNumber);
						f = true;
						Choice.set(PlayerNumber, "C");
					}
					
					if (option.compareTo("Call") == 0)
					{
						System.out.println("Called");
						if (!isBetEqualAmount() || !NoBet)
						{
							if (players.get(PlayerPosition).getChips() >= lastBet)
							{
								players.get(PlayerPosition).BetChips(lastBet);
								AmountPlayerBet.set(PlayerNumber, AmountPlayerBet.get(PlayerNumber) + lastBet);
								pool += lastBet;
							}
							else
							{
								int bet = players.get(PlayerPosition).getChips();
								players.get(PlayerPosition).BetChips(bet);
								AmountPlayerBet.set(PlayerNumber, AmountPlayerBet.get(PlayerNumber) + bet);
								pool += bet;
							}
							//CurrentPlayerCount++;
							//CurrentPlayerCount += CalculateGap(PlayerNumber);
							f = true;
							Choice.set(PlayerNumber, "A");
						}
						WritePlayer();
						readPlayers();
						try
						{
							oou.writeObject(players.get(PlayerPosition));
						}catch(Exception e1)
						{
							e1.printStackTrace();
						}
					}
					if (option.compareTo("Raise") == 0)
					{
						int bet = -1;
						try 
						{
							bet = oin.readInt();
						} catch (IOException e) 
						{
							e.printStackTrace();
						}
						if (!isBetEqualAmount() || !NoBet)
						{
							players.get(PlayerPosition).BetChips(bet + lastBet);
							lastBet += bet;
							AmountPlayerBet.set(PlayerNumber, AmountPlayerBet.get(PlayerNumber) + lastBet);
							pool += lastBet;
							WritePlayer();
							readPlayers();
							//CurrentPlayerCount++;
							//CurrentPlayerCount += CalculateGap(PlayerNumber);
							f = true;
							Choice.set(PlayerNumber, "R");
						}
						try
						{
							oou.writeObject(players.get(PlayerPosition));
						}catch(Exception e1)
						{
							e1.printStackTrace();
						}
					}
					if (option.compareTo("Bet") == 0)
					{
						int bet = -1;
						try 
						{
							bet = oin.readInt();
						} catch (IOException e) 
						{
							e.printStackTrace();
						}
						if (isBetEqualAmount() && NoBet)
						{
							players.get(PlayerPosition).BetChips(bet);
							lastBet = bet;
							AmountPlayerBet.set(PlayerNumber, AmountPlayerBet.get(PlayerNumber) + bet);
							f = true;
							//CurrentPlayerCount += CalculateGap(PlayerNumber);
							//CurrentPlayerCount++;
							NoBet = false;
							pool += lastBet;
							WritePlayer();
							readPlayers();
							try
							{
								oou.writeObject(players.get(PlayerPosition));
							}catch(Exception e1)
							{
								e1.printStackTrace();
							}
							Choice.set(PlayerNumber, "B");
						}
					}
					if (players.get(PlayerPosition).getChips() <= 0) //make sure everyone have enough chips
						GameStarted.set(PlayerNumber, false);
					
					if (f) //deal with race condition
					{
						CurrentPlayerCount += CalculateGap(PlayerNumber);
						f = false;
					}
					
					if (CurrentPlayerCount % NumberOfPlayer== 0) //test for boundary cases
					{
						NoBet = true;
						if (isSameChoice(NumberOfPlayer) && (Choice.get(0).compareTo("C") == 0 || Choice.get(0).compareTo("A") == 0))
						{
							ArrayList<Player> p = new ArrayList<Player>();
							for (int i = 0; i < CurrentPlayers.size(); i++)
								p.add(players.get(CurrentPlayers.get(i)));
							int pos = g.returnWin(p);
							Winner = players.get(CurrentPlayers.get(pos)).getName();
							players.get(CurrentPlayers.get(pos)).GetRewards(pool);
							status = 1;
							WritePlayer();
							readPlayers();
							flag = false;
						}
					}
					if(!flag)
						newGame();
				}
				//CurrentPlayerCount++;
			}
		}
	}
}
