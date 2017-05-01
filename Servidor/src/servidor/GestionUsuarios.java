package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import comun.Profile;
import utils.EncriptarPasswords;

public class GestionUsuarios {
	private static ArrayList<String> profiles = new ArrayList<>();
	static String nombreArchivo="profiles.info";
	public static void register(String username, String password) {
		for (int i = 0; i < profiles.size(); i += 2) {
			if (username.equals(profiles.get(i))) {
				System.err.println("No se ha podido registrar");
				return;
			} else {

			}
		}
		System.out.println("Se ha conseguido registrarte");
		String passwordEncriptada=EncriptarPasswords.encriptarPassword(password);
		ConnectionSQL.addProfile(username, passwordEncriptada);
		saveProfile(new Profile(username, passwordEncriptada));
	}

	public static void login(String username, String password) {
		for (int i = 0; i < profiles.size(); i += 2) {
			if (username.equals(profiles.get(i))) {
				if (password.equals(profiles.get(i + 1))) {
					System.out.println("Se ha logueado correctamente");
					return;
				} else {
					System.out.println("Password incorrecta");
					return;
				}
			}

		}
		System.out.println("No se ha encontrado ese usuario");
	}

	public static void loadProfiles() {
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
				while ((linea = br.readLine()) != null) {
					profiles.add(linea);
					System.out.println(linea);
				}
			}else{
				System.out.println("Se ha creado un nuevo archivo porque no se encontraba");
				archivo.createNewFile();
			}
		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public static ArrayList<String> getProfiles() {
		return profiles;
	}
	public static void clear() {
		profiles.clear();
		System.out.println("Archivo vaciado");
	}

	public static void saveProfile(Profile p) {
		try {
			profiles.add(p.getUsername());
			profiles.add(p.getPassword());
			FileWriter fw = new FileWriter(nombreArchivo);
			PrintWriter pw = new PrintWriter(fw);
			System.out.println("Se esta guardando el la informacion");
			for (String s : profiles) {
				pw.println(s);
			}
			fw.close();
			System.out.println("Completado..");
			System.out.println("Se ha guardado el nuevo registro con usuario " + p.getUsername() + " y con password "
					+ p.getPassword());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
