package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class UI {
	Display display = new Display();
	Shell shell = new Shell(display);
	Label label1 = new Label(shell, SWT.NULL);

	public UI() {
		shell.setText("Loading components");
		shell.setSize(450, 200);
		ProgressBar progressBar3 = new ProgressBar(shell, SWT.INDETERMINATE);

		label1.setText("Loading");
		progressBar3.setBounds(140, 70, 200, 20);
		label1.setBounds(10, 10, 120, 20);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();

	}

	public void update(String mensaje) {
		label1.setText(mensaje);
	}

}