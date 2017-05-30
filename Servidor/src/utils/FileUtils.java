package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
import javax.swing.plaf.synth.SynthSpinnerUI;

public class FileUtils {
	//// Settings
	static String nombreArchivo = "settings.ini";

	public static void readSettings() {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		System.out.println("Tratando de leer usuarios");
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).

			archivo = new File(nombreArchivo);
			if (archivo.exists()) {
				fr = new FileReader(archivo);
				br = new BufferedReader(fr);

				// Lectura del fichero
				String linea;
				int contador = 0;
				while ((linea = br.readLine()) != null) {
					try {
						String[] textos = linea.split("=");
						String settingId = textos[0].trim();
						String settingData = textos[1].trim();
						Funciones.repartirFunciones(settingId, settingData);
						System.out.println("Se ha leido una linea con " + settingId + " y " + settingData);
						contador++;
					} catch (ArrayIndexOutOfBoundsException e) {
						String[]opciones={"Si","No","Restablecer settings"};
						System.err.println(
								"Hay algun fallo en el archivo settings.ini. Reparalo o eliminalo para restablecer la configuracion");						
						int option =   JOptionPane.showOptionDialog(null,
								"Repare el archivo o eliminelo para restablecer la configuracion. \n ¿Desea continuar? Se cogera la configuracion anterior.",
								"Hay algun fallo en el archivo settings.ini",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
//						int option = JOptionPane.showConfirmDialog(null,"Repare el archivo o eliminelo para restablecer la configuracion. \n ¿Desea continuar? Se cogera la configuracion anterior.",
//								"Hay algun fallo en el archivo settings.ini", JOptionPane.YES_NO_OPTION);
						if (option ==0) {
							// No se hace nada
							System.out.println("Ejecutando con configuracion anterior");
						} else if(option==1){
			
							System.exit(0);
						}else if(option==2){
							System.out.println("Restableciendo configuracion");
							writeDefaultSettings();
							System.exit(0);
						}else{
							System.exit(0);
						}
					}
				}

			} else {
				System.out.println("Se ha creado un nuevo archivo porque no se encontraba");
				archivo.createNewFile();
				writeDefaultSettings();
			}
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	// Cuando se añada un nuevo ajuste hacer lo siguiente
	// 1-Añadir en funciones (explicado ahi)
	// 2-Añadir en MainServidor la funcion arriba para que apareza en consola y
	// ejecutar donde se necesite (con if)
	// 3-Escribir en el array default settings las constantes siguientes
	// ConstantesServer -> (default)+(id con primera letra mayuscula)
	// Escribir en ConstantesServer el nombre de la variable -> (default)+(id
	// con primera letra mayuscula) y la id como informacion

	// Work in Encrypt db in a file(high security) and the server password
	// Hacer que ponga que se ha desconectado cuando salga del chat no cuando se
	// salga de la aplicacion-ERROR

	public static void writeDefaultSettings () {
		String[] defaultSettings = { 
				/* 1 */(ConstantesServer.nameAdvanced + "=" + ConstantesServer.defaultAdvanced),
				/* 2 */(ConstantesServer.nameAnonymous + "=" + ConstantesServer.defaultAnonymous),
				/* 3 */(ConstantesServer.nameSecurity + "=" + ConstantesServer.defaultSecurity) };
		FileWriter fw = null;
		try {
			fw = new FileWriter(nombreArchivo);
		} catch (IOException e1) {
			System.out.println("Error tratando de leer el archivo " + FileUtils.nombreArchivo);
			e1.printStackTrace();
			return;

		}
		PrintWriter pw = new PrintWriter(fw);
		System.out.println("Se esta guardando el la informacion");
		for (String s : defaultSettings) {
			pw.println(s);
		}
		try {
			fw.close();
		} catch (IOException e) {
			System.out.println("Error tratando de cerrar el archivo " + FileUtils.nombreArchivo);

			e.printStackTrace();
			return;
		}
		System.out.println("Completado..");
	}
}
