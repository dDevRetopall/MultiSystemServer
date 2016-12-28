package comun;

import java.io.Serializable;

public class Comandos implements Serializable{
	public  boolean borrarData=false;
	
	public Comandos(boolean borrarData){
		this.borrarData = borrarData;
		
	}
}
