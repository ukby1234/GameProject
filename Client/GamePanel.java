import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class GamePanel extends JPanel implements ActionListener
{
	private JLabel player = new JLabel();
	private JLabel chips = new JLabel();
	private JButton button[] = {new JButton("Check"), new JButton("Bet"), new JButton("Call"), new JButton("Raise"), new JButton("Fold")};
	private JPanel CardPanel, ButtonPanel;
	private GridBagConstraints gbc = new GridBagConstraints();
	Application frame;
	CardLayout cl;
	Player p;
	//ArrayList<PlayerData> current;
	public GamePanel(Application frame, CardLayout cl)
	{
		this.frame = frame;
		this.cl = cl;
		CardPanel = new JPanel();
		ButtonPanel = new JPanel();
	}
	
	public void setPlayer(Player p)
	{
		this.p = p;
		NewGame();
	}
	
	public void PaintCards() //maintain the GUI
	{
		final int width = 75; 
		for (int i = 0; i < 5; i++)
		{
			Card c = this.p.getCards().get(i);
			ImageIcon CardImage = new ImageIcon(c.getFileName());
			JLabel card = new JLabel(CardImage);
			card.setPreferredSize(new Dimension(CardImage.getIconWidth(), CardImage.getIconHeight()));
			card.setMinimumSize(new Dimension(CardImage.getIconWidth(), CardImage.getIconHeight()));
			card.setMaximumSize(new Dimension(CardImage.getIconWidth(), CardImage.getIconHeight()));
			gbc.gridx = i * width;
			gbc.gridy = 20;
			gbc.gridheight = CardImage.getIconHeight();
			gbc.gridwidth = CardImage.getIconWidth();
			gbc.insets = new Insets(250, 0, 250, 0);
			card.setVisible(true);
			CardPanel.add(card, gbc);
		}
	}
	
	public void drawButton() //Draw buttons on GUI
	{
		player.setText("Name: " + p.getName());
		player.setPreferredSize(new Dimension(150, 40));
		player.setMinimumSize(new Dimension(150, 40));
		player.setMaximumSize(new Dimension(150, 40));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 50;
		gbc.gridwidth = 50;
		gbc.insets = new Insets(0, 0, 0, 0);
		ButtonPanel.add(player, gbc);
		chips.setText("Total Chips: " + p.getChips());
		chips.setPreferredSize(new Dimension(150, 40));
		chips.setMinimumSize(new Dimension(150, 40));
		chips.setMaximumSize(new Dimension(150, 40));
		gbc.gridy = 51;
		ButtonPanel.add(chips, gbc);
		for (int i = 0; i < button.length; i++)
		{
			button[i].setPreferredSize(new Dimension(150, 50));
			button[i].setMinimumSize(new Dimension(150, 50));
			button[i].setMaximumSize(new Dimension(150, 50));
			gbc.gridx = 0;
			gbc.gridy = 51 * (i + 2);
			gbc.gridheight = 50;
			gbc.gridwidth = 50;
			gbc.insets = new Insets(0, 0, 0, 25);
			button[i].addActionListener(this);
			button[i].setVisible(true);
			ButtonPanel.add(button[i], gbc);
		}
	}
	
	public void NewGame()  //When user choose to start a new game
	{
		CardPanel.removeAll();
		ButtonPanel.removeAll();
		this.removeAll();
		for (int i = 0; i < button.length; i++)
			button[i].removeActionListener(this);
		setLayout(new GridBagLayout());
		CardPanel.setLayout(new GridBagLayout());
		ButtonPanel.setLayout(new GridBagLayout());
		PaintCards();
		drawButton();
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.weightx = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(CardPanel, gbc);
		gbc.weightx = 0.1;
		gbc.gridx = 2;
		gbc.gridy = 0;
		add(ButtonPanel, gbc);
	}
	
	public void actionPerformed(ActionEvent e) //respond action to the button
	//sending signals to the server
	{
		JButton tmp = (JButton)e.getSource();
		
		if(tmp.getText().compareTo("Check") == 0) // Player check
		{
			try 
			{
				frame.oou.writeObject(new String("Check"));
			} catch (Exception e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(tmp.getText().compareTo("Bet") == 0)
		{
			int bet = -1;
			while (bet < 5 || bet > 100) //Make sure the bet is between min and max
			{
				try
				{
					bet = Integer.parseInt(JOptionPane.showInputDialog(this, "Please input a value between 5 and 100"));    
				}
				catch(Exception e1)
				{
				}
			}
			try 
			{
				frame.oou.writeObject(new String("Bet"));
				frame.oou.writeInt(bet);
				frame.oou.flush();
				p = (Player) frame.oin.readObject();
			} catch (Exception e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(tmp.getText().compareTo("Call") == 0)
		{
			try 
			{
				frame.oou.writeObject(new String("Call"));
				frame.oou.flush();
				p = (Player) frame.oin.readObject();
			} catch (Exception e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(tmp.getText().compareTo("Raise") == 0)
		{
			int bet = -1;
			while (bet < 5 || bet > 50) //Make sure the bet is between min and max
			{
				try
				{
					bet = Integer.parseInt(JOptionPane.showInputDialog(this, "Please input a value between 5 and 50"));    
				}
				catch(Exception e1)
				{
				}
			}
			try 
			{
				frame.oou.writeObject(new String("Raise"));
				frame.oou.writeInt(bet);
				frame.oou.flush();
				p = (Player) frame.oin.readObject();
			} catch (Exception e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(tmp.getText().compareTo("Fold") == 0)
		{
			try 
			{
				frame.oou.writeObject(new String("Fold"));
			} catch (Exception e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		chips.setText("Total Chips: " + p.getChips());
		int status = -1;
		try
		{
			frame.oou.writeObject(new String("Status"));
			frame.oou.flush();
			status = frame.oin.readInt();
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		if (status == 1)
		{
			String name = null;
			try 
			{
				 name = (String) frame.oin.readObject();
			} catch (Exception e1) 
			{
				e1.printStackTrace();
			}
			if (name.compareTo(p.getName()) == 0)
				JOptionPane.showMessageDialog(this, "You Win!", "Winner",JOptionPane.ERROR_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, "You Lose!.", "Winner",JOptionPane.ERROR_MESSAGE);
			frame.CreatePlayers.setEnabled(true);
			frame.ListPlayers.setEnabled(true);
			frame.StartGame.setEnabled(true);
			cl.show(frame.panel, "SelectPlayers");
			
		}
	}
}
