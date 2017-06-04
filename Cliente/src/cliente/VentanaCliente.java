package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.JTextComponent;

import comun.Constantes;
import comun.Mensaje;
import comun.Usuario;

public class VentanaCliente extends JFrame {
	Cliente c;
	VentanaUsuarios v;
	JLabel l = new JLabel("Username: ");
	JLabel l2 = new JLabel("IP: ");
	JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	VentanaRegister vr;
	VentanaLogin vl;
	JTextField te3 = new JTextField(10);
	JTextField te2 = new JTextField(10);
	JTextArea te;
	JTextArea ta;
	JButton agrandarArea = new JButton();
	JButton b = new JButton("Enviar");
	JButton b2 = new JButton("Conectarse");
	JButton listaUsuarios = new JButton("Lista de Usuarios");
	JPanel p1 = new JPanel(new FlowLayout());
	JPanel p2 = new JPanel(new FlowLayout());
	JPanel p;
	JScrollPane scroll;
	JScrollPane scroll2;
	JLabel registrarse = new JLabel("Registrarse");
	JLabel loguearte = new JLabel("Login");
	JButton desconectarse = new JButton("Desconectarse");
	boolean minimizado = true;

	public VentanaCliente(String name) {

		this.setSize(900, 500);
		this.setLocationRelativeTo(MainCliente.vs);
		this.setTitle("Cliente " + name);

		te = new JTextArea(1, 20);
		te.setFont(new Font("Arial", Font.PLAIN, 12));
		scroll2 = new JScrollPane(te);
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		ta = new JTextArea(20, 60);
		ta.setFont(new Font("Arial", Font.BOLD, 12));
		ta.setEditable(false);
		scroll = new JScrollPane(ta);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		b.setEnabled(false);

		registrarse.setForeground(Color.BLUE);
		loguearte.setForeground(Color.BLUE);

		// -------------Cambiar------------\\
		// b2.setEnabled(true);
		b2.setEnabled(false);
		// ---------------------------------\\

		p = new JPanel(new BorderLayout());
		this.setContentPane(p);
		desconectarse.setEnabled(false);
		te3.setText(Constantes.HOST);

		p.add(p1, BorderLayout.NORTH);
		p.add(p2, BorderLayout.CENTER);
		p.add(p3, BorderLayout.SOUTH);

		p1.add(l2);
		p1.add(te3);

		p1.add(l);
		p1.add(te2);
		p1.add(b2);
		p1.add(scroll2);

		p1.add(b);
		ImageIcon ii = new ImageIcon(getClass().getResource("descarga.png"));

		p1.add(agrandarArea);
		agrandarArea.setIcon(ii);
		p2.add(scroll);
		p3.add(registrarse);
		p3.add(listaUsuarios);
		p3.add(desconectarse);
		p3.add(loguearte);

		v = new VentanaUsuarios();
		vr = new VentanaRegister(this);
		vl = new VentanaLogin(this);
		v.getTa().setText("No estas conectado");
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Se esta cerrando la ventana");
				try {
					c.s.close();

				} catch (IOException e1) {
					System.out.println("Error mientras se intentaba cerra la ventana");
					e1.printStackTrace();
				} catch (NullPointerException e2) {
					VentanaCliente.this.setVisible(false);

				}

			}

		});
		desconectarse.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (desconectarse.isEnabled()) {
					super.mouseClicked(e);
					try {
						cambiosComponentesAlDesconectarse();
						c.s.close();

					} catch (IOException e1) {
						System.out.println("Error mientras se intentaba cerra la ventana");
						e1.printStackTrace();
					} catch (NullPointerException e2) {
						System.out.println("Unexpected error (null)");

					}
				}
			}
		});
		listaUsuarios.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				v.setVisible(true);
			}

		});

		agrandarArea.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (minimizado) {
					te.setColumns(20);
					te.setRows(5);
					VentanaCliente.this.pack();
					minimizado = false;
				} else {
					te.setColumns(20);
					te.setRows(1);
					VentanaCliente.this.pack();
					minimizado = true;
				}

				// scroll2 = new JScrollPane(te);
				// scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			}

		});
		b2.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (b2.isEnabled()) {
					super.mouseClicked(e);
					if (!te2.getText().isEmpty()) {
						Constantes.HOST = te3.getText();
						v.getTa().setText("Usuarios" + "\n");
						// Puerto.loadList();
						ta.setText("");
						c = new Cliente(te2.getText(), VentanaCliente.this);
						c.enviarMensajeAlServidor(new Usuario(te2.getText()));
						c.enviarMensajeDeConexionAlServidor(new Mensaje(te2.getText(), true));

						cambiosComponentesAlConectarse();
					} else {
						System.out.println("No te puedes conectar porque el campo de usuario esta vacio");
					}

				}
			}

		});

		b.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (b.isEnabled()) {
					super.mouseClicked(e);
					if (!te.getText().isEmpty()) {
						c.enviarMensajeAlServidor(new Mensaje(te.getText()));
						te.setText("");
					}

				}
			}

		});
		registrarse.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (registrarse.isEnabled()) {
					vr.setVisible(true);
					vr.setLocationRelativeTo(VentanaCliente.this);
				}
			}
		});
		loguearte.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (loguearte.isEnabled()) {
					vl.setVisible(true);
					vl.setLocationRelativeTo(VentanaCliente.this);
				}
			}
		});

		this.setVisible(true);

	}

	public void cambiosComponentesAlConectarse() {
		b.setEnabled(true);
		desconectarse.setEnabled(true);
		
		
	}

	public void cambiosComponentesAlDesconectarse() {
		b.setEnabled(false);
		loguearte.setEnabled(true);
		registrarse.setEnabled(true);
		desconectarse.setEnabled(false);
		registrarse.setForeground(Color.BLUE);
		loguearte.setForeground(Color.BLUE);
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

	public void habilitarConexion() {
		b2.setEnabled(true);
		b.setEnabled(false);
		v.getTa().setText("No estas conectado");

	}

	public void deshabilitarConexion() {
		b2.setEnabled(false);
		b.setEnabled(true);

	}

}
