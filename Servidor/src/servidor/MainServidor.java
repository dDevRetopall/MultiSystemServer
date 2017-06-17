
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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import comun.Comandos;
import comun.Constantes;
import comun.Mensaje;
import comun.Profile;
import comun.Usuarios;
import utils.ConnectionSQLUsuarios;
import utils.ConstantesServer;
import utils.FileUtils;

public class MainServidor {
	static Socket s;
	static Thread t2;
	static Thread t3;
	static Usuarios u;
	static VentanaServidor vs;
	static ServerSocket sc;
	static ArrayList<Cliente> clientes = new ArrayList<>();
	static Connection con;
	static InetAddress localHost = null;

	public static void main(String[] args) {

		System.out.println("This program is made by Diego Berrocal. All right reserved");

		u = new Usuarios();

		// Pongo la interfaz de Windows 10 o la de el sistema operativo que se
		// este usando

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
			vs.deshabilitarBotones();
			// Leo los settings del archivo inicial
			FileUtils.readSettings();
			System.out.println("Advanced mode is " + ConstantesServer.advanced);
			escribirEnServidorMensajeDeSettings("Advanced mode is " + ConstantesServer.advanced);
			System.out.println("Anonymous mode is " + ConstantesServer.anonymous);
			escribirEnServidorMensajeDeSettings("Anonymous mode is " + ConstantesServer.anonymous);
			if (ConstantesServer.advancedSecurity) {
				System.out.println("Security mode is high");
				escribirEnServidorMensajeDeSettings("Security mode is high");
			} else {
				System.out.println("Security mode is medium");
				escribirEnServidorMensajeDeSettings("Security mode is medium");
			}

			try {
				localHost = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				System.out.println("Unknown host exception");
				e.printStackTrace();

			}

			vs.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					System.out.println("Se esta cerrando la ventana");
					Servidores.eliminarServidor(con, MainServidor.localHost.getHostAddress());

				}

			});

			// Se conecta a la base de datos
			ConnectionSQLUsuarios.escribirMensajesEnConsola=true;
			con = ConnectionSQLUsuarios.connect();
			

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			vs.dispatchEvent(new WindowEvent(vs, WindowEvent.WINDOW_CLOSING));
			System.exit(0);
		}

		// ******************************************//
		// Contraseña administrador

		ConnectionSQL.createTableForSettings(con);

		JPanel panel = new JPanel();
		JLabel label = new JLabel("Enter a password:");
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[] { "Accept", "Cancel" };
		int option = JOptionPane.showOptionDialog(MainServidor.vs, panel, "Password to enter the server",
				JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, panel);
		if (option == 0) // pressing OK button
		{
			char[] password = pass.getPassword();
			String passwordCompleta = "";
			for (int i = 0; i < password.length; i++) {
				passwordCompleta = passwordCompleta + password[i];

			}
			boolean resultado = ConnectionSQL.hacerConsultaDeSetting("passAdmin", passwordCompleta);
			if (!resultado) {
				System.out.println("Contraseña incorrecta");
				escribirEnServidorMensajeDeMySQL("Contraseña incorrecta");
				vs.dispatchEvent(new WindowEvent(vs, WindowEvent.WINDOW_CLOSING));
				System.exit(0);
			} else {
				System.out.println("Contraseña aprobada");
				escribirEnServidorMensajeDeMySQL("Contraseña aprobada");
				vs.habilitarBotones();
			}

		} else {
			vs.dispatchEvent(new WindowEvent(vs, WindowEvent.WINDOW_CLOSING));
			System.exit(0);
		}

		if (ConstantesServer.anonymous) {
			vs.setTitle("Anonymous Server");
		}

		// ******************************************//
		// Añado Servidor
		if (!ConstantesServer.anonymous) {
			Servidores.añadirServidor(con);
		}

		// Inicializo la conexion con la base de datos MySQL
		cargarMySQL();

		escribirEnServidorMensajeEspecial("Servidor ejecutando");
		System.out.println("Servidor ejecutando");

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
							if (c2.connected && !c2.getUsuario().isEmpty()) {
								if (s.getInetAddress().getHostAddress()
										.equals(c2.s.getInetAddress().getHostAddress())) {
									// Sistema de seguridad-Mejorar y activar
									// cuando se pueda
									if (ConstantesServer.advancedSecurity) {

										c2.connected = false;
										s.close();

									}
									System.out.println("Ya hay un cliente corriendo en esa ip");
									System.out.println("Bloqueada una entrada de el cliente "+s.getInetAddress().getHostAddress()+"porque ya hay uno corriendo con esa ip");
									if (ConstantesServer.advancedSecurity) {
										contador++;

									}

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

	public static void enviarMensajeATodos(Mensaje m, boolean esMensajeDeServidor) {
		System.out.println("Mensaje del Servidor? " + esMensajeDeServidor);
		// Los mensajes de texto les pongo cuando se han enviado con un prefijo
		// y sufijo especial

		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();

		String strDate = sdfDate.format(now);

		/// Saltar de linea
		int longitudMax = 80;
		String mensajeFinal = "";
		String mensajeSimplificado = "";
		String prefijoServidor = "<Server>";
		if (m.getMensaje().length() + prefijoServidor.length() * 2 + strDate.length() > longitudMax) {
			int espacios = (int) (m.getMensaje().length() / longitudMax);
			if (esMensajeDeServidor) {
				vs.getTa().setText(vs.getTa().getText() + strDate + "  " + prefijoServidor + "\n");
				mensajeFinal = mensajeFinal + strDate + "  " + prefijoServidor + "\n";
			} else {
				vs.getTa().setText(vs.getTa().getText() + strDate + "\n");
				mensajeFinal = mensajeFinal + strDate + "\n";
			}
			for (int i = 1; i <= espacios; i++) {
				mensajeSimplificado = m.getMensaje().substring((i - 1) * longitudMax, i * longitudMax);
				vs.getTa().setText(vs.getTa().getText() + mensajeSimplificado + "\n");
				mensajeFinal = mensajeFinal + mensajeSimplificado + "\n";
			}

			mensajeSimplificado = m.getMensaje().substring(longitudMax * espacios, m.getMensaje().length());

			if (esMensajeDeServidor) {
				vs.getTa().setText(vs.getTa().getText() + mensajeSimplificado + "   " + prefijoServidor + "\n");
				mensajeFinal = mensajeFinal + mensajeSimplificado + "  " + prefijoServidor + "\n";
			} else {
				vs.getTa().setText(vs.getTa().getText() + mensajeSimplificado + "   " + "\n");
				mensajeFinal = mensajeFinal + mensajeSimplificado + "\n";
			}

		} else {
			if (esMensajeDeServidor) {
				vs.getTa().setText(vs.getTa().getText() + strDate + "  " + prefijoServidor + "  " + m.getMensaje()
						+ "  " + prefijoServidor + "\n");
				mensajeFinal = mensajeFinal + strDate + "  " + prefijoServidor + "  " + m.getMensaje() + "  "
						+ prefijoServidor + "\n";
			} else {
				vs.getTa().setText(vs.getTa().getText() + strDate + "  " + m.getMensaje() + "  " + "\n");
				mensajeFinal = mensajeFinal + strDate + "  " + m.getMensaje() + "\n";
			}
		}

		// que baje automaticamente la barra
		vs.scroll.getVerticalScrollBar().setValue(vs.scroll.getVerticalScrollBar().getMaximum());
		// formatea el mensaje

		// Guardo ese mensaje en la data temporal para cuando se conecte otro
		// cliente pueda ver los otros mensajes
		Loader.mensajes.add(new Mensaje(mensajeFinal));

		// Envio el mensaje formateado a todos los clientes
		for (Cliente c : clientes) {
			c.enviarMensaje(new Mensaje(mensajeFinal));

		}
	}

	public static void escribirEnServidorMensajeEspecial(String mensaje) {
		// Si el setting avanzado esta activado

		vs.getTa().setText(vs.getTa().getText() + mensaje + "\n");
		vs.scroll.getVerticalScrollBar().setValue(vs.scroll.getVerticalScrollBar().getMaximum());

	}

	public static void escribirEnServidorMensajeDeMySQL(String mensaje) {
		// Si el setting avanzado esta activado
		if (ConstantesServer.advanced) {
			vs.getTa().setText(vs.getTa().getText() + "MySQL-> " + mensaje + "\n");
			vs.scroll.getVerticalScrollBar().setValue(vs.scroll.getVerticalScrollBar().getMaximum());
		}
	}

	public static void escribirEnServidorMensajeDeSettings(String mensaje) {
		// Si el setting avanzado esta activado
		if (ConstantesServer.advanced) {
			vs.getTa().setText(vs.getTa().getText() + "Settings-> " + mensaje + "\n");
			vs.scroll.getVerticalScrollBar().setValue(vs.scroll.getVerticalScrollBar().getMaximum());
		}
	}

	public static void escribirEnServidorMensajeDeLimpieza(String mensaje) {
		// Si el setting avanzado esta activado
		if (ConstantesServer.advanced) {
			vs.getTa().setText(vs.getTa().getText() + "Limpieza-> " + mensaje + "\n");
			vs.scroll.getVerticalScrollBar().setValue(vs.scroll.getVerticalScrollBar().getMaximum());
		}
	}
	public static void escribirEnServidorMensajeCustom(String mensaje){
		if (ConstantesServer.advanced) {
			
			vs.getTa().setText(vs.getTa().getText()  + mensaje + "\n");
			vs.scroll.getVerticalScrollBar().setValue(vs.scroll.getVerticalScrollBar().getMaximum());
		}
	}

	public static void enviarMensajeATodos(Usuarios usuariosNombres) {

		// Envio el array de usuarios a todos
		for (Cliente c : clientes) {
			System.out.println("Tratando de enviar lista de usuarios a " + c.getUsuario());
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
					int respuesta = JOptionPane.showConfirmDialog(MainServidor.vs,
							"Seguro que quieres banear a " + c.getUsuario() + " con la ip " + c.getIpUsuario());
					if (respuesta == JOptionPane.YES_OPTION) {

						c.kick();
						c.ban();

						clientes.remove(c);
						u.usuariosNombre.remove(c.getUsuario());
						enviarMensajeATodos(u);
						c2++;
						break;// no obligatorio
					}
				}
			}
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
		if(ListaNegra.ipaddress.contains(ip)){
			ListaNegra.ipaddress.remove(ip);
			System.out.println("Se ha desbaneado a " + ip);
			enviarMensajeATodos(new Mensaje(ip + " has been unbanned"), false);
			ListaNegra.saveList();
		}else{
			System.out.println("No se ha podido desbanear a ese usuario porque no existe");
		}
		

	}

	public static void eliminarData() {

		// Limpio todos los mensajes de la clase que almacena todos los
		// mennsajes y envio un mensaje diciendo que
		// se ha limpiado a todos los clientes
		// y les borro todo lo que habia antes
		String[] botones = { "A los clientes", "Al Servidor y clientes" };
		int respuesta = JOptionPane.showOptionDialog(MainServidor.vs, "A quien hacemos la limpieza de mensajes",
				"Advertencia", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, botones, botones[0]);
		if (respuesta == 1 /* Al Servidor */ ) {
			vs.getTa().setText("Servidor ejecutando" + "\n");
			System.out.println();
			escribirEnServidorMensajeDeLimpieza("Mensajes del Servidor limpiados");
		} else if (respuesta == 0 || respuesta == 1) {
			System.out.println("Limpieza->Eliminando todos los mensajes");
			escribirEnServidorMensajeDeLimpieza("Eliminando todos los mensajes");
			Loader.mensajes.clear();

			for (Cliente c : clientes) {
				c.enviarMensaje(new Comandos(true));
				c.enviarMensaje(new Mensaje("Se han limpiado todos los mensajes e informacion por el Servidor" + "\n"));
				if (c.getUsuario().isEmpty()) {
					System.out.println("Limpieza->Eliminando mensajes de un SOCKET");
					escribirEnServidorMensajeDeLimpieza("Eliminando mensajes de un socket");
				} else {
					System.out.println("Limpieza->Eliminando mensajes de " + c.getUsuario());
					escribirEnServidorMensajeDeLimpieza("Eliminando mensajes de " + c.getUsuario());
				}

			}
			System.out.println("Limpieza->Limpieza terminada correctamente");
			escribirEnServidorMensajeDeLimpieza("Limpieza terminada correctamente");
		} else if (respuesta != 0 && respuesta != 1) {
			System.out.println("Se ha cancelado la operacion");
		}
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

	public static void cargarMySQL() {
		// Connection con = ConnectionSQL.getConnection();
		// Se sincronizan el archivo donde se guardan las cuentas(profiles.info)
		GestionUsuarios.loadProfiles();

		// Se intenta crear una tabla si esta no existe
		// Si no existe se intenta rellenar con los datos de las cuentas locales
		// guardadas en el archivo de cuentas(profiles.info)
		ConnectionSQLUsuarios.crearTablaUsuarios(ConstantesServer.tablaUsuariosPredeterminada, null);

		// Se mira si existe la base de datos
		boolean existe = ConnectionSQLUsuarios.existeTabla(ConstantesServer.tablaUsuariosPredeterminada);

		System.out.println(existe);
		
	/*SINCRONIZACION*/
		if (existe) {

			// Si existe se sincroniza la informacion de la base de datos con el
			// archivo (profiles.info)
			// Para ello se coje una lista con la informacion que se encuentra
			// en la base de datos
			// Luego se limpia el archivo y se va almacenando la informacion
			// (saverProfile())
			ArrayList<String> datos = ConnectionSQLUsuarios.getUsuariosDeMySQL(con,ConstantesServer.tablaUsuariosPredeterminada);
			GestionUsuarios.clear();
			for (int i = 0; i < (datos.size()); i += 2) {

				System.out.println("MySQL->Sincronizando " + (i + 1) + "/" + datos.size() + "\n");
				escribirEnServidorMensajeDeMySQL("Sincronizando " + (i + 1) + "/" + datos.size());
				System.out.println("MySQL->Sincronizando " + (i + 2) + "/" + datos.size() + "\n");
				escribirEnServidorMensajeDeMySQL("Sincronizando " + (i + 2) + "/" + datos.size());
				GestionUsuarios.saveProfile(new Profile(datos.get(i), datos.get(i + 1)));
			}
			System.out.println("MySQL->Datos sincronizados");
			escribirEnServidorMensajeDeMySQL("Datos sincronizados");
		}
		
		/*SINCRONIZACION*/
		
		// ConnectionSQL.close(con);
	}

}
