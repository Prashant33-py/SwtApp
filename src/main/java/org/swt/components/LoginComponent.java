package org.swt.components;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.swt.model.Users;
import org.swt.util.Language;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class LoginComponent {
    private final Composite composite;
    private final Shell shell;
    private final Logger logger = LogManager.getLogger();
    public final static List<Users> users = new ArrayList<>();
    public static Users currentUser;
    private Label title;
    private Label usernameLabel;
    private Label passwordLabel;
    private Button loginButton;
    private Locale newLocale;
    private Button showHideButton;

    public LoginComponent(Shell shell) {
        this.composite = new Composite(shell, SWT.NONE);
        this.shell = shell;
        Users user1 = new Users();
        user1.setUserName("admin");
        user1.setPassword("admin123");
        user1.setRole("admin");

        Users user2 = new Users();
        user2.setUserName("bot1");
        user2.setPassword("bot135");
        user2.setRole("bot");

        Users user3 = new Users();
        user3.setUserName("bot2");
        user3.setPassword("bot246");
        user3.setRole("bot");

        users.addAll(Arrays.asList(user1, user2, user3));
    }

    public void createLoginComponent(Locale currentLocale) {
        Composite languageComposite = new Composite(composite, SWT.END);
        languageComposite.setLayout(new GridLayout(3, false));
        languageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Label languageLabel = new Label(languageComposite, SWT.NONE);
        languageLabel.setText("Language");

        Button englishLanguageButton = new Button(languageComposite, SWT.RADIO);
        englishLanguageButton.setText("en");
        englishLanguageButton.setFocus();

        Button germanLanguageButton = new Button(languageComposite, SWT.RADIO);
        germanLanguageButton.setText("de");

        composite.setLayout(new GridLayout(1, false));
        composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        title = new Label(composite, SWT.NONE);
        title.setText("Login");
        Font font = new Font(composite.getDisplay(), "Helvetica", 20, SWT.BOLD);
        title.setFont(font);
        GridData titleGridData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        title.setLayoutData(titleGridData);

        Composite loginComposite = new Composite(composite, SWT.NONE);
        loginComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        loginComposite.setLayout(new GridLayout(2, false));

        usernameLabel = new Label(loginComposite, SWT.NONE);
        usernameLabel.setText("Username:");
        usernameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Text usernameText = new Text(loginComposite, SWT.SINGLE | SWT.BORDER);
        usernameText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        passwordLabel = new Label(loginComposite, SWT.NONE);
        passwordLabel.setText("Password:");
        passwordLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Text passwordText = new Text(loginComposite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
        passwordText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        passwordText.setEchoChar('*');

        showHideButton = new Button(composite, SWT.CHECK);
        showHideButton.setText("Show Password");
        showHideButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));

        // Add a SelectionListener to the show/hide button
        showHideButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Toggle the password visibility based on the button state
                boolean showPassword = showHideButton.getSelection();
                if (showPassword) {
                    passwordText.setEchoChar('\0'); // Show characters
                } else {
                    passwordText.setEchoChar('*');  // Hide characters
                }
            }
        });

        loginButton = new Button(composite, SWT.PUSH);
        loginButton.setText("Login");
        loginButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));

        englishLanguageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newLocale = new Locale("en", "US");
                Locale.setDefault(newLocale);
                languageLabel.setText(Language.getTranslatedText("lang"));
                shell.setText(Language.getTranslatedText("shellTitle"));

                updateLanguage(newLocale);
            }
        });

        germanLanguageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newLocale = new Locale("de", "DE");
                Locale.setDefault(newLocale);
                languageLabel.setText(Language.getTranslatedText("lang"));
                shell.setText(Language.getTranslatedText("shellTitle"));

                updateLanguage(newLocale);
            }
        });

        if (!currentLocale.getLanguage().equals("de")) {
            title.setText(Language.getTranslatedText("login"));
            usernameLabel.setText(Language.getTranslatedText("username"));
            passwordLabel.setText(Language.getTranslatedText("password"));
            loginButton.setText(Language.getTranslatedText("login"));
        }

        loginButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String username = usernameText.getText();
                String password = passwordText.getText();
                boolean userFound = false;

                for (Users user : users) {
                    if (user.getUserName().equals(username)) {
                        userFound = true;
                    }
                    if (username.equals(user.getUserName()) && password.equals(user.getPassword())) {
                        currentUser = user;
                        logger.info("{} logged in successfully!", username);
                        composite.dispose();
                        HomeComponent homeComponent = new HomeComponent(shell, logger);
                        try {
                            try {
                                homeComponent.createHomeComponent(currentLocale);
                            } catch (IOException | SAXException ex) {
                                throw new RuntimeException(ex);
                            }
                        } catch (ParserConfigurationException ex) {
                            throw new RuntimeException(ex);
                        }
                        shell.layout();
                    } else if (username.equals(user.getUserName()) && !password.equals(user.getPassword())) {
                        logger.error("Unable to login because of incorrect username or password.");
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                        messageBox.setText(Language.getTranslatedText("error"));
                        messageBox.setMessage(Language.getTranslatedText("errorMsg"));
                        messageBox.open();
                    }
                }
                if (!userFound) {
                    logger.error("User with the name " + username + " does not exist!");
                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                    messageBox.setText(Language.getTranslatedText("error"));
                    messageBox.setMessage(Language.getTranslatedText("noUserMsg"));
                    messageBox.open();
                }
            }
        });
    }

    public void updateLanguage(Locale newLocale) {
        Locale.setDefault(newLocale);
        title.setText(Language.getTranslatedText("login"));
        usernameLabel.setText(Language.getTranslatedText("username"));
        passwordLabel.setText(Language.getTranslatedText("password"));
        showHideButton.setText(Language.getTranslatedText("showHide"));
        loginButton.setText(Language.getTranslatedText("login"));
        adjustLabelWidth(title);
    }

    private void adjustLabelWidth(Label label) {
        GC gc = new GC(label);
        int textWidth = gc.textExtent(label.getText()).x;
        gc.dispose();

        GridData layoutData = (GridData) label.getLayoutData();
        layoutData.widthHint = textWidth;
        label.setLayoutData(layoutData);

        shell.layout();
    }
}