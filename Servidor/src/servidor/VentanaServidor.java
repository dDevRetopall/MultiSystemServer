package servidor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import comun.Comandos;
import comun.Constantes;
import comun.Mensaje;
import utils.ConstantesServer;

public class VentanaServidor extends JFrame {
	Cliente c;
	JLabel l = new JLabel("Username: ");
	JLabel l2 = new JLabel("IP: ");
	JLabel kick = new JLabel("Kick username : ");
	JTextField username = new JTextField(10);
	JButton bKick = new JButton("Kick username");
	JTextField te3 = new JTextField(10);
	JTextField te2 = new JTextField(10);
	JTextField te = new JTextField(10);
	JTextArea ta;
	JButton listaUsuarios = new JButton("Lista de Usuarios");
	JButton listaBaneados = new JButton("Lista de Baneados");
	JButton b = new JButton("Enviar");
	JButton info = new JButton("Show info");
	JButton b2 = new JButton("Clear data");
	JButton ban = new JButton("Ban");
	JButton unban = new JButton("Unban");
	JButton cambiarpwd = new JButton("Cambiar ");
	JTextField pwd = new JTextField(10);
	JLabel lpwd = new JLabel("Cambiar pwd server : ");
	JLabel serverInfo = new JLabel("Version: "+ConstantesServer.version);
	JPanel p1 = new JPanel(new FlowLayout());
	JPanel p2 = new JPanel(new FlowLayout());
	JPanel p3 = new JPanel(new GridLayout(2, 1));
	JPanel p5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel p4 = new JPanel(new FlowLayout());
	JPanel p;
	
	JScrollPane scroll;

	public VentanaServidor() {

		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setTitle("Servidor");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ta = new JTextArea(23, 60);
		ta.setFont(new Font("Arial", Font.PLAIN, 14));
		ta.setEditable(false);
		scroll = new JScrollPane(ta);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		serverInfo.setFont(new Font("Sherif", Font.PLAIN, 10));

		p = new JPanel(new BorderLayout());
		this.setContentPane(p);
		p.add(p1, BorderLayout.NORTH);
		p.add(p2, BorderLayout.CENTER);
		p.add(p3, BorderLayout.SOUTH);
		p3.add(p4);
		p3.add(p5);
		p5.add(serverInfo);
		p5.add(info);

		p1.add(kick);

		p1.add(username);
		p1.add(bKick);
		p1.add(ban);
		p1.add(unban);
		p1.add(b2);
		p1.add(te);
		p1.add(b);
		p2.add(scroll);

		p4.add(listaUsuarios);
		p4.add(listaBaneados);
		
		p4.add(lpwd);
		p4.add(pwd);
		p4.add(cambiarpwd);
		info.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				VentanaInfo i = new VentanaInfo();
				i.setVisible(true);
			}

		});
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
				int respuesta = JOptionPane.showConfirmDialog(MainServidor.vs, "Seguro que quieres borrar la DATA",
						"Advertencia", JOptionPane.YES_NO_OPTION);
				if (respuesta == JOptionPane.YES_OPTION) {
					MainServidor.eliminarData();
				} else {
					System.out.println("Se ha cancelado la ejecucion de la limpieza de mensajes e informacion");
				}

			}

		});
		cambiarpwd.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (!pwd.getText().isEmpty()) {
					JPanel panel = new JPanel();
					JLabel label = new JLabel("Enter a password:");
					JPasswordField pass = new JPasswordField(10);
					panel.add(label);
					panel.add(pass);
					String[] options = new String[] { "Accept", "Cancel" };
					int option = JOptionPane.showOptionDialog(null, panel, "Escribe la anterior contraseña",
							JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, panel);
					if (option == 0) // pressing OK button
					{
						char[] password = pass.getPassword();
						String passwordCompleta = "";
						for (int i = 0; i < password.length; i++) {
							passwordCompleta = passwordCompleta + password[i];

						}
						boolean resultado = ConnectionSQL.hacerConsultaDeSetting("passAdmin", passwordCompleta);
						if (!resultado) {
							System.out.println("Contraseña incorrecta");
						} else {
							System.out.println("Contraseña aprobada");
							ConnectionSQL.editSetting(MainServidor.con, "passAdmin", pwd.getText(), 1);
						}

					}
				}
			}

		});
		listaBaneados.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				VentanaBaneados b = new VentanaBaneados();

			}

		});
		ban.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				MainServidor.buscarSocketYBanear(username.getText());
			}

		});
		unban.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				String ip = JOptionPane.showInputDialog(new JTextField(), "Escriba la ip que deseas desbanear");
				MainServidor.buscarSocketYDesBanear(ip);
			}

		});
		b.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				MainServidor.enviarMensajeATodos(new Mensaje(te.getText()), true);
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
