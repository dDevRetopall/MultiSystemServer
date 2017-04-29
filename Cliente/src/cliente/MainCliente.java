package cliente;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import comun.Constantes;
import comun.Mensaje;

public class MainCliente {
	static VentanaDeServidores vs;
	public static void main(String[] args) {
		
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
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
		
		
		Connection con=ConnectionSQLDangerous.getConnection();
		ArrayList<String>data=ConnectionSQLDangerous.getDataOfServers(con);
		vs = new VentanaDeServidores(con);
		vs.rellenar(data);
		vs.setVisible(true);
//		String resultado=JOptionPane.showInputDialog("Introduce la IP a la que te quieres conectar", "localhost");
//		Constantes.HOST=resultado;
		
		//
		

	}

}
