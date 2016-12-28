package comun;

import java.io.Serializable;

public class Mensaje implements Serializable{
	private String mensaje="";
	private String usuario;
	private boolean conectado;

	public Mensaje(String mensaje){
		this.mensaje = mensaje;
		
	}
	public Mensaje(String usuario,boolean conectado){
		String mensaje;
		if(conectado){
			mensaje = usuario +" se ha conectado";
		}else{
			mensaje = usuario +" se ha desconectado";
		}
		this.mensaje=mensaje;
		
		
		
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
