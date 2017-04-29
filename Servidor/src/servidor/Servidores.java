package servidor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;

import comun.Constantes;

public class Servidores {
	int[] puertosUsados;
	static int contadorServidores;

	public static void añadirServidor(Connection con) {

		String contador = (ConnectionSQL.returnData("NumServidores"));
		contadorServidores = Integer.parseInt(contador);
		contadorServidores++;
		String contadorAumentado = Integer.toString(contadorServidores);
		ConnectionSQL.editSetting(con, "NumServidores", contadorAumentado, 2);
		String pwd = "";
		int option = JOptionPane.showConfirmDialog(MainServidor.vs, "Desea poner password al Servidor");
		if (option == JOptionPane.YES_OPTION) {
			String p = JOptionPane.showInputDialog("Password del Servidor");
			pwd = p;
		}
		InetAddress localHost = null;
		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionSQL.addServerSetting("SV" + contadorServidores, "SV" + contadorServidores, localHost.getHostAddress(),
				Integer.toString((Constantes.PORT)), pwd);
		MainServidor.vs.setTitle("Servidor SV" + contadorServidores);

	}

	public static void eliminarServidor(Connection con, String ipServer) {
		try {
			
			String query = "delete from settings where ip = ?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, ipServer);

			preparedStmt.executeUpdate();
			System.out.println("Servidor eliminado");
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}

	}
}
// addSetting("NumServidores","1"); //pos 2
//
//
// System.out.println(MainServidor.sc.isBound());
//
//

// System.out.println("Name : "+localHost.getHostName());
// System.out.println("Address : "+localHost.getHostAddress());

// String contador = (returnData("NumServidores"));
// int contadorv2=Integer.parseInt(contador);
// contadorv2++;
// String contadorAumentado= Integer.toString(contadorv2);
// editSetting(con, "NumServidores", contadorAumentado,2);
// InetAddress localHost = null;
// try {
// localHost = InetAddress.getLocalHost();
// } catch (UnknownHostException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
// String pwd="";
// int option=JOptionPane.showConfirmDialog(null, "Desea poner password al
// Servidor");
// if(option==JOptionPane.YES_OPTION){
// String p=JOptionPane.showInputDialog("Password del Servidor");
// pwd=p;
// }
//
//
// addServerSetting("SV"+contadorAumentado,
// "Server"+contadorAumentado,localHost.getHostAddress(),Integer.toString((Constantes.PORT)),pwd);
//////////////////////////////////////////////////////////////////
//// Hacer tambien setting del puerto y cuando se cierra la ventana del servidor
// quitar servidor
//// Hacer que pueda elegir el cliente el servidor al que conectarse con
// jcomobobox
//// IP del server no funiona