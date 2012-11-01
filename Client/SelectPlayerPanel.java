import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
public class SelectPlayerPanel extends JPanel implements ActionListener
{
	ArrayList<Player> players;
	int SelectedPlayer = -1;
	ButtonGroup groups = new ButtonGroup();
	ArrayList<JRadioButton> buttons = new ArrayList<JRadioButton>();
	JButton b = new JButton("OK");
	JButton CreatePlayerButton = new JButton("Create Player");
	JPanel RadioButtonPanel = new JPanel();
	JPanel ButtonPanel = new JPanel();
	Application frame;
	CardLayout cl;
	GamePanel gp;
	public SelectPlayerPanel(Application frame, CardLayout cl)
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
			players = (ArrayList<Player>)frame.oin.readObject();
		}catch (Exception e)
		{
			players = new ArrayList<Player>();
		}
	}
	
	public void DrawPanel()
	{
		RadioButtonPanel.removeAll();
		ButtonPanel.removeAll();
		b.removeActionListener(this);
		this.removeAll();
		int n = buttons.size();
		for (int i = 0; i < n; i++)
		{
			groups.remove(buttons.remove(0));
		}
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		RadioButtonPanel.setLayout(new GridLayout(players.size() + 1, 1));
		RadioButtonPanel.add(new JLabel("Player: "));
		for (int i = 0; i < players.size(); i++)
		{
			JRadioButton tmp = new JRadioButton(players.get(i).getName());
			groups.add(tmp);
			tmp.setHorizontalAlignment(JRadioButton.CENTER);
			RadioButtonPanel.add(tmp);
			buttons.add(tmp);
		}
		ButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 280, 0));
		b.addActionListener(this);
		CreatePlayerButton.addActionListener(this);
		ButtonPanel.add(CreatePlayerButton);
		ButtonPanel.add(b);
		add(RadioButtonPanel);
		add(ButtonPanel);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		gp = (GamePanel)frame.panel.getComponent(3);
		if (e.getSource() == b)
		{
			for (int i = 0; i < buttons.size(); i++)
			{
				if (buttons.get(i).isSelected())
				{
						SelectedPlayer = i;
				}
			}
			if (SelectedPlayer != -1)
			{
				try 
				{
					frame.oou.writeObject(new String("Ready"));
					frame.oou.writeInt(SelectedPlayer);
					frame.oou.flush();
					Player p = (Player) frame.oin.readObject();
					gp.setPlayer(p);
					System.out.println();
					//gp;
					//gp.revalidate();
				//	gp.repaint();
				} catch (Exception e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				frame.CreatePlayers.setEnabled(false);
				frame.ListPlayers.setEnabled(false);
				frame.StartGame.setEnabled(false);
				cl.show(frame.panel, "GamePanel");
			}
		}
		
		if (e.getSource() == CreatePlayerButton)
		{
			cl.show(frame.panel, "CreatePlayers");
		}
		/*if (SelectedPlayers.get(0) == SelectedPlayers.get(1))
			System.out.println("Not Valid!");
		System.out.println(SelectedPlayers.get(0).getName() + " " + SelectedPlayers.get(0).getChips());
		System.out.println(SelectedPlayers.get(1).getName() + " " + SelectedPlayers.get(1).getChips());*/
		
	}
}
