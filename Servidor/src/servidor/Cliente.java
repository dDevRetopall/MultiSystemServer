package servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import comun.Comandos;
import comun.Mensaje;
import comun.Usuario;
import comun.Usuarios;


public class Cliente {
	private Socket s;
	private String usuario="";
	private boolean connected=false;
	public Cliente(Socket s){
		this.s = s;
		connected=true;
		
		for(Mensaje m : Loader.mensajes){
			enviarMensaje(m);
		}
		if(Loader.mensajes.isEmpty()){
			enviarMensaje(new Comandos(true));
		}
	
		Thread t = new Thread(new Runnable() {
			
			public void run() {
				while(connected){
					
					try {
						InputStream is = s.getInputStream();
						ObjectInputStream ois = new ObjectInputStream(is);
						Object o = ois.readObject();
						if(o instanceof Mensaje){
							Mensaje m =(Mensaje)o;
							System.out.println(m.getMensaje());
							MainServidor.enviarMensajeATodos(m);
						}else if(o instanceof Usuario){
							Usuario u =(Usuario)o;
							usuario= u.getUsername();
							MainServidor.u.add(usuario);
							MainServidor.enviarMensajeATodos(MainServidor.u);
						}
						
					} catch (IOException e) {
						MainServidor.u.remove(Cliente.this.getUsuario());
						connected=false;
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						connected=false;
						e.printStackTrace();
					}
					
					
					
				}
				
			}
		});
		t.start();
		
	}
	
	public void enviarMensaje(Mensaje m){
		if(connected){
		OutputStream os;
		try {
			os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public void enviarMensaje(Comandos c){
		if(connected){
		OutputStream os;
		try {
			os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public void enviarMensaje(Usuarios u){
		if(connected){
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
	public void kick(){
		try {
			s.close();
			connected=false;
			System.out.println("Se ha kickeado a "+usuario);
			MainServidor.u.remove(this.getUsuario());
			MainServidor.enviarMensajeATodos(new Mensaje("Se ha kickeado a "+usuario));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
}
