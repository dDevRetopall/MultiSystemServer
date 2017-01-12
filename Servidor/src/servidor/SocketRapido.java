package servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import comun.Comandos;

public class SocketRapido {
	private Socket s;
	InputStream is;
	ObjectInputStream ois;
	public SocketRapido(Socket s){
		this.s = s;
		
		try {
			ois = new ObjectInputStream(is);
			is = s.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(){
			public void run(){
				while(true){
					try {
						Object o =ois.readObject();
						System.out.println(o);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}.start();;
	}

}
