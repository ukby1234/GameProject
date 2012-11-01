import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import java.util.*;
public class CreatePlayerPanel extends JPanel implements ActionListener
{
	JButton SubmitButton = new JButton("Submit");
	JTextField NameField = new JTextField();
	JLabel NameLabel = new JLabel("Please enter your name: ");
	GridBagConstraints gbc = new GridBagConstraints();
	Application frame;
	CardLayout cl;
	public CreatePlayerPanel(Application frame, CardLayout cl)
	{
		this.frame = frame;
		this.cl = cl;
		setLayout(new GridBagLayout());
		SubmitButton.addActionListener(this);
		NameField.setPreferredSize(new Dimension(150, 50));
		NameField.setMaximumSize(new Dimension(150, 50));
		NameField.setMinimumSize(new Dimension(150, 50));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 50;
		gbc.gridwidth = 100;
		gbc.insets = new Insets(50, 0, 0, 50);
		add(NameLabel, gbc);
		gbc.gridy = 55;
		add(NameField, gbc);
		gbc.gridy = 105;
		add(SubmitButton, gbc);
	}

	public void actionPerformed(ActionEvent ae) 
	{
		if (ae.getSource() == SubmitButton)
		{
			if (NameField.getText().compareTo("") != 0)
			{
				Player p = new Player(NameField.getText());
				try
				{
					frame.oou.writeObject(new String("Create")); //send signal to the server
					frame.oou.writeObject(p); //create player object
					HighScorePanel gp = (HighScorePanel) frame.panel.getComponent(2);
					SelectPlayerPanel spp = (SelectPlayerPanel) frame.panel.getComponent(0);
					gp.readPlayers();
					gp.DrawPanel();
					gp.revalidate();
					gp.repaint();
					spp.readPlayers();
					spp.DrawPanel();
					spp.revalidate();
					spp.repaint();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			//	SelectPlayerPanel p = (SelectPlayerPanel)frame.panel.getComponent(0);
			//	p.DrawPanel();
			//	p.revalidate();
			//	p.repaint();
			//	cl.show(frame.panel, "SelectPlayers");
			}
		}
		
	}
}
