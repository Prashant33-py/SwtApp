package org.swt.components;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.swt.util.Language;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class HomeComponent {
    private final Shell shell;
    private final Composite composite;
    private FormComponent formComponent;
    private final Logger logger;

    public HomeComponent(Shell shell, Logger logger) {
        this.composite = new Composite(shell, SWT.NONE);
        this.shell = shell;
        this.logger = logger;
    }

    public void createHomeComponent(Locale currentLocale) throws ParserConfigurationException, IOException, SAXException {
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

        NodeList bookNodes = createNodeList();
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
        addItem.setText("&Add");

        // Create the "Cancel" MenuItem
        MenuItem cancelItem = new MenuItem(fileMenu, SWT.PUSH);
        cancelItem.setText("&Cancel");

        Composite homeComposite = new Composite(shell, SWT.NONE);
        homeComposite.setLayout(new GridLayout(1, false));
        homeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        /*
          Creation of Tree Component
         */
        TreeComponent treeComponent = new TreeComponent(homeComposite, logger);
        treeComponent.createTreeComponent(bookNodes);

        if(currentLocale.getLanguage().equals("de")){
            shell.setText(Language.getTranslatedText("homeShellTitle"));
        }

        addItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                formComponent = new FormComponent(shell.getDisplay(), logger);
                formComponent.createAddBookForm();
                try {
                    NodeList newBookNodes = createNodeList();
                    TreeComponent newTreeComponent = new TreeComponent(homeComposite, logger);
                    newTreeComponent.createTreeComponent(newBookNodes);

                } catch (ParserConfigurationException | IOException | SAXException ex) {
                    throw new RuntimeException(ex);
                }
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

    public NodeList createNodeList() throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File("./src/main/java/org/swt/data/Books.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        return doc.getElementsByTagName("book");
    }
}