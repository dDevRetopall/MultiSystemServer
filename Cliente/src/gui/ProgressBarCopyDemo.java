package gui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
 
public class ProgressBarCopyDemo {
 
    public CopyThread copyThread = null;
 
    public ProgressBarCopyDemo() {
 
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Loading components");
        shell.setSize(450, 200);
 
        shell.setLayout(null);
 
        ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
        progressBar.setBounds(10, 23, 350, 17);
 
        Label labelInfo = new Label(shell,SWT.ABORT);
        labelInfo.setBounds(10, 46, 350, 20);
        labelInfo.setText(" ...");
        copyThread=new CopyThread(display, progressBar, labelInfo);
       
      
 
        shell.open();
 
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
 
        display.dispose();
    }
 
 
 
}