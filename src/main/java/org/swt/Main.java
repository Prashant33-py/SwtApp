package org.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.swt.components.LoginComponent;
import org.swt.util.Language;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Login - SWT App");
        shell.setSize(500, 320);
        shell.setLayout(new GridLayout(1,false));

        Composite languageComposite = new Composite(shell, SWT.END);
        languageComposite.setLayout(new GridLayout(3, false));
        languageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,false));

        Label languageLabel = new Label(languageComposite, SWT.NONE);
        languageLabel.setText("Language");

        Button englishLanguageButton = new Button(languageComposite, SWT.RADIO);
        englishLanguageButton.setText("en");

        Button germanLanguageButton = new Button(languageComposite, SWT.RADIO);
        germanLanguageButton.setText("de");

        LoginComponent loginComponent = new LoginComponent(shell);
        loginComponent.createLoginComponent(Locale.getDefault());

        englishLanguageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Locale newLocale = new Locale("en", "US");
                Locale.setDefault(newLocale);
                languageLabel.setText(Language.getTranslatedText("lang"));
                shell.setText(Language.getTranslatedText("shellTitle"));

                loginComponent.updateLanguage(newLocale);
            }
        });

        germanLanguageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Locale newLocale = new Locale("de", "DE");
                Locale.setDefault(newLocale);
                languageLabel.setText(Language.getTranslatedText("lang"));
                shell.setText(Language.getTranslatedText("shellTitle"));

                loginComponent.updateLanguage(newLocale);
            }
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}