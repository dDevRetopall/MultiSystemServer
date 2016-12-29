package comun;

import java.io.Serializable;

public class Comandos implements Serializable{
	public  boolean borrarData=false;
	public boolean existeUsuario;
	public boolean enseñarOptionPane;
	
	public Comandos(boolean borrarData){
		this.borrarData = borrarData;
		
	}
	public Comandos(boolean existeUsuario,boolean enseñarOptionPane){
		this.existeUsuario = existeUsuario;
		this.enseñarOptionPane = enseñarOptionPane;
		
		
	}
}
