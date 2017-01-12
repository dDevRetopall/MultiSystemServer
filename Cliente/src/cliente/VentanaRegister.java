package cliente;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

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

	
	public VentanaRegister() {
	
		
		this.setVisible(false);
		this.setSize(300, 200);
		this.setResizable(false);                                            
		this.setTitle("Registro");
		this.setLocationRelativeTo(null);
		
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
		
		b.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
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
				super.mouseClicked(e);
			}
			
		});
		
		this.setContentPane(p);
	
		
		
		
	}
	public void enviarProfileAlServidor(Profile p){
		Socket s;
		try {
			s = new Socket(Constantes.HOST,Constantes.PORT);
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
