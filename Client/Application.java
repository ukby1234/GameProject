

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;

public class Application extends JFrame implements ActionListener
{
	private JMenuBar menubar= new JMenuBar();
	private JMenu GameMenu = new JMenu("Game");
	private JMenu PlayerMenu = new JMenu("Player");
	public JMenuItem StartGame = new JMenuItem("Start Game");
	public JMenuItem StopGame = new JMenuItem("Stop Game");
	public JMenuItem ExitApp = new JMenuItem("Exit Application");
	public JMenuItem ListPlayers = new JMenuItem("High Scores");
	public JMenuItem CreatePlayers = new JMenuItem("Create Players");
	private CardLayout cl = new CardLayout();
	private CreatePlayerPanel cp; 
	private HighScorePanel hsp;
	private SelectPlayerPanel spp;
	private GamePanel gp;
	private ArrayList<Player> players;
	public Socket s;
	public JPanel panel = new JPanel();
	public ObjectInputStream oin;
	public ObjectOutputStream oou;
	@SuppressWarnings("unchecked")
	public Application()
	{ 
		try
		{
			s = new Socket("aludra.usc.edu", 3500); //connect to the server
			oou = new ObjectOutputStream(s.getOutputStream());
			oin = new ObjectInputStream(s.getInputStream());
			oou.writeObject(new String("GetPlayers"));
			oou.flush();
			players = (ArrayList<Player>)oin.readObject();
		} catch (Exception e) 
		{
			players = new ArrayList<Player>();
		}
		gp = new GamePanel(this, cl);
		cp = new CreatePlayerPanel(this, cl);
		hsp = new HighScorePanel(this, cl);
		spp = new SelectPlayerPanel(this, cl);
		setSize(800, 800);
		StartGame.addActionListener(this);
		StopGame.addActionListener(this);
		ExitApp.addActionListener(this);
		ListPlayers.addActionListener(this);
		CreatePlayers.addActionListener(this);
		GameMenu.add(StartGame);
		GameMenu.add(StopGame);
		GameMenu.add(ExitApp);
		PlayerMenu.add(CreatePlayers);
		PlayerMenu.add(ListPlayers);
		menubar.add(GameMenu);
		menubar.add(PlayerMenu);
		this.setJMenuBar(menubar);
		panel.setLayout(cl);
		panel.add(spp, "SelectPlayers");
		panel.add(cp, "CreatePlayers");
		panel.add(hsp, "HighScores");
		panel.add(gp, "GamePanel");
		this.add(panel);
	}
	
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				Application frame = new Application();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setTitle("Poker");
				frame.setVisible(true);
			}
		});
	}
	
	/*public void WritePlayer()
	{
		try
		{
			oou.writeObject(players);
			oou.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/

	@Override
	public void actionPerformed(ActionEvent e) //deal with the menuitems
	{
		if (e.getSource() == StartGame)
		{
			spp.readPlayers();
			spp.DrawPanel();
			spp.revalidate();
			spp.repaint();
			cl.show(panel, "SelectPlayers");
		}
		if (e.getSource() == StopGame)
		{
			spp.readPlayers();
			spp.DrawPanel();
			spp.revalidate();
			spp.repaint();
			cl.show(panel, "SelectPlayers"); 
		}
		if (e.getSource() == ExitApp)
		{
			System.exit(0); 
		}
		if (e.getSource() == ListPlayers)
		{
			hsp.readPlayers();
			hsp.DrawPanel();
			hsp.revalidate();
			hsp.repaint();
			cl.show(panel, "HighScores");
		}
		if (e.getSource() == CreatePlayers)
		{
			cl.show(panel, "CreatePlayers");
		}
	}

}
