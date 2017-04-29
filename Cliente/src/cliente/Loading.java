package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Loading extends JFrame {
	private String title;
	private ImageIcon icon;
	private String text;
	private JPanel p;
	JLabel l;
	private Thread t2;
	private String text2;

	public Loading(String title, ImageIcon icon, String text) {
		this.title = title;
		this.icon = icon;
		text2 = text;
		this.text = text;
		this.setSize(150, 100);
		this.setLocationRelativeTo(null);
		// this.setTitle(title);
		p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setBackground(new Color(64,64,64));
		this.setContentPane(p);
		this.setResizable(false);
		l = new JLabel(text, icon, JLabel.CENTER);
		l.setForeground(Color.WHITE);
		p.add(l,BorderLayout.CENTER);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.setVisible(true);
		
	}

	public void changePoints(String points) {
		l.setText(text+points);
	}


}
