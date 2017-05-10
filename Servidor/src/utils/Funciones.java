package utils;

import javax.swing.JOptionPane;

public class Funciones {
	public static void repartirFunciones(String funcion, String data) {

		if (funcion.equals(ConstantesServer.nameAdvanced)) {// Nombre de la
															// funcion
			if (data.equals("false")) {// Valor 1
				ConstantesServer.advanced = false;
				System.out.println("Change to easy");
			} else if (data.equals("true")) {// Valor 2
				ConstantesServer.advanced = true;
				System.out.println("Change to advanced");
			} else {// Excepcion error default option
				System.err.println("Error while trying to read settings file. Default mode: advanced");
				System.err.println("Hay informacion de el setting " + ConstantesServer.nameAdvanced
						+ " que está corrupta. Reparalo o elimina el archivo para restablecer la configuracion");
				int option = JOptionPane.showConfirmDialog(null,
						"Hay informacion de el setting " + ConstantesServer.nameAdvanced
								+ " que está corrupta. Reparalo o elimina el archivo para restablecer la configuracion. \n ¿Desea continuar? Se cogera la configuracion anterior.",
						"Hay algun fallo en el archivo settings.ini", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					// No se hace nada
					System.out.println("Ejecutando este setting con configuracion anterior");
					ConstantesServer.advanced = ConstantesServer.defaultAdvanced;
				} else {
					System.exit(0);
				}
			}
		}
		if (funcion.equals(ConstantesServer.nameAnonymous)) {// Nombre de la
			// funcion
			if (data.equals("false")) {// Valor 1
				ConstantesServer.anonymous = false;
				System.out.println("Change to normal");
			} else if (data.equals("true")) {// Valor 2
				ConstantesServer.anonymous = true;
				System.out.println("Change to anonymous");
			} else {// Excepcion error default option
				System.err.println("Error while trying to read settings file. Default mode: normal");
				System.err.println("Hay informacion de el setting " + ConstantesServer.nameAdvanced
						+ " que está corrupta. Reparalo o elimina el archivo para restablecer la configuracion");
				int option = JOptionPane.showConfirmDialog(null,
						"Hay informacion de el setting " + ConstantesServer.nameAnonymous
								+ " que está corrupta. Reparalo o elimina el archivo para restablecer la configuracion. \n ¿Desea continuar? Se cogera la configuracion anterior.",
						"Hay algun fallo en el archivo settings.ini", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					// No se hace nada
					System.out.println("Ejecutando este setting con configuracion anterior");
					ConstantesServer.anonymous = ConstantesServer.defaultAnonymous;
				} else {
					System.exit(0);
				}
			}
		}
		if (funcion.equals(ConstantesServer.nameSecurity)) {// Nombre de la
			// funcion
			if (data.equals("false")) {// Valor 1
				ConstantesServer.advancedSecurity = false;
				System.out.println("Change to medium security");
			} else if (data.equals("true")) {// Valor 2
				ConstantesServer.advancedSecurity = true;
				System.out.println("Change to high security");
			} else {// Excepcion error default option
				System.err.println("Error while trying to read settings file. Default mode: high security");
				System.err.println("Hay informacion de el setting " + ConstantesServer.nameSecurity
						+ " que está corrupta. Reparalo o elimina el archivo para restablecer la configuracion");
				int option = JOptionPane.showConfirmDialog(null,
						"Hay informacion de el setting " + ConstantesServer.nameSecurity
								+ " que está corrupta. Reparalo o elimina el archivo para restablecer la configuracion. \n ¿Desea continuar? Se cogera la configuracion anterior.",
						"Hay algun fallo en el archivo settings.ini", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					// No se hace nada
					System.out.println("Ejecutando este setting con configuracion anterior");
					ConstantesServer.advancedSecurity = ConstantesServer.defaultSecurity;
				} else {
					System.exit(0);
				}
			}
		}
	}
}
