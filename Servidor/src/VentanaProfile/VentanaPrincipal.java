package VentanaProfile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import servidor.ConnectionSQL;

public class VentanaPrincipal extends JFrame {

	JPanel pp = new JPanel(new GridLayout(4, 1));
	JPanel p0 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JTextField username = new JTextField(20);
	JPasswordField password = new JPasswordField(20);
	JLabel usernameLb = new JLabel("Username: ");
	JLabel passwordLb = new JLabel("Password: ");
	JLabel status = new JLabel();
	JLabel register = new JLabel("Dont have an account?");
	JButton b = new JButton("Login");
	private JLabel lblBackgroundImage = new JLabel();
	private Connection con;

	public VentanaPrincipal(Connection con){
	this.con = con;
		//	p.setLayout(new GridBagLayout());
		register.setForeground(new Color(0,0,153));
		this.setTitle("Server application");
		this.setSize(450, 350);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		status.setForeground(Color.GREEN);
		status.setText( "Status: "+"working");
		usernameLb.setForeground(Color.WHITE);
		passwordLb.setForeground(Color.WHITE);
		lblBackgroundImage.setIcon(new ImageIcon("hqdefault.jpg"));
	    lblBackgroundImage.setLayout(new BorderLayout());
		p1.add(usernameLb);
		p1.add(username);
		p2.add(passwordLb);
		p2.add(password);
		p3.add(b);
		p3.add(register);
		p0.add(p1);
		p0.add(p2);
		pp.add(status);
		
		
		
		pp.add(p0);
		pp.add(p3);
		
	
		p1.setOpaque(false);
		p2.setOpaque(false);
		p3.setOpaque(false);
		p0.setOpaque(false);
		pp.setOpaque(false);
		
		lblBackgroundImage.add(pp);
		add(lblBackgroundImage);
		
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String pass="";
				char[]descompuesto = password.getPassword();
				for(int i =0;i<descompuesto.length;i++){
					pass=pass+descompuesto[i];
				}
				boolean resultado=ConnectionSQL.hacerConsulta(username.getText(), pass);
				if(resultado){
					System.out.println("COntraseña aceptada");
				}else{
				System.out.println("COntraseña erronea");
				}
			}
		});
	}
}
