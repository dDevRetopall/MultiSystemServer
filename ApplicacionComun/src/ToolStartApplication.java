import java.io.IOException;

public class ToolStartApplication {
	public static void startApp(String nombre){
		try {
			Runtime.getRuntime().exec("java -jar "+nombre+".jar");
			System.out.println("Iniciando "+nombre);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
