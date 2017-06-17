
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class VentanaPrincipal extends JFrame {

	ArrayList<String> passwords = new ArrayList<>();
	JPanelBackground pPrincipal = new JPanelBackground(new BorderLayout());
	JPanel central = new JPanel();
	JPanel otros = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel subcentral = new JPanel(new GridLayout(8, 1, 280, 10));
	JPanel generalNorth = new JPanel(new BorderLayout());
	JCheckBox box = new JCheckBox("View header", false);
	JLabel system = new JLabel("System");
	JPanel pass = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPasswordField pwd = new JPasswordField(20);
	JLabel password = new JLabel("Password: ");
	JPanel usuario = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JLabel username = new JLabel("Username: ");
	JTextField user = new JTextField(20);
	JLabel login = new JLabel("Login");
	JLabel noaccount = new JLabel("I don't have an account");
	JButton submit = new JButton("Conectarse");
	JPanel elementos = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel direccion = new JPanel(new GridLayout(3, 1));
	JButton chat = new JButton("Ir al chat");
	JButton profile = new JButton("Ir a tu perfil");
	JButton crearServidor = new JButton("Crear nuevo servidor");
	JButton entrarServidor = new JButton("Entrar en un servidor");
	JButton atras = new JButton("Atras");
	private Connection con;
	Font font;

	public VentanaPrincipal() {
		pPrincipal.setBackground(("fondo.jpg"));
		this.con = con;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(900, 600);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("chat.png")));
				
		
		this.setTitle("");
		font = new Font("Sheriff", Font.PLAIN, 14);
		// try {
		//// font = Font.createFont(Font.TRUETYPE_FONT,
		// ("Orbitron\\orbitron-black.otf"));
		//
		// } catch (FontFormatException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		this.setContentPane(pPrincipal);
		this.setLocationRelativeTo(null);

		chat.setVisible(false);
		profile.setVisible(false);
		crearServidor.setVisible(false);
		entrarServidor.setVisible(false);

		usuario.add(username);
		usuario.add(user);

		pass.add(password);
		pass.add(pwd);

		elementos.add(submit);
		elementos.add(noaccount);

		subcentral.add(usuario);
		subcentral.add(pass);
		subcentral.add(elementos);
		subcentral.add(chat);

		subcentral.add(profile);
		// subcentral.add(entrarServidor);
		// subcentral.add(crearServidor);
		subcentral.add(atras);

		chat.setVisible(false);
		profile.setVisible(false);
		atras.setVisible(false);

		north.add(login);
		// font.deriveFont(40);
		font = font.deriveFont(Font.PLAIN, 25);
		login.setFont(font);
		font = font.deriveFont(Font.PLAIN, 12);
		noaccount.setFont(font);
		username.setFont(font);
		password.setFont(font);

		login.setForeground(Color.WHITE);

		central.add(subcentral);

		central.setOpaque(false);
		subcentral.setOpaque(false);
		pass.setOpaque(false);
		usuario.setOpaque(false);
		elementos.setOpaque(false);

		username.setForeground(Color.WHITE);
		password.setForeground(Color.WHITE);
		noaccount.setForeground(Color.BLUE);

		generalNorth.add(box, BorderLayout.WEST);
		generalNorth.add(system, BorderLayout.EAST);
		generalNorth.add(north, BorderLayout.CENTER);

		generalNorth.setOpaque(false);

		box.setForeground(Color.WHITE);
		system.setForeground(Color.WHITE);
		system.setText(System.getProperty("os.name"));

		pPrincipal.setOpaque(false);

		north.setOpaque(false);
		central.setOpaque(false);
		box.setOpaque(false);

		pPrincipal.add(central, BorderLayout.CENTER);

		pPrincipal.add(generalNorth, BorderLayout.NORTH);

		// tabla.setPtabla.setFillsViewportHeight(true);referredScrollableViewportSize(tabla.getPreferredSize());

		// tabla.setPreferredScrollableViewportSize(table.getPreferredSize());

		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chat.setVisible(true);
				profile.setVisible(true);
				atras.setVisible(true);

			}
		});
		chat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				subcentral.remove(atras);
				subcentral.remove(profile);
				subcentral.remove(chat);
				subcentral.add(entrarServidor);
				subcentral.add(crearServidor);
				subcentral.add(atras);
				subcentral.updateUI();
				entrarServidor.setVisible(true);
				crearServidor.setVisible(true);

			}
		});
		atras.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (entrarServidor.isVisible() || crearServidor.isVisible()) {
					entrarServidor.setVisible(false);
					crearServidor.setVisible(false);
					subcentral.remove(entrarServidor);
					subcentral.remove(crearServidor);
					subcentral.remove(atras);
					subcentral.add(chat);
					subcentral.add(profile);
					subcentral.add(atras);
					subcentral.updateUI();
				} else if (chat.isVisible() || profile.isVisible()) {

					chat.setVisible(false);
					profile.setVisible(false);
					entrarServidor.setVisible(false);
					crearServidor.setVisible(false);
					atras.setVisible(false);

				}

			}
		});
		entrarServidor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ToolStartApplication.startApp("Cliente");
			}
		});
		crearServidor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ToolStartApplication.startApp("Servidor");
			}
		});
	}
}
