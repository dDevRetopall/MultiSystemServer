package comun;

import java.io.Serializable;
import java.util.ArrayList;

public class Tabla implements Serializable{
	ArrayList <String> ips = new ArrayList();
	ArrayList <String> names = new ArrayList();
	ArrayList <Integer> ports = new ArrayList();
	ArrayList <String> passwords = new ArrayList();
	
	public Tabla (ArrayList<String>ips,ArrayList<String>names,ArrayList<Integer>ports,ArrayList<String>passwords){
		this.ips=ips;
		this.names=names;
		this.ports=ports;
		this.passwords=passwords;

	}

	public ArrayList<String> getIps() {
		return ips;
	}

	public ArrayList<String> getNames() {
		return names;
	}

	public ArrayList<Integer> getPorts() {
		return ports;
	}

	public ArrayList<String> getPasswords() {
		return passwords;
	}
	
}
