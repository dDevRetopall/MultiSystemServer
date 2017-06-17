

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import comun.Comandos;
import comun.Constantes;
import comun.Profile;

public class VentanaRegister extends JFrame{
	JPanel p = new JPanel(new GridLayout(3,1));
	JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JTextField username = new JTextField(20);
	JPasswordField password = new JPasswordField(20);
	JLabel usernameLb = new JLabel("Username: ");
	JLabel passwordLb = new JLabel("Password: ");
	JButton b = new JButton("Registrar!");
	InputStream is;
	ObjectInputStream ois;
	boolean connected = true;
	Socket s;
	public VentanaRegister(){
		this.setSize(300, 200);
		this.setResizable(false);                                            
		this.setTitle("Registro");
	
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
		
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaRegister.this.setVisible(false);
				String passwordString="";
				char chars[]=password.getPassword();
				for(int i =0;i<password.getPassword().length;i++){
					passwordString=passwordString+chars[i];
				}
				if(!passwordString.isEmpty()&&!username.getText().isEmpty()){
					enviarProfileAlServidor(new Profile(username.getText(),passwordString));
				}
				
				username.setText("");
				password.setText("");
				
			}
		});
		
		this.setContentPane(p);
		try {
			s = new Socket(Constantes.HOST,Constantes.PORT);
			is = s.getInputStream();
			ois = new ObjectInputStream(is);;
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (connected) {
					try {
						System.out.println("Se ha detectado algo");
						Object o = ois.readObject();
						if (o instanceof Comandos) {
						Comandos c = (Comandos) o;
						if (c.enseñarOptionPane) {
									//JOptionPane.showMessageDialog(vc, c.mensaje);
									connected = false;
									s.close();
								}
							

							}
						}
					 catch (ClassNotFoundException e) {
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
		
		
		
	}
	public void enviarProfileAlServidor(Profile p) {
	
		try {
			
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
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
