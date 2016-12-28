package servidor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


import comun.Constantes;
import comun.Mensaje;

public class VentanaServidor extends JFrame{
	Cliente c;
	JLabel l = new JLabel("Username: ");
	JLabel l2 = new JLabel("IP: ");
	JLabel kick= new JLabel ("Kick username : ");
	JTextField username = new JTextField(20);
	JButton bKick = new JButton("Kick username");
	JTextField te3 = new JTextField(10);
	JTextField te2 = new JTextField(10);
	JTextField te = new JTextField(20);
	JTextArea ta ;
	JButton listaUsuarios = new JButton("Lista de Usuarios");
	JButton b = new JButton("Enviar");
	JButton b2 = new JButton("Clear data");
	JPanel p1= new JPanel(new FlowLayout());
	JPanel p2= new JPanel(new FlowLayout());
	JPanel p3= new JPanel(new FlowLayout());
	JPanel p;
	JScrollPane scroll;
	public VentanaServidor(){
		
		this.setSize(800, 500);
		
		this.setTitle("SERVIDOR");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ta= new JTextArea(20,60);
		ta.setEditable(false);
		scroll= new JScrollPane(ta);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		p= new JPanel(new BorderLayout());
		this.setContentPane(p);
		p.add(p1,BorderLayout.NORTH);
		p.add(p2,BorderLayout.CENTER);
		p.add(p3,BorderLayout.SOUTH);
		p1.add(kick);
		p1.add(username);
		p1.add(bKick);
		p1.add(b2);
		p1.add(te);
		p1.add(b);
		p2.add(scroll);
		p3.add(listaUsuarios);
		listaUsuarios.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				VentanaUsuarios v = new VentanaUsuarios();
			}
			
		});
		b2.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				MainServidor.eliminarData();
			}
			
		});
		b.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				MainServidor.enviarMensajeATodos(new Mensaje("<-SERVIDOR->  "+te.getText()+"  <-Servidor->"));
			}
			
		});
		bKick.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				MainServidor.buscarSocket(username.getText());
			}
			
		});
		
		
		
		this.setVisible(true);
	
	}
	public JTextArea getTa() {
		return ta;
	}
	public void setTa(JTextArea ta) {
		this.ta = ta;
	}
	public JTextField getUsername() {
		return username;
	}
	public void setUsername(JTextField username) {
		this.username = username;
	}
	
}
