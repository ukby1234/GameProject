import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
public class HighScorePanel extends JPanel
{
	ArrayList<Player> players;
	JPanel LabelPanel = new JPanel();
	JPanel ScorePanel = new JPanel();
	Application frame;
	CardLayout cl;
	public HighScorePanel(Application frame, CardLayout cl)
	{
		this.frame = frame;
		this.cl = cl;
		readPlayers();
		DrawPanel();
	}
	
	public void readPlayers()
	{
		try
		{
			frame.oou.writeObject(new String("GetPlayers"));
			frame.oou.flush();
			players = (ArrayList<Player>)frame.oin.readObject(); //read players from the server
		}catch (Exception e)
		{
			players = new ArrayList<Player>();
		}
	}
	public void DrawPanel()
	{
		LabelPanel.removeAll();
		ScorePanel.removeAll();
		this.removeAll();
		if (!players.isEmpty())
			Collections.sort(players);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		ScorePanel.setLayout(new GridLayout(players.size(), 3));
		LabelPanel.setLayout(new FlowLayout());
		LabelPanel.add(new JLabel("HighScores: "));
		LabelPanel.setPreferredSize(new Dimension(800, 50));
		LabelPanel.setMaximumSize(new Dimension(800, 50));
		LabelPanel.setMinimumSize(new Dimension(800, 50));
		for (int i = 0;i < players.size(); i++) //arrange the GUI
		{
			JLabel OrderLabel = new JLabel("" + (i + 1));
			JLabel NameLabel = new JLabel(players.get(i).getName());
			JLabel ChipsLabel = new JLabel("" + players.get(i).getChips());
			OrderLabel.setHorizontalAlignment(JLabel.CENTER);
			NameLabel.setHorizontalAlignment(JLabel.CENTER);
			ChipsLabel.setHorizontalAlignment(JLabel.CENTER);
			ScorePanel.add(OrderLabel);
			ScorePanel.add(NameLabel);
			ScorePanel.add(ChipsLabel);
		}
		add(LabelPanel);
		add(ScorePanel);
	}
}
