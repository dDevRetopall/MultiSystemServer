package gui;

import java.io.File;
 
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
 
public class CopyThread  {
 
    private Display display;
    private ProgressBar progressBar;
   
    private Label labelInfo;

 
    public CopyThread(Display display, ProgressBar progressBar, //
            Label labelInfo) {
        this.display = display;
        this.progressBar = progressBar;
       
        this.labelInfo = labelInfo;
    }
 
   public void update(String mensaje,int value,int count){
	   
   
        if (display.isDisposed()) {
            return;
        }
        
        display.asyncExec(new Runnable() {
        	 
            @Override
            public void run() {
                labelInfo.setText(mensaje);
                progressBar.setMaximum(count);
                progressBar.setSelection(value);
            }
        });
       // updateTime();
        
   }
 
    private void updateTime() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }
 
    
 
   
 
   
}