package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.eclipse.swt.SWT;

import comun.Constantes;

public class VentanaDeServidores extends JFrame {
	Loading load;
	ArrayList<String> passwords = new ArrayList<>();
	JPanelBackground pPrincipal = new JPanelBackground(new BorderLayout());
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
	JPanel generalNorth = new JPanel(new BorderLayout());
	JCheckBox box = new JCheckBox("View header", false);
	DefaultTableModel modelo = new DefaultTableModel();
	JLabel system = new JLabel("System");
	JTable tabla = new JTable(modelo) {
		public boolean isCellEditable(int rowIndex, int colIndex) {

			return false; // Las celdas no son editables.

		}
	};
	JScrollPane scrollpane = new JScrollPane(tabla);
	private Connection con;

	public VentanaDeServidores(Connection con) {
		pPrincipal.setBackground(("fondo.jpg"));
		this.con = con;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(900, 600);

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
		
		north.add(cargando);
		north.add(update);
		

		generalNorth.add(box, BorderLayout.WEST);
		generalNorth.add(north, BorderLayout.EAST);
		generalNorth.setOpaque(false);

		tabla.setBackground(Color.black);
		box.setForeground(Color.WHITE);

		tabla.setForeground(Color.white);
		tabla.getTableHeader().setBackground(Color.BLACK);

		pPrincipal.setOpaque(false);
		tabla.setOpaque(false);
		north.setOpaque(false);
		central.setOpaque(false);
		box.setOpaque(false);

		pPrincipal.add(central, BorderLayout.CENTER);
		pPrincipal.add(otros, BorderLayout.SOUTH);

		pPrincipal.add(generalNorth, BorderLayout.NORTH);
		tabla.getTableHeader().setOpaque(false);
	
		tabla.setDragEnabled(false);
		tabla.setCellSelectionEnabled(false);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.setBackground(Color.black);
		scrollpane.getViewport().setBackground(Color.black);
		tabla.setFillsViewportHeight(true);
		scrollpane.setViewportView(tabla);
		// tabla.setPtabla.setFillsViewportHeight(true);referredScrollableViewportSize(tabla.getPreferredSize());

		// tabla.setPreferredScrollableViewportSize(table.getPreferredSize());
		box.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED){
					tabla.getTableHeader().setOpaque(true);
					tabla.updateUI();
				}else{
					tabla.getTableHeader().setOpaque(false);
					tabla.updateUI();
				}

			}
		});
		Bcon.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if ((!tf.getText().isEmpty()) && (!tf2.getText().isEmpty())) {
					ArrayList<String> data = ConnectionSQLDangerous.getDataOfServers(con);
					for (int fila = 0; fila < tabla.getRowCount(); fila++) {

						if (tf.getText().equals(tabla.getValueAt(fila, 2))
								&& tf2.getText().equals(tabla.getValueAt(fila, 4))) {
							String value = (String) tabla.getValueAt(fila, 3);
							if (value.equals("Publico")) {
								Constantes.HOST = (String) (tabla.getValueAt(fila, 2));
								Constantes.PORT = Integer.parseInt((String) (tabla.getValueAt(fila, 4)));
								VentanaCliente vc = new VentanaCliente((String) (tabla.getValueAt(fila, 0)));
								vc.te3.setEditable(false);
								return;
							} else if (value.equals("Privado")) {

								char[] password = pw.getPassword();
								String passwordCompleta = "";
								for (int i = 0; i < password.length; i++) {
									passwordCompleta = passwordCompleta + password[i];

								}
								if (!passwordCompleta.isEmpty()) {
									if ((passwordCompleta.equals(passwords.get(fila)))) {
										Constantes.HOST = (String) (tabla.getValueAt(fila, 2));
										Constantes.PORT = Integer.parseInt((String) (tabla.getValueAt(fila, 4)));
										VentanaCliente vc = new VentanaCliente((String) (tabla.getValueAt(fila, 0)));
										vc.te3.setEditable(false);
										pw.setText("");
										return;
									} else {
										JOptionPane.showMessageDialog(VentanaDeServidores.this,
												"Contraseña incorrecta");
										System.out.println("Contraseña incorrecta");
										return;
									}
								} else {
									JOptionPane.showMessageDialog(VentanaDeServidores.this,
											"El servidor es privado y por lo tanto requiere contraseña");
									System.out.println("El servidor es privado y por lo tanto requiere contraseña");
									return;
								}
							}
						}

					}

					// -----------------FASE DE PRUEBAS-----------------\\
					// Hacer que se pueda hacer un servidor fantasma (Que nos se
					// vea en la lista)->Y le puedas poner un nombre especial
					System.out.println("Intentando establecer conexion con una IP desconocida a la base de datos");

					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {

							ImageIcon loading = new ImageIcon(getClass().getResource("loading.gif"));
							load = new Loading("Trying to connect", loading, "Connecting");

							load.addWindowListener(new WindowAdapter() {

								public void windowOpened(WindowEvent e) {
									Socket s = null;
									try {

										s = new Socket(tf.getText(), Integer.parseInt(tf2.getText()));

										Constantes.HOST = tf.getText();
										Constantes.PORT = Integer.parseInt(tf2.getText());
										VentanaCliente vc = new VentanaCliente("Unknown Server");
										vc.te3.setEditable(false);

									} catch (UnknownHostException e1) {
										System.out.println("ERROR while trying to connect to the server");
										load.setVisible(false);

										JOptionPane.showMessageDialog(VentanaDeServidores.this,
												"Error while trying to connect to the Server");

									} catch (IOException e1) {
										load.setVisible(false);
										System.out.println("ERROR while trying to connect to the server");
										JOptionPane.showMessageDialog(VentanaDeServidores.this,
												"Error while trying to connect to the Server");
									}
									try {
										s.close();
										s.shutdownInput();
										s.shutdownOutput();
									} catch (IOException e1) {
										System.out.println("Error al eliminar el socket");
										e1.printStackTrace();
									}
								}
							});

						}
					});
					t.run();

					// --------------------------------------------------\\
				}
			}
		});
		// (!pw.getPassword().toString().isEmpty())
		update.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				double tiempoInicio = System.currentTimeMillis();
				cargando.setText("Actualizando");
				cargando.setForeground(Color.RED);
				ArrayList<String> data = ConnectionSQLDangerous.getDataOfServers(con);
				MainCliente.vs.rellenar(data);
				double tiempoActual = System.currentTimeMillis();
				double tiempoFinal = (tiempoActual - tiempoInicio) / 1000d;
				cargando.setForeground(new Color(0, 125, 0));
				cargando.setText("Actualizado " + tiempoFinal + "s");
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
					vc.te3.setEditable(false);
				} else if (value.equals("Privado")) {
					JPanel panel = new JPanel();
					JLabel label = new JLabel("Enter a password:");
					JPasswordField pass = new JPasswordField(10);
					panel.add(label);
					panel.add(pass);
					String[] options = new String[] { "Accept", "Cancel" };
					int option = JOptionPane.showOptionDialog(null, panel, "Password to enter to the chat",
							JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, panel);
					if (option == 0) // pressing OK button
					{
						char[] password = pass.getPassword();
						String passwordCompleta = "";
						for (int i = 0; i < password.length; i++) {
							passwordCompleta = passwordCompleta + password[i];

						}
						if (passwordCompleta.equals(passwords.get(fila))) {
							Constantes.HOST = (String) (tabla.getValueAt(fila, 2));
							Constantes.PORT = Integer.parseInt((String) (tabla.getValueAt(fila, 4)));
							VentanaCliente vc = new VentanaCliente((String) (tabla.getValueAt(fila, 0)));
							vc.te3.setEditable(false);
						} else {
							JOptionPane.showMessageDialog(null, "Contraseña erronea del servidor");
						}
					} else {

					}

				}
			}

		});
	}

	public void limpiarTabla() {
		try {

			int filas = tabla.getRowCount();
			for (int i = 0; filas > i; i++) {
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
