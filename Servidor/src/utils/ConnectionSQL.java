package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import servidor.MainServidor;

public class ConnectionSQL {
	private static boolean escribirMensajesEnConsola = false;
	private static String url;
	private static String usuario;
	private static String password;
	private static Connection con;
	
	public static Connection connect() {
	    con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			url = "jdbc:mysql://minecraft236.omgserv.com:3306/minecraft_107543";
			usuario = "minecraft_107543";
			password = "pinkifamo";

			escribirMensaje("Leyendo datos");

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		try {
			escribirMensaje("Tratando de conectar");
			con = DriverManager.getConnection(url, usuario, password);
			escribirMensaje("Conexion creada correctamente a la base de datos MySQL");
			
		} catch (SQLException e) {
			escribirMensaje("No se ha podido conectar a la base de datos");
			e.printStackTrace();
		}
		return null;

	}

	public static void escribirMensaje(String mensaje) {
		String prefijo = "[ MySQL ] ";
		System.out.println(prefijo + mensaje);
		if (escribirMensajesEnConsola) {
			MainServidor.escribirEnServidorMensajeCustom(mensaje);
		}

	}
	public static void crearTabla(String nombreTabla,String[]tipoTabla){
		
	}
}
