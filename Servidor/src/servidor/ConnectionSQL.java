package servidor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;


import comun.Profile;

public class ConnectionSQL {
	private static Statement st;

	public static Connection getConnection() {
		Connection con = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://minecraft236.omgserv.com:3306/minecraft_107543";
			String usuario = "minecraft_107543";
			String password = "pinkifamo";
			System.out.println("MySQL->Leyendo datos");
			MainServidor.vs.getTa().setText(MainServidor.vs.getTa().getText() + "MySQL->Leyendo datos" + "\n");

			try {
				con = DriverManager.getConnection(url, usuario, password);

				System.out.println("MySQL->Tratando de conectar");
				MainServidor.vs.getTa()
						.setText(MainServidor.vs.getTa().getText() + "MySQL->Tratando de conectar" + "\n");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		System.out.println("MySQL->Conexion creada correctamente a la base de datos MySQL");
		MainServidor.vs.getTa().setText(MainServidor.vs.getTa().getText()
				+ "MySQL->Conexion creada correctamente a la base de datos MySQL" + "\n");
		return con;
	}

	
	//////////////////////Settings////////////////////////////	
	
	public static void createTableForSettings(Connection con){
		//incluye contraseña de administrador ,puerto
		try {

			st = con.createStatement();
			// ResultSet ts = st.executeQuery("select * from login");
			// ResultSetMetaData rsmd= ts.getMetaData();
			DatabaseMetaData dbmd = con.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, null, null);
			boolean existe = false;
			while (rs.next()) {
				String tabla = rs.getObject(3).toString();
				if (tabla.equals("settings")) {
					existe = true;
				}
			}

			// st.executeUpdate(
			// "SELECT * FROM INFORMATION_SCHEMA.TABLES"+
			//
			// "WHERE TABLE_SCHEMA = '"+"dbo"+"' AND TABLE_NAME =
			// '"+"login"+"'");
			//
			if (!existe) {
				st.executeUpdate("CREATE TABLE settings (" + "id INT AUTO_INCREMENT, " + "PRIMARY KEY(id), "
						+ "nombreAjuste VARCHAR(20), " + "informacion VARCHAR(20)) ");
				System.out.println("MySQL->Tabla de usuarios creada");
				MainServidor.vs.getTa()
						.setText(MainServidor.vs.getTa().getText() + "MySQL->Tabla de usuarios creada" + "\n");
				addSetting("passAdmin", "default");
				
			} else {
				System.out.println("MySQL->No se ha creado la tabla porque ya existe" + "\n");
				MainServidor.vs.getTa().setText(
						MainServidor.vs.getTa().getText() + "MySQL->No se ha creado la tabla porque ya existe" + "\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean hacerConsultaDeSetting(String idSetting, String informacion) {
		String id, data;
		try {
			ResultSet rs = st.executeQuery("select * from settings");
			while (rs.next()) {
				id = rs.getString(2);
				data = rs.getString(3);
				if (id.equals(idSetting) && data.equals(informacion)) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;

	}
	public static void editSetting(Connection con,String id, String data) {
		try {
			PreparedStatement ps;
			String sql = "UPDATE settings SET nombreAjuste=?, informacion=? WHERE id=?;";
			ps = con.prepareStatement(sql);
			ps.setString(1,id);
			ps.setString(2,data);
			ps.setInt(3,1);
			ps.executeUpdate();
			System.out.println("MySQL->Se ha editado el ajuste " + id + " con su informacion");
			MainServidor.vs.getTa().setText(MainServidor.vs.getTa().getText() + "MySQL->Se ha editado el ajuste "
					+ id + " con su informacion" + "\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	
	public static void addSetting(String id, String data) {
		try {
			st.executeUpdate("INSERT INTO settings (" + "nombreAjuste, " + "informacion)" + "VALUES (" + "'" + id + "','"
					+ data + "' )");
			System.out.println("MySQL->Se ha insertado el ajuste " + id + " con su informacion");
			MainServidor.vs.getTa().setText(MainServidor.vs.getTa().getText() + "MySQL->Se ha insertado el ajuste "
					+ id + " con su informacion" + "\n");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	
		
	//////////////////////Login////////////////////////////	
		
	public static void createTable(Connection con) {

		try {

			st = con.createStatement();
			// ResultSet ts = st.executeQuery("select * from login");
			// ResultSetMetaData rsmd= ts.getMetaData();
			DatabaseMetaData dbmd = con.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, null, null);
			boolean existe = false;
			while (rs.next()) {
				String tabla = rs.getObject(3).toString();
				if (tabla.equals("login")) {
					existe = true;
				}
			}

			// st.executeUpdate(
			// "SELECT * FROM INFORMATION_SCHEMA.TABLES"+
			//
			// "WHERE TABLE_SCHEMA = '"+"dbo"+"' AND TABLE_NAME =
			// '"+"login"+"'");
			//
			if (!existe) {
				st.executeUpdate("CREATE TABLE login (" + "id INT AUTO_INCREMENT, " + "PRIMARY KEY(id), "
						+ "usuario VARCHAR(20), " + "password VARCHAR(20)) ");
				System.out.println("MySQL->Tabla de usuarios creada");
				MainServidor.vs.getTa()
						.setText(MainServidor.vs.getTa().getText() + "MySQL->Tabla de usuarios creada" + "\n");
				String username = "";
				String password = "";
				for (int i = 0; i < GestionUsuarios.getProfiles().size(); i++) {

					if (i % 2 == 0) {
						username = GestionUsuarios.getProfiles().get(i);
					} else {
						password = GestionUsuarios.getProfiles().get(i);
						ConnectionSQL.addProfile(username, password);
						password = "";
						username = "";
					}

				}
			} else {
				System.out.println("MySQL->No se ha creado la tabla porque ya existe" + "\n");
				MainServidor.vs.getTa().setText(
						MainServidor.vs.getTa().getText() + "MySQL->No se ha creado la tabla porque ya existe" + "\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static ArrayList<String> getUsuariosDeMySQL(Connection con) {
		ResultSet rs;

		ArrayList<String> datos = new ArrayList<>();
		System.out.println("MySQL->Cogiendo informacion de la base de datos para sincronizar con el archivo." + "\n");
		MainServidor.vs.getTa()
		.setText(MainServidor.vs.getTa().getText() + "MySQL->Cogiendo informacion de la base de datos para sincronizar con el archivo" + "\n");
		try {
			rs = st.executeQuery("select * from login");
			String user, pwd;
			int posicion = 0;
			while (rs.next()) {
				user = rs.getString(2);
				datos.add(user);
				posicion++;
				pwd = rs.getString(3);
				datos.add(pwd);
				posicion++;
			}
			System.out.println("MySQL->Datos cogidos correctamente" + "\n");
			MainServidor.vs.getTa()
					.setText(MainServidor.vs.getTa().getText() + "MySQL->Datos cogidos correctamente" + "\n");
		} catch (SQLException e1) {
			System.out.println("MySQL->Error cuando se intentaban coger los datos" + "\n");
			MainServidor.vs.getTa()
					.setText(MainServidor.vs.getTa().getText() + "MySQL->Error cuando se intentaban coger los datos" + "\n");
			e1.printStackTrace();
		}
		return datos;
	
		
		
	
	}

	public static boolean existeTablaLogin(Connection con) {
		ResultSet rs;
		try {
			st = con.createStatement();
			DatabaseMetaData dbmd = con.getMetaData();
			rs = dbmd.getTables(null, null, null, null);

			while (rs.next()) {
				String tabla = rs.getObject(3).toString();
				if (tabla.equals("login")) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static void addProfile(String username, String password) {
		try {
			st.executeUpdate("INSERT INTO login (" + "usuario, " + "password)" + "VALUES (" + "'" + username + "','"
					+ password + "' )");
			System.out.println("MySQL->Se ha insertado el usuario " + username + " con su password");
			MainServidor.vs.getTa().setText(MainServidor.vs.getTa().getText() + "MySQL->Se ha insertado el usuario "
					+ username + " con su password" + "\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void close(Connection c) {
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean hacerConsulta(String username, String password) {
		String user, pwd;
		try {
			ResultSet rs = st.executeQuery("select * from login");
			while (rs.next()) {
				user = rs.getString(2);
				pwd = rs.getString(3);
				if (user.equals(username) && pwd.equals(password)) {
					System.out.println("MySQL->Usuario aceptado " + username);
					MainServidor.vs.getTa()
							.setText(MainServidor.vs.getTa().getText() + "MySQL->Usuarios aceptado " + username + "\n");
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("MySQL->Usuario incorrecto " + username);
		MainServidor.vs.getTa()
				.setText(MainServidor.vs.getTa().getText() + "MySQL->Usuarios incorrecto " + username + "\n");
		return false;

	}
}
