package servidor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import comun.Comandos;
import comun.Constantes;
import comun.Mensaje;

public class VentanaServidor extends JFrame{
	Cliente c;
	JLabel l = new JLabel("Username: ");
	JLabel l2 = new JLabel("IP: ");
	JLabel kick= new JLabel ("Kick username : ");
	JTextField username = new JTextField(10);
	JButton bKick = new JButton("Kick username");
	JTextField te3 = new JTextField(10);
	JTextField te2 = new JTextField(10);
	JTextField te = new JTextField(10);
	JTextArea ta ;
	JButton listaUsuarios = new JButton("Lista de Usuarios");
	JButton listaBaneados = new JButton("Lista de Baneados");
	JButton b = new JButton("Enviar");
	JButton b2 = new JButton("Clear data");
	JButton ban = new JButton("Ban");
	JButton unban = new JButton("Unban");
	JButton cambiarPuerto = new JButton("Cambiar puerto");
	JTextField puerto = new JTextField(10);
	JLabel lPuerto = new JLabel("Puerto : ");
	JPanel p1= new JPanel(new FlowLayout());
	JPanel p2= new JPanel(new FlowLayout());
	JPanel p3= new JPanel(new FlowLayout());
	JPanel p;
	JScrollPane scroll;
	public VentanaServidor(){
		
		this.setSize(800, 500);
		this.setLocationRelativeTo(null);
		this.setTitle("Servidor. Diego Berrocal");
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
		p1.add(ban);
		p1.add(unban);
		p1.add(b2);
		p1.add(te);
		p1.add(b);
		p2.add(scroll);
		p3.add(listaUsuarios);
		p3.add(listaBaneados);
		p3.add(lPuerto);
		p3.add(puerto);
		p3.add(cambiarPuerto);
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
				int respuesta=JOptionPane.showConfirmDialog(null, "Seguro que quieres borrar la DATA", "Advertencia", JOptionPane.YES_NO_OPTION);
				if(respuesta == JOptionPane.YES_OPTION){
					MainServidor.eliminarData();
				}else{
					System.out.println("Se ha cancelado la ejecucion de la limpieza de mensajes e informacion");
				}
				
			}
			
		});
		cambiarPuerto.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				
				
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
				String ip=JOptionPane.showInputDialog(new JTextField(),"Escriba la ip que deseas desbanear");
				MainServidor.buscarSocketYDesBanear(ip);
			}
			
		});
		b.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				MainServidor.enviarMensajeATodos(new Mensaje("<-SERVIDOR->  "+te.getText()+"  <-SERVIDOR->"));
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
