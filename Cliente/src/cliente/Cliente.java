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
import comun.PeticionDeLogin;
import comun.Profile;
import comun.Usuario;
import comun.Usuarios;

public class Cliente {
	public Socket s;
	private boolean connected = false;
	VentanaCliente vc;
	private String usuario;
	InputStream is;
	ObjectInputStream ois;
	OutputStream os;
	ObjectOutputStream oos;
	private boolean usuarioReal;

	public Cliente(String usuario, VentanaCliente vc) {
		this.vc = vc;
		this.usuario = usuario;
		this.usuarioReal = usuarioReal;

		try {
			s = new Socket(Constantes.HOST, Constantes.PORT);
			connected = true;
			vc.deshabilitarConexion();
		} catch (UnknownHostException e) {
			connected = false;
			vc.habilitarConexion();
			vc.getTa().setText("No se reconoce ese host. \n");
			e.printStackTrace();
		} catch (IOException e) {
			connected = false;
			vc.habilitarConexion();
			vc.getTa().setText("No se pudo conectar, comprueba tu conexion a internet \n");
			e.printStackTrace();
		}
		try {
			os = s.getOutputStream();
			oos = new ObjectOutputStream(os);
			is = s.getInputStream();
			ois = new ObjectInputStream(is);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		new Thread() {
			@Override
			public void run() {
				while (connected) {
					try {

						Object o = ois.readObject();
						if (o instanceof Mensaje) {
							System.out.println("Detectado un mensaje");
							Mensaje m = (Mensaje) o;
							vc.getTa().setText(vc.getTa().getText() + m.getMensaje());
							vc.scroll.getVerticalScrollBar().setValue(vc.scroll.getVerticalScrollBar().getMaximum());
						} else if (o instanceof Usuarios) {
							System.out.println(o.getClass());
							System.out.println("Detectado un usuario");
							Usuarios usuarios = (Usuarios) o;
							String usuarios1 = "";
							System.out.println(usuarios.usuariosNombre.size());
							Iterator i = usuarios.usuariosNombre.iterator();
							while (i.hasNext()) {
								String next= (String) i.next();
								System.out.println("NEXT >> "+next);
								usuarios1 = usuarios1 + next + "\n";
								System.out.println(usuarios1);
							}
							
							vc.getV().getTa().setText("Usuarios \n" + usuarios1);
						} else if (o instanceof Comandos) {

							System.out.println("Detectado un comando");
							Comandos c = (Comandos) o;

							if (c.borrarData) {
								System.out.println("BORRADO DATA");
								vc.getTa().setText("");
							} else if (c.existeUsuario) {
								if (c.enseñarOptionPane) {
									vc.te2.setText("");
									System.out.println("Visualizando cuadro de texto con " + c.mensaje);
									JOptionPane.showMessageDialog(vc, c.mensaje);
								}
							}
						}
					} catch (ClassNotFoundException e1) {
						connected = false;
						vc.habilitarConexion();
						e1.printStackTrace();

					} catch (IOException e) {
						connected = false;
						vc.habilitarConexion();
						e.printStackTrace();
					}

				}
			}
		}.start();

	}

	public void enviarMensajeAlServidor(Mensaje m) {

		try {

			oos.writeObject(new Mensaje(usuario + "-> " + m.getMensaje()));// revisar
		} catch (IOException e) {
			connected = false;
			vc.habilitarConexion();
			e.printStackTrace();
		}

	}

	public void enviarMensajeAlServidor(Usuario u) {

		try {

			oos.writeObject(u);

		} catch (IOException e) {
			connected = false;
			vc.habilitarConexion();
			e.printStackTrace();
		}

	}

}
