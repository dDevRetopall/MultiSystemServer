package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import comun.Constantes;

public class VentanaDeServidores extends JFrame {
	ArrayList<String>passwords = new ArrayList<>();
	JPanel pPrincipal = new JPanel(new BorderLayout());
	JPanel central = new JPanel();
	JPanel otros = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPasswordField pw = new JPasswordField(8);
	JTextField tf = new JTextField(8);
	JTextField tf2 = new JTextField(5);
	JLabel cargando = new JLabel();
	JButton Bcon = new JButton("Conectarse");
	JLabel l1 = new JLabel("Ip");
	JLabel l2 = new JLabel("Password");
	JLabel l3 = new JLabel("Puerto");
	JButton update = new JButton("Actualizar");
	DefaultTableModel modelo = new DefaultTableModel();
	JTable tabla = new JTable(modelo) {
		public boolean isCellEditable(int rowIndex, int colIndex) {

			return false; // Las celdas no son editables.

		}
	};
	JScrollPane scrollpane = new JScrollPane(tabla);
	private Connection con;

	public VentanaDeServidores(Connection con) {
		this.con = con;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 600);

		this.setTitle("Servidores disponibles");
		this.setContentPane(pPrincipal);
		this.setLocationRelativeTo(null);

		modelo.addColumn("ID");
		modelo.addColumn("Nombre Server");
		modelo.addColumn("Ip");
		modelo.addColumn("Tipo");
		modelo.addColumn("Puerto");

		central.add(scrollpane);

		otros.add(l1);
		otros.add(tf);
		otros.add(l3);
		otros.add(tf2);
		otros.add(l2);
		otros.add(pw);
		otros.add(Bcon);
		
		north.add(update);
		north.add(cargando);
		
		pPrincipal.add(central,BorderLayout.CENTER);
		pPrincipal.add(otros, BorderLayout.SOUTH);
		pPrincipal.add(north,BorderLayout.NORTH);
		update.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				double tiempoInicio=System.currentTimeMillis();
				cargando.setText("Actualizando");
				cargando.setForeground(Color.RED);
				ArrayList<String>data=ConnectionSQLDangerous.getDataOfServers(con);
				MainCliente.vs.rellenar(data);
				double tiempoActual=System.currentTimeMillis();
				double tiempoFinal=(tiempoActual-tiempoInicio)/1000d;
				cargando.setForeground(new Color(0,125,0));
				cargando.setText("Actualizado "+tiempoFinal+"s");
				System.out.println("MySQL->Actualizada la informacion");
				
			}
			
		});
		tabla.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int fila = tabla.rowAtPoint(e.getPoint());
				String value = (String) tabla.getValueAt(fila, 3);
				if (value.equals("Publico")) {
					Constantes.HOST = (String) (tabla.getValueAt(fila, 2));
					Constantes.PORT = Integer.parseInt((String) (tabla.getValueAt(fila, 4)));
					VentanaCliente vc = new VentanaCliente((String) (tabla.getValueAt(fila, 0)));
				} else if (value.equals("Privado")) {
					JPanel panel = new JPanel();
					JLabel label = new JLabel("Enter a password:");
					JPasswordField pass = new JPasswordField(10);
					panel.add(label);
					panel.add(pass);
					String[] options = new String[] { "OK", "Cancel" };
					int option = JOptionPane.showOptionDialog(null, panel, "Password to enter to the chat",
							JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, panel);
					if (option == 0) // pressing OK button
					{
						char[] password = pass.getPassword();
						String passwordCompleta = "";
						for (int i = 0; i < password.length; i++) {
							passwordCompleta = passwordCompleta + password[i];

						}
						if(passwordCompleta.equals(passwords.get(fila))){
							Constantes.HOST = (String) (tabla.getValueAt(fila, 2));
							Constantes.PORT = Integer.parseInt((String) (tabla.getValueAt(fila, 4)));
							VentanaCliente vc = new VentanaCliente((String) (tabla.getValueAt(fila, 0)));
						}else{
							JOptionPane.showMessageDialog(null, "Contraseña erronea del servidor");
						}
					}else{
						
					}
					
				}
			}

		});
	}
	public void limpiarTabla(){
        try {
            
            int filas=tabla.getRowCount();
            for (int i = 0;filas>i; i++) {
                modelo.removeRow(i);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }
	public void rellenar(ArrayList<String> datos) {
		limpiarTabla();
		int contador = 0;

		for (int i = 0; i < datos.size() / 4; i++) {

			Object[] fila = new Object[5];
			fila[0] = datos.get(contador);
			
			fila[1] = datos.get(contador);
			
			contador++;
			fila[2] = datos.get(contador);
			
			contador++;
			if (datos.get(contador).isEmpty()) {
				fila[3] = "Publico";
			} else {
				fila[3] = "Privado";
			}
			passwords.add(datos.get(contador));

			contador++;
			fila[4] = datos.get(contador);
			
			contador++;
			modelo.addRow(fila);
		}

	}
}
