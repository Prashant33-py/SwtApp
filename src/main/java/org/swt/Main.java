package org.swt;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swt.components.LoginComponent;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Login - SWT App");
        shell.setSize(500, 320);
        shell.setLayout(new GridLayout(1,false));

        LoginComponent loginComponent = new LoginComponent(shell);
        loginComponent.createLoginComponent(Locale.getDefault());

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}