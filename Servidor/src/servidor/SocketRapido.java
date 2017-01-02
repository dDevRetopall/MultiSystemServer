package servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import comun.Comandos;

public class SocketRapido {
	private Socket s;

	public SocketRapido(Socket s){
		this.s = s;
		
	}
	public void enviarMensajeRapido(Comandos c){
		
		try {
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(os);
			oos.writeObject(c);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
