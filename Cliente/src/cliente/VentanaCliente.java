package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
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
import comun.Usuario;


public class VentanaCliente extends JFrame{
	Cliente c;
	VentanaUsuarios v;
	JLabel l = new JLabel("Username: ");
	JLabel l2 = new JLabel("IP: ");
	JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JTextField te3 = new JTextField(10);
	JTextField te2 = new JTextField(10);
	JTextField te = new JTextField(20);
	JTextArea ta ;
	JButton b = new JButton("Enviar");
	JButton b2 = new JButton("Conectarse");
	JButton listaUsuarios = new JButton("Lista de Usuarios");
	JPanel p1= new JPanel(new FlowLayout());
	JPanel p2= new JPanel(new FlowLayout());
	JPanel p;
	JScrollPane scroll;
	public VentanaCliente(){

		this.setSize(800, 500);

		this.setTitle("CLIENTE");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ta= new JTextArea(20,60);
		ta.setEditable(false);
		scroll= new JScrollPane(ta);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		b.setEnabled(false);
		b2.setEnabled(true);

		p= new JPanel(new BorderLayout());
		this.setContentPane(p);
		te3.setText("localhost");
		p.add(p1,BorderLayout.NORTH);
		p.add(p2,BorderLayout.CENTER);
		p.add(p3,BorderLayout.SOUTH);
		p1.add(l2);
		p1.add(te3);
		p1.add(l);
		p1.add(te2);
		p1.add(b2);
		p1.add(te);
		p1.add(b);
		p2.add(scroll);
		p3.add(listaUsuarios);
		v = new VentanaUsuarios();
		
		listaUsuarios.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				v.setVisible(true);
			}

		});
		b2.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(b2.isEnabled()){
					super.mouseClicked(e);
					Constantes.HOST=te3.getText();
					c = new Cliente(te2.getText(),VentanaCliente.this);
					c.enviarMensajeAlServidor(new Mensaje(te2.getText(),true));
					c.enviarMensajeAlServidor(new Usuario(te2.getText()));
					b.setEnabled(true);
				}
			}

		});
		b.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(b.isEnabled()){
					super.mouseClicked(e);
					c.enviarMensajeAlServidor(new Mensaje(te.getText()));
					te.setText("");
				}
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
	public VentanaUsuarios getV() {
		return v;
	}
	public void setV(VentanaUsuarios v) {
		this.v = v;
	}

	public void habilitarConexion(){
		b2.setEnabled(true);
		b.setEnabled(false);
	}
	public void deshabilitarConexion() {
		b2.setEnabled(false);
		b.setEnabled(true);

	}

}
