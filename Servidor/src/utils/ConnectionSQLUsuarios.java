package utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import servidor.GestionUsuarios;
import servidor.MainServidor;

public class ConnectionSQLUsuarios {
	public static boolean escribirMensajesEnConsola = false;
	private static String url;
	private static String usuario;
	private static String password;
	private static Connection con;
	private static Statement st;

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
		return con;

	}

	public static void escribirMensaje(String mensaje) {
		String prefijo = "[ MySQL ] ";
		System.out.println(prefijo + mensaje);
		if (escribirMensajesEnConsola) {
			MainServidor.escribirEnServidorMensajeCustom(prefijo+mensaje);
			
		}

	}

	public static void crearTablaUsuarios(String nombreTabla, String[] tipoTabla) {

		try {
			st = con.createStatement();
			if (!existeTabla(nombreTabla)) {
				st.executeUpdate("CREATE TABLE "+ nombreTabla+" (" + "id INT AUTO_INCREMENT, " + "PRIMARY KEY(id), "
						+ "usuario VARCHAR(20), " + "password VARCHAR(50), " + "rango VARCHAR(10))");

				escribirMensaje("Tabla de usuarios creada");

			} else {
				escribirMensaje("No se ha creado la tabla porque ya existe");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<String> getUsuariosDeMySQL(Connection con, String nombreTabla) {
		ResultSet rs;

		ArrayList<String> datos = new ArrayList<>();
		escribirMensaje("Cogiendo informacion de la base de datos para sincronizar con el archivo.");
		try {
			rs = st.executeQuery("select * from " + nombreTabla);
			String user, pwd, rango;
			int posicion = 0;
			while (rs.next()) {
				user = rs.getString(2);
				datos.add(user);
				posicion++;
				pwd = rs.getString(3);
				datos.add(pwd);
				posicion++;
				rango = rs.getString(4);
				datos.add(rango);
				posicion++;
			}
			escribirMensaje("Datos cogidos correctamente.");
		} catch (SQLException e1) {
			escribirMensaje("Error mientras se intentaban coger los datos.");
			e1.printStackTrace();
		}
		return datos;

	}

	public static boolean existeTabla(String nombreTabla) {
		boolean existe = false;

		ResultSet rs;

		try {
			DatabaseMetaData dbmd = con.getMetaData();
			rs = dbmd.getTables(null, null, null, null);

			while (rs.next()) {
				String tabla = rs.getObject(3).toString();
				if (tabla.equals(nombreTabla)) {
					existe = true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return existe;
	}

	public static boolean añadirUsuario(String nombreTabla, String username, String password, String rango) {
		
		try {
			st.executeUpdate("INSERT INTO " + nombreTabla + " (" + "usuario, " + "password, " + "rango" + ")" + "VALUES ("
					+ "'" + username + "','" + password + "','" + rango + "')");
			escribirMensaje("Se ha insertado el usuario " + username + " con su password encriptada " + password);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static boolean consultarUsuario(String nombreTabla,String username, String password) {
		String passwordEncriptada=EncriptarPasswords.encriptarPassword(password);
		
		String user, pwd;
		try {
			ResultSet rs = st.executeQuery("select * from "+nombreTabla);
			while (rs.next()) {
				user = rs.getString(2);
				pwd = rs.getString(3);
				System.out.println(pwd+"  ->  "+passwordEncriptada);
				if (user.equals(username) && pwd.equals(passwordEncriptada)) {
					escribirMensaje("Usuario aceptado " + username);
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		escribirMensaje("Usuario incorrecto"+ username);
		return false;

	}
	public static String devolverRangoUsuario(String nombreTabla, String username) {
		String passwordEncriptada=EncriptarPasswords.encriptarPassword(password);
		
		String rank,user;
		try {
			ResultSet rs = st.executeQuery("select * from "+nombreTabla);
			while (rs.next()) {
				user = rs.getString(2);
				if(username.equals(user)){
					rank = rs.getString(4);
					return rank;
				}
			}
				
				
				
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	public Connection getConnection(){
		return con;
	}
	public static void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}
}
