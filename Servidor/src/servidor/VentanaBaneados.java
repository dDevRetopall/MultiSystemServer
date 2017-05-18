package servidor;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class VentanaBaneados extends JFrame{
	JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JTextArea ta = new JTextArea(15,40);

	public VentanaBaneados() {
		this.setSize(600, 400);

		this.setTitle("Baneados");
		
		this.setLocationRelativeTo(null);

		this.setContentPane(p);
		ta.setEditable(false);
		ta.setText("Baneados"+"\n");
		JScrollPane scroll= new JScrollPane(ta);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		p.add(scroll);
		this.setVisible(true);
		
		for(String ip:ListaNegra.ipaddress){
		ta.setText(ta.getText()+ip+"\n");
		}
	}

}
