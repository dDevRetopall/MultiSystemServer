package cliente;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class ConnectionSQLDangerous {
	public static Connection getConnection() {
		Connection con = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://minecraft236.omgserv.com:3306/minecraft_107543";
			String usuario = "minecraft_107543";
			String password = "pinkifamo";
		
			
			try {
				con = DriverManager.getConnection(url, usuario, password);

				System.out.println("MySQL->Tratando de conectar");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		System.out.println("MySQL->Conexion creada correctamente a la base de datos MySQL");
	
		return con;
	}
	public static ArrayList<String> getDataOfServers(Connection con){
		ArrayList<String>datos = new ArrayList<>();
		Statement st = null;
		try {
			st = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	
			ResultSet rs;
			try {
				rs = st.executeQuery("select * from settings");
				while (rs.next()) {
					
					if(rs.getString(2).startsWith("SV")){
						
						
						datos.add(rs.getString(3));
						datos.add(rs.getString(4));
						datos.add(rs.getString(5));
						datos.add(rs.getString(6));
					}
					
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return datos;
			
	
		
	}


}