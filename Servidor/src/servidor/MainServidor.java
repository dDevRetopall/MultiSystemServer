package servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import comun.Comandos;
import comun.Constantes;
import comun.Mensaje;
import comun.Usuarios;

public class MainServidor {
	static Socket s;
	static Thread t2;
	static Thread t3;
	static Usuarios u;
	static VentanaServidor vs;
	static ServerSocket sc;
	static ArrayList<Cliente> clientes = new ArrayList<>();

	public static void main(String[] args) {
		u = new Usuarios();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			sc = new ServerSocket(Constantes.PORT);
			vs = new VentanaServidor();
			vs.getTa().setText(vs.getTa().getText() + "Servidor ejecutando" + "\n");
			System.out.println("Servidor ejecutando");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {

						s = sc.accept();
						Cliente c = new Cliente(s);
						clientes.add(c);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		t2.start();

	}

	public static void enviarMensajeATodos(Mensaje m) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();

		String strDate = sdfDate.format(now);
		vs.getTa().setText(vs.getTa().getText() + strDate + "  " + m.getMensaje() + "\n");
		// formatea el mensaje
		Mensaje m2 = new Mensaje(strDate + "  " + m.getMensaje() + "\n");
		Loader.mensajes.add(m2);
		for (Cliente c : clientes) {
			c.enviarMensaje(m2);

		}
	}

	public static void enviarMensajeATodos(Usuarios usuariosNombres) {

		for (Cliente c : clientes) {
			c.enviarMensaje(usuariosNombres);

		}

	}

	public static void buscarSocket(String username) {
		int c2 = 0;
		for (Cliente c : clientes) {

			if (c.getUsuario().startsWith(username)) {
				c.kick();
				clientes.remove(c);
				u.usuariosNombre.remove(c.getUsuario());
				enviarMensajeATodos(u);
				c2++;
			}
		}
		if (c2 == 0) {
			System.out.println("No se ha encontrado a esa persona ");
		} else {
			System.out.println("Se ha(n) encontrado a " + c2 + " persona(s)");
		}
	}

	public static void eliminarData() {
		vs.getTa().setText("Servidor ejecutando" + "\n");
		Loader.mensajes.clear();
		for (Cliente c : clientes) {
			c.enviarMensaje(new Comandos(true));
			c.enviarMensaje(new Mensaje("Se ha borrado DATA por el Servidor" + "\n"));

		}

	}

	public static boolean existeUsuario(String usuario) {
		Iterator i = MainServidor.u.usuariosNombre.iterator();
		while (i.hasNext()) {

			if (i.next().equals(usuario)) {
				return true;
			}
		}
		return false;
	}

}
