package servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

public class ListaNegra {
	public static  HashSet <String>ipaddress= new HashSet<>();
	
	public static void loadList(){
		File f = new File("banned.ip");
		
		try {
			System.out.println("Tratando de leer las ips baneadas");
			FileInputStream fo = new FileInputStream(f);
			ObjectInputStream oo = new ObjectInputStream(fo);
			HashSet<String> lista = (HashSet<String>) oo.readObject();
			oo.close();
			fo.close();
			ipaddress = lista;
			return;
		} catch (FileNotFoundException e) {
			try {
				System.out.println("Se ha creado un nuevo archivo porque no se encontraba");
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ipaddress = new HashSet<>();
	}
	
	public static void saveList(){
		File f = new File("banned.ip");
		try {
			FileOutputStream fo = new FileOutputStream(f);
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			oo.writeObject(ipaddress);
			oo.close();
			fo.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}

