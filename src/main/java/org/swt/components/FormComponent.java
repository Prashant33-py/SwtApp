package org.swt.components;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.util.List;

public class FormComponent {
    public Shell newShell;
    public Display newDisplay;
    private final Logger logger;
    private Text addCategoryNameText;
    private Text addAuthorNameText;
    private Text addBookNameText;
    private Label messageLabel;
    GridData textGridData = new GridData(SWT.CENTER, SWT.CENTER, true,false);
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

        messageLabel = new Label(formBodyComposite, SWT.NONE);
        messageLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Composite mainFormBodyComposite = new Composite(formBodyComposite, SWT.NONE);
        mainFormBodyComposite.setLayout(new GridLayout(2, false));
        mainFormBodyComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Label addCategoryName = new Label(mainFormBodyComposite, SWT.NONE);
        addCategoryName.setText("Enter the category:");
        addCategoryName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        textGridData.widthHint = 120;

        addCategoryNameText = new Text(mainFormBodyComposite, SWT.BORDER);
        addCategoryNameText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,false));
        addCategoryNameText.setData("type", "category");

        Label addAuthorName = new Label(mainFormBodyComposite, SWT.NONE);
        addAuthorName.setText("Enter the author's name:");
        addAuthorName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        addAuthorNameText = new Text(mainFormBodyComposite, SWT.BORDER);
        addAuthorNameText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,false));
        addAuthorNameText.setData("type","author");

        Label addBookName = new Label(mainFormBodyComposite, SWT.NONE);
        addBookName.setText("Enter the book name:");
        addBookName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        addBookNameText = new Text(mainFormBodyComposite, SWT.BORDER);
        addBookNameText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        addBookNameText.setData("type","title");

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

                if(checkForInvalidInputs(formBodyComposite, bookCategory, bookAuthor, bookTitle)){
                    try {
                        boolean isBookSaved = saveBookToXMLFile(bookCategory, bookAuthor, bookTitle);
                        if(isBookSaved){
                            messageLabel.setText(bookTitle+" saved successfully!");
                            messageLabel.setForeground(new Color(0, 255, 0));
                            formBodyComposite.layout(true);
                        }
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

        KeyListener saveKeyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.stateMask & SWT.CTRL) != 0 && e.keyCode == 's') {
                    Control[] children = mainFormBodyComposite.getChildren();
                    String bookTitle = "";
                    String bookAuthor = "";
                    String bookCategory = "";
                    for (Control control : children) {
                        if (control instanceof Text) {
                            Text textField = (Text) control;
                            String text = textField.getText();

                            if(bookCategory.isEmpty() && textField.getData("type").equals("category")){
                                bookCategory = text;
                                continue;
                            }
                            if(bookAuthor.isEmpty() && textField.getData("type").equals("author")){
                                bookAuthor = text;
                                continue;
                            }
                            if(bookTitle.isEmpty() && textField.getData("type").equals("title")){
                                bookTitle = text;
                            }
                        }
                    }

                    if(checkForInvalidInputs(formBodyComposite, bookCategory, bookAuthor, bookTitle)){
                        try {
                            boolean isBookSaved = saveBookToXMLFile(bookCategory, bookAuthor, bookTitle);
                            if(isBookSaved){
                                messageLabel.setText(bookTitle+" saved successfully!");
                                messageLabel.setForeground(new Color(0, 255, 0));
                                formBodyComposite.layout(true);
                            }
                        } catch (ParserConfigurationException | SAXException | TransformerException | IOException err) {
                            logger.error(err);
                        }
                    }
                }
                if ((e.stateMask & SWT.CTRL) != 0 && e.keyCode == 'z') {
                    addCategoryNameText.setText("");
                    addAuthorNameText.setText("");
                    addBookNameText.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        addCategoryNameText.addKeyListener(saveKeyListener);
        addAuthorNameText.addKeyListener(saveKeyListener);
        addBookNameText.addKeyListener(saveKeyListener);

        newShell.open();
        newShell.layout(true);

        while (!newShell.isDisposed()) {
            if (!newDisplay.readAndDispatch()) {
                newDisplay.sleep();
            }
        }
    }

    public boolean saveBookToXMLFile(String bookCategory, String bookAuthor, String bookTitle) throws ParserConfigurationException, IOException, SAXException, TransformerException {
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
        return true;
    }

    public void createEditBookForm(TableItem rightClickedTableItem, List<TreeItem> parentChildren){
        String existingBookCategory = "";
        String existingBookAuthor = "";

        for(TreeItem treeItem: parentChildren){
            existingBookAuthor = treeItem.getParentItem().getText();
            if(rightClickedTableItem.getText(1).equals(treeItem.getText())){
                existingBookCategory = treeItem.getParentItem().getParentItem().getText();
                break;
            }
        }

        newShell = new Shell(newDisplay);
        newShell.setText("Edit Book");
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
        addBookLabel.setText("Edit the details you want to change.");

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

        textGridData.widthHint = 120;

        addCategoryNameText = new Text(mainFormBodyComposite, SWT.BORDER);
        addCategoryNameText.setLayoutData(textGridData);
        addCategoryNameText.setText(existingBookCategory);
        addCategoryNameText.setEnabled(false);

        Label addAuthorName = new Label(mainFormBodyComposite, SWT.NONE);
        addAuthorName.setText("Enter the author's name:");
        addAuthorName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        addAuthorNameText = new Text(mainFormBodyComposite, SWT.BORDER);
        addAuthorNameText.setLayoutData(textGridData);
        addAuthorNameText.setText(existingBookAuthor);

        Label addBookName = new Label(mainFormBodyComposite, SWT.NONE);
        addBookName.setText("Enter the book name:");
        addBookName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        addBookNameText = new Text(mainFormBodyComposite, SWT.BORDER);
        addBookNameText.setLayoutData(textGridData);
        addBookNameText.setText(rightClickedTableItem.getText(1));

        Composite buttonsComposite = new Composite(formBodyComposite, SWT.NONE);
        buttonsComposite.setLayout(new GridLayout(2,true));
        GridData buttonsData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        buttonsData.horizontalSpan = 2;
        buttonsComposite.setLayoutData(buttonsData);

        Button cancelButton = new Button(buttonsComposite, SWT.NONE);
        cancelButton.setText("Cancel");

        Button updateButton = new Button(buttonsComposite, SWT.NONE);
        updateButton.setText("Update");

        updateButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String bookTitle = addBookNameText.getText();
                String bookAuthor = addAuthorNameText.getText();
                String bookCategory = addCategoryNameText.getText();
                if(checkForInvalidInputs(formBodyComposite, bookCategory, bookAuthor, bookTitle)){
                    try {
                        boolean isBookUpdated = updateBookDataInXMLFile(bookCategory, bookAuthor, bookTitle);
                        if(isBookUpdated){
                            messageLabel.setText(bookTitle+" updated successfully!");
                            messageLabel.setForeground(new Color(0, 255, 0));
                            formBodyComposite.layout(true);
                        }
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

        KeyListener updateKeyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                if ((event.stateMask & SWT.CTRL) != 0 && event.keyCode == 's') {
                    Control[] children = mainFormBodyComposite.getChildren();
                    String bookTitle = "";
                    String bookAuthor = "";
                    String bookCategory = "";
                    for (Control control : children) {
                        if (control instanceof Text) {
                            Text textField = (Text) control;
                            String text = textField.getText();

                            if(bookCategory.isEmpty()){
                                bookCategory = text;
                                continue;
                            }
                            if(bookAuthor.isEmpty()){
                                bookAuthor = text;
                                continue;
                            }

                            if(bookTitle.isEmpty()){
                                bookTitle = text;
                            }
                        }
                    }
                    if(checkForInvalidInputs(formBodyComposite, bookCategory, bookAuthor, bookTitle)){
                        try {
                            boolean isBookUpdated = updateBookDataInXMLFile(bookCategory, bookAuthor, bookTitle);
                            if(isBookUpdated){
                                messageLabel.setText(bookTitle+" updated successfully!");
                                messageLabel.setForeground(new Color(0, 255, 0));
                                formBodyComposite.layout(true);
                            }
                        } catch (ParserConfigurationException | SAXException | TransformerException | IOException err) {
                            logger.error(err);
                        }
                    }
                    event.doit = false;
                }

                if ((event.stateMask & SWT.CTRL) != 0 && event.keyCode == 'z') {
                    addCategoryNameText.setText("");
                    addAuthorNameText.setText("");
                    addBookNameText.setText("");
                }

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        };

        addCategoryNameText.addKeyListener(updateKeyListener);
        addAuthorNameText.addKeyListener(updateKeyListener);
        addBookNameText.addKeyListener(updateKeyListener);

        newShell.open();
        newShell.layout(true);

        while (!newShell.isDisposed()) {
            if (!newDisplay.readAndDispatch()) {
                newDisplay.sleep();
            }
        }
    }

    public boolean updateBookDataInXMLFile(String bookCategory, String bookAuthor, String bookTitle) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        File xmlFile = new File("./src/main/java/org/swt/data/Books.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        NodeList bookList = doc.getElementsByTagName("book");

        for (int i = 0; i < bookList.getLength(); i++) {
            Element bookElement = (Element) bookList.item(i);
            String existingTitle = bookElement.getElementsByTagName("title").item(0).getTextContent();

            if (existingTitle.equals(bookTitle)) {
                bookElement.getElementsByTagName("category").item(0).setTextContent(bookCategory);
                bookElement.getElementsByTagName("author").item(0).setTextContent(bookAuthor);
                bookElement.getElementsByTagName("title").item(0).setTextContent(bookTitle);
                break;
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(source, result);

        logger.info("{} has been updated in the library successfully!", bookTitle);

        return true;
    }

    public void disableWarningMessage(){
        newDisplay.timerExec(2000, () -> messageLabel.setText(""));
    }

    public boolean checkForInvalidInputs(Composite formBodyComposite, String bookCategory, String bookAuthor, String bookTitle){
        boolean isInvalidInput = false;
        if(bookCategory.isEmpty()){
            messageLabel.setText("The category of the book cannot be empty!");
            messageLabel.setForeground(new Color(255, 0, 0));
            isInvalidInput = true;
            formBodyComposite.layout(true);
            disableWarningMessage();
        }else if(bookAuthor.isEmpty()){
            messageLabel.setText("The author of the book cannot be empty!");
            messageLabel.setForeground(new Color(255, 0, 0));
            isInvalidInput = true;
            formBodyComposite.layout(true);
            disableWarningMessage();
        }else if(bookTitle.isEmpty()){
            messageLabel.setText("The title of the book cannot be empty!");
            messageLabel.setForeground(new Color(255, 0, 0));
            isInvalidInput = true;
            formBodyComposite.layout(true);
            disableWarningMessage();
        }
        return !isInvalidInput;
    }
}