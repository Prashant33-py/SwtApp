package org.swt.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class HomeComponent {
    private final Shell shell;
    private final Composite composite;

    public HomeComponent(Shell shell) {
        this.composite = new Composite(shell, SWT.NONE);
        this.shell = shell;
    }

    public void createHomeComponent() throws ParserConfigurationException, IOException, SAXException {
        GridData compositeData = new GridData(SWT.CENTER, SWT.FILL, true, false);
        composite.setLayout(new GridLayout(1, false));
        composite.setLayoutData(compositeData);
        shell.setText("Home - SWT App");
        shell.setMaximized(true);

        Label welcomeLabel = new Label(composite, SWT.CENTER);
        welcomeLabel.setText("Welcome to Home Page");
        Font font = new Font(composite.getDisplay(), "Helvetica", 20, SWT.BOLD);
        welcomeLabel.setFont(font);
        welcomeLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Composite homeComposite = new Composite(shell, SWT.NONE);
        homeComposite.setLayout(new GridLayout(2, false));
        homeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        TreeComponent treeComponent = new TreeComponent(homeComposite);
        treeComponent.createTreeComponent();

        homeComposite.layout();
    }
}