
/****************************************
 * Programa diseñado por Diego Berrocal 
 * Caracteristicas:
 * 
 * -MySQL
 * -Login
 * -Register
 * -Ban and unban
 * -Kick
 * -Clear
 * -AntiSpam
 * -User window
 * -Chat
 * -Ban and user list
 * -Server window				
 * 
 ****************************************/

package servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import comun.Comandos;
import comun.Constantes;
import comun.Mensaje;
import comun.Profile;
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

		// Pongo la interfaz de WINDOWS 10

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

			// Leo del archivo todas las ips baneadas
			ListaNegra.loadList();

			// Creo una entrada al servidor
			sc = new ServerSocket(Constantes.PORT);

			// Abro una ventana del Servidor
			vs = new VentanaServidor();

			// Inicializo la conexion con la base de datos MySQL
			crearMySQL();

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
						int contador = 0;

						// Acepto clientes que se conecten
						s = sc.accept();

						// Recorro todos los clientes conectados y veo si alguna
						// ip se repite. Si esto no pasa creo un cliente con el
						// socket
						// que se ha aceptado
						for (Cliente c2 : clientes) {
							if (c2.connected) {
								if (s.getInetAddress().getHostAddress()
										.equals(c2.s.getInetAddress().getHostAddress())) {

									// s.close();

									System.out.println("Ya hay un cliente corriendo en esa ip");
									// contador++;
								}
							}
						}
						if (contador == 0) {
							Cliente c = new Cliente(s);
							clientes.add(c);
						}

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

		// Los mensajes de texto les pongo cuando se han enviado con un prefijo
		// y sufijo especial

		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();

		String strDate = sdfDate.format(now);
		vs.getTa().setText(vs.getTa().getText() + strDate + "  " + m.getMensaje() + "\n");
		// formatea el mensaje
		Mensaje m2 = new Mensaje(strDate + "  " + m.getMensaje() + "\n");

		// Guardo ese mensaje en la data temporal para cuando se conecte otro
		// cliente pueda ver los otros mensajes
		Loader.mensajes.add(m2);

		// Envio el mensaje formateado a todos los clientes
		for (Cliente c : clientes) {
			c.enviarMensaje(m2);

		}
	}

	public static void enviarMensajeATodos(Usuarios usuariosNombres) {

		// Envio el array de usuarios a todos
		for (Cliente c : clientes) {
			System.out.println("Enviando lista de usuarios a "+c.getUsuario());
			c.enviarMensaje(usuariosNombres);

		}

	}

	public static void buscarSocket(String username) {
		int c2 = 0;

		// Miro si empieza algun usuario con el texto que ha puesto el servidor
		// para kickearle (Le elimino de todos los clientes y de las listas)

		for (Cliente c : clientes) {
			if (!c.getUsuario().isEmpty()) {
				System.out.println("Buscando socket del usuario " + c.getIpUsuario() + " <-***-> " + c.getUsuario());
				if (c.getUsuario().startsWith(username)) {
					c.kick();
					System.out.println("Se va a kickeado a " + c.getUsuario());
					clientes.remove(c);
					u.usuariosNombre.remove(c.getUsuario());
					enviarMensajeATodos(u);
					c2++;
					break;// no obligatorio
				}
			}
		}
		if (c2 == 0) {
			System.out.println("No se ha encontrado a esa persona ");
		} else {
			System.out.println("Se ha(n) encontrado a " + c2 + " persona(s)");
		}
	}

	public static void buscarSocketYBanear(String username) {

		// Miro si empieza algun usuario con el texto que ha puesto el servidor
		// para kickearle y luego banearle y no dejarle entrar en el servidor
		// (Le elimino de todos los clientes y de las listas)
		
		int c2 = 0;
		for (Cliente c : clientes) {
			if (!c.getUsuario().isEmpty()) {
				if (c.getUsuario().startsWith(username)) {
					int respuesta=JOptionPane.showConfirmDialog(null, "Seguro que quieres banear a "+c.getUsuario()+"con la ip "+c.getIpUsuario());
					if(respuesta==JOptionPane.YES_OPTION){
					c.ban();
					c.kick();

					clientes.remove(c);
					u.usuariosNombre.remove(c.getUsuario());
					enviarMensajeATodos(u);
					c2++;
					break;// no obligatorio
				}
			}}
		}
		if (c2 == 0) {
			System.out.println("No se ha encontrado a esa persona para banearla");
		} else {
			System.out.println("Se ha(n) encontrado a " + c2 + " persona(s) para banear");
		}
	}

	public static void buscarSocketYDesBanear(String ip) {

		// Miro si empieza algun usuario con el texto que ha puesto el servidor
		// para desbanearle
		// Para ello le elimino la ip de la lista negra y guardo la lista en el
		// archivo de ips (banned.ip)

		int c2 = 0;

		System.out.println(ip);
		ListaNegra.ipaddress.remove(ip);
		System.out.println("Se ha desbaneado a " + ip);
		enviarMensajeATodos(new Mensaje(ip + " has been unbanned"));
		ListaNegra.saveList();

	}

	public static void eliminarData() {

		// Limpio todos los mensajes de la clase que almacena todos los
		// mennsajes y envio un mensaje diciendo que
		// se ha limpiado a todos los clientes
		// y les borro todo lo que habia antes
		String[] botones = { "A los clientes", "Al Servidor y clientes" };
		int respuesta = JOptionPane.showOptionDialog(null, "A quien hacemos la limpieza de mensajes", "Advertencia",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, botones, botones[0]);
		if (respuesta == 1 /* Al Servidor */ ) {
			vs.getTa().setText("Servidor ejecutando" + "\n");
			System.out.println("Limpieza->Mensajes del Servidor limpiados");
			MainServidor.vs.getTa()
					.setText(MainServidor.vs.getTa().getText() + "Limpieza->Mensajes del Servidor limpiados " + "\n");
		}
		System.out.println("Limpieza->Eliminando todos los mensajes");
		MainServidor.vs.getTa()
				.setText(MainServidor.vs.getTa().getText() + "Limpieza->Eliminando todos los mensajes" + "\n");
		Loader.mensajes.clear();

		for (Cliente c : clientes) {
			c.enviarMensaje(new Comandos(true));
			c.enviarMensaje(new Mensaje("Se han limpiado todos los mensajes e informacion por el Servidor" + "\n"));
			if (c.getUsuario().isEmpty()) {
				System.out.println("Limpieza->Eliminando mensajes de un SOCKET");
				MainServidor.vs.getTa().setText(
						MainServidor.vs.getTa().getText() + "Limpieza->Eliminando mensajes de un SOCKET" + "\n");
			} else {
				System.out.println("Limpieza->Eliminando mensajes de " + c.getUsuario());
				MainServidor.vs.getTa().setText(MainServidor.vs.getTa().getText() + "Limpieza->Eliminando mensajes de "
						+ c.getUsuario() + "\n");
			}

		}
		System.out.println("Limpieza->Limpieza terminada correctamente");
		MainServidor.vs.getTa()
				.setText(MainServidor.vs.getTa().getText() + "Limpieza->Limpieza terminada correctamente" + "\n");

	}

	public static boolean existeUsuario(String usuario) {

		// Devuelve un boolean si el usuario existe en la lista de usuarios o no

		Iterator i = MainServidor.u.usuariosNombre.iterator();
		while (i.hasNext()) {

			if (i.next().equals(usuario)) {
				return true;
			}
		}
		return false;
	}

	public static void crearMySQL() {

		// Se sincronizan el archivo donde se guardan las cuentas(profiles.info)
		GestionUsuarios.loadProfiles();

		// Se crea un conexion a la base de datos
		Connection con = ConnectionSQL.getConnection();

		// Se intenta crear una tabla si esta no existe
		// Si no existe se intenta rellenar con los datos de las cuentas locales
		// guardadas en el archivo de cuentas(profiles.info)
		ConnectionSQL.createTable(con);

		// Se mira si existe la base de datos
		boolean existe = ConnectionSQL.existeTablaLogin(con);

		if (existe) {

			// Si existe se sincroniza la informacion de la base de datos con el
			// archivo (profiles.info)
			// Para ello se coje una lista con la informacion que se encuentra
			// en la base de datos
			// Luego se limpia el archivo y se va almacenando la informacion
			// (saverProfile())
			ArrayList<String> datos = ConnectionSQL.getUsuariosDeMySQL(con);
			GestionUsuarios.clear();
			for (int i = 0; i < (datos.size()); i += 2) {

				System.out.println("MySQL->Sincronizando " + (i + 1) + "/" + datos.size() + "\n");
				MainServidor.vs.getTa().setText(MainServidor.vs.getTa().getText() + "MySQL->Sincronizando " + (i + 1)
						+ "/" + datos.size() + "\n");
				System.out.println("MySQL->Sincronizando " + (i + 2) + "/" + datos.size() + "\n");
				MainServidor.vs.getTa().setText(MainServidor.vs.getTa().getText() + "MySQL->Sincronizando " + (i + 2)
						+ "/" + datos.size() + "\n");
				GestionUsuarios.saveProfile(new Profile(datos.get(i), datos.get(i + 1)));
			}
			System.out.println("MySQL->Datos sincronizados");
			MainServidor.vs.getTa().setText(MainServidor.vs.getTa().getText() + "MySQL->Datos sincronizados" + "\n");
		}
		// ConnectionSQL.close(con);
	}

}
