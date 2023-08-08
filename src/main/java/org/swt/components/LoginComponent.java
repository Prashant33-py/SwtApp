package org.swt.components;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class LoginComponent {
    private final Composite composite;
    private final Shell shell;
    private static final String VALID_USERNAME = "abc";
    private static final String VALID_PASSWORD = "123";
    private final Logger logger = LogManager.getLogger();
    public LoginComponent(Shell shell) {
        this.composite = new Composite(shell, SWT.NONE);
        this.shell = shell;
    }

    public void createLoginComponent(){
        composite.setLayout(new GridLayout(1, false));
        composite.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,true,false));

        Label title = new Label(composite, SWT.NONE);
        title.setText("Login");
        Font font = new Font(composite.getDisplay(), "Helvetica", 20,SWT.BOLD);
        title.setFont(font);
        title.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Composite loginComposite = new Composite(composite, SWT.NONE);
        loginComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        loginComposite.setLayout(new GridLayout(2, false));

        Label usernameLabel = new Label(loginComposite, SWT.NONE);
        usernameLabel.setText("Username:");
        usernameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Text usernameText = new Text(loginComposite, SWT.SINGLE | SWT.BORDER);
        usernameText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Label passwordLabel = new Label(loginComposite, SWT.NONE);
        passwordLabel.setText("Password:");
        passwordLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Text passwordText = new Text(loginComposite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
        passwordText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Button loginButton = new Button(composite, SWT.PUSH);
        loginButton.setText("Login");
        loginButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));

        loginButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String username = usernameText.getText();
                String password = passwordText.getText();

                if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
                    logger.info("{} logged in successfully!",username);
                    composite.dispose();
                    HomeComponent homeComponent = new HomeComponent(shell, logger);
                    try {
                        try {
                            homeComponent.createHomeComponent();
                        } catch (IOException | SAXException ex) {
                            throw new RuntimeException(ex);
                        }
                    } catch (ParserConfigurationException ex) {
                        throw new RuntimeException(ex);
                    }
                    shell.layout();
                } else {
                    logger.error("Unable to login because of incorrect password.");
                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                    messageBox.setText("Error");
                    messageBox.setMessage("Invalid username or password");
                    messageBox.open();
                }
            }
        });
    }
}