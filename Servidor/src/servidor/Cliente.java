package servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JOptionPane;

import comun.Comandos;
import comun.Mensaje;
import comun.PeticionDeLogin;
import comun.Profile;
import comun.Usuario;
import comun.Usuarios;


public class Cliente {
	public Socket s;
	private String usuario="";
	private String ipUsuario="";
	public boolean connected=false;
	OutputStream os;
	ObjectOutputStream oos ;
	InputStream is;
	ObjectInputStream ois ;
	
	public Cliente(Socket s){
		this.s = s;
		connected=true;
		try {
			os = s.getOutputStream();
			oos= new ObjectOutputStream(os);
			is = s.getInputStream();
			ois= new ObjectInputStream(is);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
	
	
		Thread t = new Thread(new Runnable() {
			
			public void run() {
				
				
				while(connected){
					
					try {
						
						Object o = ois.readObject();
						if(o instanceof Mensaje){
							if(connected){
							Mensaje m =(Mensaje)o;
							System.out.println(m.getMensaje());
							MainServidor.enviarMensajeATodos(m);
							}
						}else if(o instanceof Profile){
							System.out.println("Ha llegado una nueva peticion de registro!!!");
							Profile p =(Profile)o;
							GestionUsuarios.register(p.getUsername(), p.getPassword());
							System.out.println("Se ha creado un registro nuevo");
							System.out.println("Cerrando entrada...");
							connected=false;
							s.close();
						}else if(o instanceof PeticionDeLogin){
							System.out.println("Detectada una peticion de login");
							PeticionDeLogin l = (PeticionDeLogin) o;
							System.out.println("Consulta aceptada");
							boolean resultado=ConnectionSQL.hacerConsulta(l.getUsername(), l.getPassword());
							if(resultado){
								enviarMensaje(new Comandos(true,true));
							}else{
								enviarMensaje(new Comandos(true,"Contraseña o usuario incorrecto"));
							}
						}else if(o instanceof Usuario){
							System.out.println("USUARIO");
							boolean estaEnListaNegra=false;
							Usuario u =(Usuario)o;
							usuario= u.getUsername();
							
							ipUsuario=	s.getInetAddress().getHostAddress();
							System.out.println(ipUsuario+" : "+usuario);
							boolean b=MainServidor.existeUsuario(u.getUsername());
							System.out.println("Existe usuario??? "+b);
							if(!b){
								Iterator<String> i = ListaNegra.ipaddress.iterator();
								while(i.hasNext()){
									String ip=i.next();
									System.out.println(ip);
									if(ipUsuario.equals(ip)){
										estaEnListaNegra=true;
										System.out.println(ipUsuario+" DETECTADA");
									}
								}
								System.out.println("ESTA EN LISTA NEGRA? "+estaEnListaNegra);
								if(!estaEnListaNegra){
								
								System.out.println("NOMBRE USUARIOS RECIBIDO "+ u.getUsername());
								MainServidor.u.usuariosNombre.add(usuario);
								MainServidor.enviarMensajeATodos(MainServidor.u);
								for(Mensaje m : Loader.mensajes){
									enviarMensaje(m);
								}
								if(Loader.mensajes.isEmpty()){
									enviarMensaje(new Comandos(true));
								}
								}else{
									enviarMensaje(new Comandos(true,true,"Has sido baneado"));
									MainServidor.enviarMensajeATodos(MainServidor.u);
//									try {
//										Thread.sleep(40);
//									} catch (InterruptedException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
									connected=false;
									s.close();
								}
							}else{
								System.out.println("Enviando mensaje de q ya existe el usuario");
								enviarMensaje(new Comandos(true,true,"Ya existe ese usuario"));
								MainServidor.enviarMensajeATodos(MainServidor.u);
							/*	try {
									Thread.sleep(40);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}*/
								connected=false;
								s.close();
								
								
								
							}
							
						}
						
					} catch (IOException e) {
						if(!Cliente.this.getUsuario().isEmpty()){
						MainServidor.u.usuariosNombre.remove(Cliente.this.getUsuario());
						MainServidor.enviarMensajeATodos(MainServidor.u);
						MainServidor.enviarMensajeATodos(new Mensaje(Cliente.this.getUsuario(),false));
						
						
						
						}
						connected=false;
						try {
							s.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						e.printStackTrace();
						
					} catch (ClassNotFoundException e) {
						if(!Cliente.this.getUsuario().isEmpty()){
						MainServidor.u.usuariosNombre.remove(Cliente.this.getUsuario());
						MainServidor.enviarMensajeATodos(MainServidor.u);
						MainServidor.enviarMensajeATodos(new Mensaje(Cliente.this.getUsuario(),false));
						
						
						
						}
						try {
							s.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						connected=false;
						
						e.printStackTrace();
					}
					
					
					
					
				}
				
			}
		});
		t.start();
		
	}
	
	public void enviarMensaje(Mensaje m){
		if(connected ){
	
		try {
			
			oos.writeObject(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public void enviarMensaje(Comandos c){
		if(connected){
		
		try {
			
			oos.writeObject(c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public void enviarMensaje(Usuarios usuariosNombres){
		if(connected){
		
		try {
			
			oos.writeObject(usuariosNombres);
			Iterator i = usuariosNombres.usuariosNombre.iterator();
			System.out.println("Enviando a "+this.getUsuario()+" con la IP de "+this.getIpUsuario());
			while(i.hasNext()){
				System.out.println("->"+i.next());
				
			}
	
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public void kick(){
		try {
			
			System.out.println("Se ha kickeado a "+usuario);
			MainServidor.enviarMensajeATodos(new Mensaje("Se ha kickeado a "+usuario));
			MainServidor.enviarMensajeATodos(new Mensaje(Cliente.this.getUsuario(),false));
			
			
			s.close();
			connected=false;
		} catch (IOException e) {
			System.out.println("ERROR NORMAL??¿?¿");
			e.printStackTrace();
		}
	}
	public void ban() {
		
			System.out.println("BANEADA "+ipUsuario+" : "+usuario);
		ListaNegra.ipaddress.add(this.getIpUsuario());
		//ListaNegra.username.add(this.getUsuario());
		System.out.println("Se ha baneado a "+getUsuario()+" con "+getIpUsuario());
		MainServidor.enviarMensajeATodos(new Mensaje(getUsuario()+" has been banned"));
		enviarMensaje(new Comandos(true,true,"El servidor te ha baneado"));
		Iterator i = ListaNegra.ipaddress.iterator();
		ListaNegra.saveList();
		while(i.hasNext()){
			System.out.println("IP BANEADA  : "+i.next());
		}
		}
		
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getIpUsuario() {
		return ipUsuario;
	}

	public void setIpUsuario(String ipUsuario) {
		this.ipUsuario = ipUsuario;
	}
	
	
}
