package comun;

import java.io.Serializable;

public class Comandos implements Serializable{
	public  boolean borrarData=false;
	public boolean existeUsuario;
	public boolean ense�arOptionPane;
	
	public Comandos(boolean borrarData){
		this.borrarData = borrarData;
		
	}
	public Comandos(boolean existeUsuario,boolean ense�arOptionPane){
		this.existeUsuario = existeUsuario;
		this.ense�arOptionPane = ense�arOptionPane;
		
		
	}
}
