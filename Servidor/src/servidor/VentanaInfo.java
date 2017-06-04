package servidor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import comun.Constantes;
import utils.ConstantesServer;

public class VentanaInfo extends JFrame {
	JLabel info = new JLabel("", SwingConstants.CENTER);
	JLabel info2 = new JLabel("", SwingConstants.CENTER);
	JLabel info3 = new JLabel("", SwingConstants.CENTER);
	JLabel info4 = new JLabel("", SwingConstants.CENTER);
	JLabel info5 = new JLabel("", SwingConstants.CENTER);
	JLabel info6 = new JLabel("", SwingConstants.CENTER);
	JLabel info7 = new JLabel("", SwingConstants.CENTER);
	JPanel p = new JPanel(new GridLayout(8,0));
	public VentanaInfo() {
		this.setSize(300, 200);
		this.setResizable(false);
		this.setTitle("Information");
		this.setLocationRelativeTo(MainServidor.vs);
		this.setVisible(false);
		p.setBackground(Color.WHITE);
	
		this.updateLabel();
		p.add(info);
		p.add(info2);
		p.add(info3);
		p.add(info4);
		p.add(info5);
		p.add(info6);
		p.add(info7);
		
		this.setContentPane(p);

	}

	public void updateLabel() {
		InetAddress localHost = null;
		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		info.setText("Program Version: " + ConstantesServer.version);
		info2.setText("Ip: " + localHost.getHostAddress());
		info3.setText("Port: " + Constantes.PORT);
		info4.setText(ConstantesServer.content);
		info5.setText("Made all with Java.");
		info6.setText("Operative System : "+System.getProperties().getProperty("os.name"));
		info7.setText("Java version : "+System.getProperties().getProperty("java.version"));
	}

	public void changeVersion(String version) {
		ConstantesServer.version = version;
	}
}
