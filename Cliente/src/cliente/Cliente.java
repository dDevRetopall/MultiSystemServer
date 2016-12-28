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

import javax.swing.JPanel;

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
								Mensaje m = (Mensaje)o;
								vc.getTa().setText(vc.getTa().getText()+m.getMensaje());
							}else if(o instanceof Usuarios){
								ArrayList usuarios = (ArrayList)o;
								String usuarios1="";
								for(int i =0;i<usuarios.size();i++){
									usuarios1=usuarios1+usuarios.get(i)+"\n";
									
								}
								vc.getV().getTa().setText(usuarios1);
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
