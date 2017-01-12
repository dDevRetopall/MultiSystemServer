package comun;

import java.io.Serializable;

public class Comandos implements Serializable {
	public boolean borrarData = false;
	public boolean existeUsuario;
	public boolean ense�arOptionPane;
	public String mensaje;
	public boolean habilitarBotonConexion;

	public Comandos(boolean borrarData) {
		this.borrarData = borrarData;

	}

	public Comandos(boolean existeUsuario, boolean ense�arOptionPane, String mensaje) {
		this.existeUsuario = existeUsuario;
		this.ense�arOptionPane = ense�arOptionPane;
		this.mensaje = mensaje;

	}

	public Comandos(boolean habilitarBotonConexion, boolean ense�arOptionPane) {
		this.habilitarBotonConexion = habilitarBotonConexion;
		this.ense�arOptionPane = ense�arOptionPane;

	}

	public Comandos(boolean ense�arOptionPane, String mensaje) {

		this.ense�arOptionPane = ense�arOptionPane;
		this.mensaje = mensaje;

	}

}
