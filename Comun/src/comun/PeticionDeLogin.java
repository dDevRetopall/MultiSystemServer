package comun;

import java.io.Serializable;

public class PeticionDeLogin implements Serializable{
	private String username;
	private String password;

	public PeticionDeLogin(String username,String password){
		this.username = username;
		this.password = password;
		
		
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
}
