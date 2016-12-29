package cliente;

import java.awt.Panel;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import comun.Comandos;
import comun.Constantes;
import comun.Mensaje;

import comun.Usuario;
import comun.Usuarios;


public class Cliente {
	public Socket s ;
	private boolean connected=false;
	VentanaCliente vc;
	private String usuario;
	public Cliente(String usuario,VentanaCliente vc){
		this.vc=vc;
		this.usuario = usuario;
		
		try{
			s= new Socket(Constantes.HOST,Constantes.PORT);
			connected=true;
			vc.deshabilitarConexion();
		} catch (UnknownHostException e) {
			connected=false;
			vc.habilitarConexion();
			vc.getTa().setText("No se reconoce ese host. \n");
			e.printStackTrace();
		} catch (IOException e) {
			connected=false;
			vc.habilitarConexion();
			vc.getTa().setText("No se pudo conectar, comprueba tu conexion a internet \n");
			e.printStackTrace();
		}
		
		new Thread(){
			@Override
			public void run() {
				while(connected){


					InputStream is;
					
						try {
							
							is = s.getInputStream();
							ObjectInputStream ois = new ObjectInputStream(is);
							try {
							Object o = ois.readObject();
							if(o instanceof Mensaje){
								System.out.println("Detectado un mensaje");
								Mensaje m = (Mensaje)o;
								vc.getTa().setText(vc.getTa().getText()+m.getMensaje());
							}else if(o instanceof Usuarios){
								System.out.println("Detectado un usuario");
								Usuarios usuarios = (Usuarios)o;
								String usuarios1="";
								Iterator i = usuarios.usuariosNombre.iterator();
								while(i.hasNext()){
									
									usuarios1=usuarios1+i.next()+"\n";
									System.out.println(usuarios1);
								}
									
								
							
								
								vc.getV().getTa().setText("Usuarios \n"+usuarios1);
							}else if(o instanceof Comandos){
								System.out.println("Detectado un comando");
								Comandos c = (Comandos)o;
								if(c.borrarData){
									System.out.println("BORRADO DATA");
									vc.getTa().setText("");
								}else if(c.existeUsuario){
									if(c.enseñarOptionPane){
										vc.te2.setText("");
										JOptionPane.showMessageDialog(null,c.mensaje);
									}
								}
							}
						} catch (ClassNotFoundException e1) {
							connected=false;
							vc.habilitarConexion();
							e1.printStackTrace();
						}
						} catch (IOException e) {
							connected=false;
							vc.habilitarConexion();
							e.printStackTrace();
						}
						
						
			

				}
			}
		}.start();
		
	}
	
	
	public void enviarMensajeAlServidor(Mensaje m) {
		OutputStream os;
		try {
			os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(new Mensaje(usuario+"-> "+m.getMensaje()));//revisar
		} catch (IOException e) {
			connected=false;
			vc.habilitarConexion();
			e.printStackTrace();
		}

	}

	public void enviarMensajeAlServidor(Usuario u) {
		OutputStream os;
		try {
			os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(u);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
