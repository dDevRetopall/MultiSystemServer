package cliente;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import comun.Comandos;
import comun.Constantes;
import comun.PeticionDeLogin;
import comun.Profile;

public class VentanaLogin extends JFrame {
	String usuario = "someone";
	JPanel p = new JPanel(new GridLayout(3, 1));
	JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JTextField username = new JTextField(20);
	JPasswordField password = new JPasswordField(20);
	JLabel usernameLb = new JLabel("Username: ");
	JLabel passwordLb = new JLabel("Password: ");
	JButton b = new JButton("Login");
	Socket s;
	OutputStream os;
	ObjectOutputStream oos;
	InputStream is;
	ObjectInputStream ois;
	boolean connected = true;
	private VentanaCliente vc;

	public VentanaLogin(VentanaCliente vc) {

		this.vc = vc;

		this.setSize(300, 200);
		this.setResizable(false);
		this.setTitle("Login");
		this.setLocationRelativeTo(vc);
		this.setVisible(false);

		username.setHorizontalAlignment(JTextField.CENTER);
		password.setHorizontalAlignment(JTextField.CENTER);

		p1.add(usernameLb);
		p1.add(username);
		p2.add(passwordLb);
		p2.add(password);
		p3.add(b);

		p.add(p1);
		p.add(p2);
		p.add(p3);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Se esta cerrando la ventana");
				username.setText("");
				password.setText("");
				

			}

		});
		
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					s = new Socket(Constantes.HOST, Constantes.PORT);

					try {
						is = s.getInputStream();
						ois = new ObjectInputStream(is);
						os = s.getOutputStream();
						oos = new ObjectOutputStream(os);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (UnknownHostException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				VentanaLogin.this.setVisible(false);
				String passwordString = "";
				char chars[] = password.getPassword();
				for (int i = 0; i < password.getPassword().length; i++) {
					passwordString = passwordString + chars[i];
				}
				if (!passwordString.isEmpty() && !username.getText().isEmpty()) {
					usuario = username.getText();
					enviarLoginAlServidor(new PeticionDeLogin(username.getText(), passwordString));
				}

				username.setText("");
				password.setText("");

			}

		});

		this.setContentPane(p);

	}
	

	public void enviarLoginAlServidor(PeticionDeLogin p) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (connected) {
					try {
						System.out.println("Se ha detectado algo");
						Object o = ois.readObject();
						if (o instanceof Comandos) {
							Comandos c = (Comandos) o;
							if (c.habilitarBotonConexion) {
								vc.registrarse.setForeground(Color.BLACK);
								vc.loguearte.setForeground(Color.BLACK);
								vc.loguearte.setEnabled(false);
								vc.registrarse.setEnabled(false);
								
								vc.te2.setText(usuario);
								vc.b2.setEnabled(true);
								if (c.enseñarOptionPane) {
									JOptionPane.showMessageDialog(vc, "Se ha iniciado sesion correctamente");
									connected = false;
									s.close();
								}
							} else if (!c.existeUsuario && c.enseñarOptionPane) {
								System.out.println("Visualizando cuadro de texto con "
										+ ".Resultado del intento de inicio de sesion - ERROR");
								JOptionPane.showMessageDialog(vc, c.mensaje);

							}
						}
					} catch (ClassNotFoundException e) {
						connected = false;
						e.printStackTrace();
					} catch (IOException e) {
						connected = false;
						e.printStackTrace();
					}

				}

			}
		});
		t.start();
		try {

			oos.writeObject(p);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
