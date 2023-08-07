package org.swt.components;

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

public class HomeComponent {
    private final Shell shell;
    private final Composite composite;
    private FormComponent formComponent;

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

        /*
          Creation of menu bar
         */
        Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);

        // Create the "File" menu
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);

        // Create the "Add" MenuItem
        MenuItem addItem = new MenuItem(fileMenu, SWT.PUSH);
        addItem.setText("&Add");

        // Create the "Edit" MenuItem
        MenuItem editItem = new MenuItem(fileMenu, SWT.PUSH);
        editItem.setText("&Edit");

        // Create the "Cancel" MenuItem
        MenuItem cancelItem = new MenuItem(fileMenu, SWT.PUSH);
        cancelItem.setText("&Cancel");

        Composite homeComposite = new Composite(shell, SWT.NONE);
        homeComposite.setLayout(new GridLayout(2, false));
        homeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        /*
          Creation of Tree Component
         */
        TreeComponent treeComponent = new TreeComponent(homeComposite);
        treeComponent.createTreeComponent();

        addItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                formComponent = new FormComponent(shell.getDisplay());
                formComponent.createAddBookForm();
                try {
                    treeComponent.createTreeComponent();
                } catch (ParserConfigurationException | IOException | SAXException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        editItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("Edit option selected");
            }
        });

        cancelItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("Cancel option selected");
                fileMenu.dispose();
            }
        });

        homeComposite.layout();
    }
}