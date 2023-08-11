package org.swt.components;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class FormComponent {
    public Shell newShell;
    public Display newDisplay;
    private final Logger logger;
    private Text addCategoryNameText;
    private Text addAuthorNameText;
    private Text addBookNameText;
    public FormComponent(Display display, Logger logger){
        newDisplay = display;
        this.logger = logger;
    }
    public void createAddBookForm(){
        newShell = new Shell(newDisplay);
        newShell.setText("Add Book");
        newShell.setLayout(new GridLayout(1, false));
        newShell.setSize(550,350);
        newShell.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

        Composite formComposite = new Composite(newShell, SWT.CENTER);
        formComposite.setLayout(new GridLayout(1,false));
        GridData formGridData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        formGridData.widthHint = 450;
        formGridData.heightHint = 250;
        formComposite.setLayoutData(formGridData);

        Label addBookLabel = new Label(formComposite, SWT.NONE);
        addBookLabel.setText("Enter the details to add a new book to the library");

        Composite formBodyComposite = new Composite(formComposite, SWT.BORDER);
        formBodyComposite.setLayout(new GridLayout(1, false));
        formBodyComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Label messageLabel = new Label(formBodyComposite, SWT.NONE);
        messageLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Composite mainFormBodyComposite = new Composite(formBodyComposite, SWT.NONE);
        mainFormBodyComposite.setLayout(new GridLayout(2, false));
        mainFormBodyComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Label addCategoryName = new Label(mainFormBodyComposite, SWT.NONE);
        addCategoryName.setText("Enter the category:");
        addCategoryName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        addCategoryNameText = new Text(mainFormBodyComposite, SWT.BORDER);
        addCategoryNameText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,false));

        Label addAuthorName = new Label(mainFormBodyComposite, SWT.NONE);
        addAuthorName.setText("Enter the author's name:");
        addAuthorName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        addAuthorNameText = new Text(mainFormBodyComposite, SWT.BORDER);
        addAuthorNameText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,false));

        Label addBookName = new Label(mainFormBodyComposite, SWT.NONE);
        addBookName.setText("Enter the book name:");
        addBookName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        addBookNameText = new Text(mainFormBodyComposite, SWT.BORDER);
        addBookNameText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Composite buttonsComposite = new Composite(formBodyComposite, SWT.NONE);
        buttonsComposite.setLayout(new GridLayout(2,true));
        GridData buttonsData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        buttonsData.horizontalSpan = 2;
        buttonsComposite.setLayoutData(buttonsData);

        Button cancelButton = new Button(buttonsComposite, SWT.NONE);
        cancelButton.setText("Cancel");

        Button saveButton = new Button(buttonsComposite, SWT.NONE);
        saveButton.setText("Save");

        saveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String bookTitle = addBookNameText.getText();
                String bookAuthor = addAuthorNameText.getText();
                String bookCategory = addCategoryNameText.getText();

                if(bookTitle.isEmpty()){
                    messageLabel.setText("The title of the book cannot be empty!");
                    messageLabel.setForeground(new Color(255, 0, 0));
                }else if(bookAuthor.isEmpty()){
                    messageLabel.setText("The author of the book cannot be empty!");
                    messageLabel.setForeground(new Color(255, 0, 0));
                }else if(bookCategory.isEmpty()){
                    messageLabel.setText("The category of the book cannot be empty!");
                    messageLabel.setForeground(new Color(255, 0, 0));
                }else {
                    try {
                        saveBookToXMLFile(bookTitle, bookAuthor, bookCategory);
                    } catch (ParserConfigurationException | SAXException | TransformerException | IOException err) {
                        logger.error(err);
                    }
                }
            }
        });


        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newShell.close();
            }
        });

        newShell.open();
        newShell.layout(true);

        while (!newShell.isDisposed()) {
            if (!newDisplay.readAndDispatch()) {
                newDisplay.sleep();
            }
        }
    }

    public void saveBookToXMLFile(String bookTitle, String bookAuthor, String bookCategory) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        File xmlFile = new File("./src/main/java/org/swt/data/Books.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        Element newBook = doc.createElement("book");

        Element bookCategoryTag = doc.createElement("category");
        bookCategoryTag.appendChild(doc.createTextNode(bookCategory));

        Element bookAuthorTag = doc.createElement("author");
        bookAuthorTag.appendChild(doc.createTextNode(bookAuthor));

        Element bookTitleTag = doc.createElement("title");
        bookTitleTag.appendChild(doc.createTextNode(bookTitle));

        newBook.appendChild(bookCategoryTag);
        newBook.appendChild(bookAuthorTag);
        newBook.appendChild(bookTitleTag);

        Node books = doc.getElementsByTagName("library").item(0);
        books.appendChild(newBook);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(source, result);

        addCategoryNameText.setText("");
        addAuthorNameText.setText("");
        addBookNameText.setText("");

        logger.info("{} has been added to the library successfully!",bookTitle);
    }
}