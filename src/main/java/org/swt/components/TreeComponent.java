package org.swt.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class TreeComponent {
    private final Composite treeComposite;
    private BodyComponent bodyComponent;
    private final Map<String, TreeItem> categoryMap = new HashMap<>();
    private final Map<String, TreeItem> authorMap = new HashMap<>();
    private final Map<String, TreeItem> openedTabs = new HashMap<>();
    private TabFolder tabFolder;
    private TreeItem categoryItem;
    private final Composite homeComposite;
    private TreeItem authorItem;
    private TreeItem titleItem;
    private List<TreeItem> children = new ArrayList<>();
    private String selectedItemText;
    private NodeList bookNodes;

    public TreeComponent(Composite homeComposite) {
        this.treeComposite = new Composite(homeComposite, SWT.NONE);
        this.homeComposite = homeComposite;
    }

    public void createTreeComponent() throws ParserConfigurationException, IOException, SAXException {
        Tree tree = new Tree(treeComposite, SWT.NONE);
        treeComposite.setLayout(new GridLayout(1, false));
        treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

        GridData treeCompositeData = new GridData(SWT.FILL, SWT.FILL, false, true);
        treeCompositeData.widthHint = 300;
        tree.setLayoutData(treeCompositeData);

        File xmlFile = new File("./src/main/java/org/swt/data/Books.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        bookNodes = doc.getElementsByTagName("book");

        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);
            String bookCategory = bookElement.getElementsByTagName("category").item(0).getTextContent();
            String bookAuthor = bookElement.getElementsByTagName("author").item(0).getTextContent();
            String bookTitle = bookElement.getElementsByTagName("title").item(0).getTextContent();

            if (categoryMap.containsKey(bookCategory)) {
                categoryItem = categoryMap.get(bookCategory);
            } else {
                categoryItem = new TreeItem(tree, SWT.NONE);
                categoryItem.setText(bookCategory);
                categoryMap.put(bookCategory, categoryItem);
                categoryItem.setData(bookElement);
                categoryItem.setData("tag", "category");
            }

            if (authorMap.containsKey(bookAuthor)) {
                authorItem = authorMap.get(bookAuthor);
            } else {
                authorItem = new TreeItem(categoryItem, SWT.NONE);
                authorItem.setText(bookAuthor);
                authorMap.put(bookAuthor, authorItem);
                authorItem.setData(bookElement);
                authorItem.setData("tag", "author");
            }

            titleItem = new TreeItem(authorItem, SWT.NONE);
            titleItem.setText(bookTitle);
            titleItem.setData(bookElement);
            titleItem.setData("tag", "title");
        }

        tabFolder = new TabFolder(homeComposite, SWT.NONE);
        tabFolder.setLayout(new GridLayout(1, false));
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tabFolder.setBackground(new Color(255,255,255));

        //Category tab item
        TabItem categoryTabItem = new TabItem(tabFolder, SWT.CLOSE);
        categoryTabItem.setText("Category");
        tabFolder.setSelection(categoryTabItem);

        Composite categoryComposite = new Composite(tabFolder, SWT.NONE);
        categoryComposite.setLayout(new GridLayout(1, false));
        categoryComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        categoryComposite.setBackground(new Color(255,255,255));

        Label categoryLabel = new Label(categoryComposite, SWT.CLOSE);
        categoryLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        Font font = new Font(categoryComposite.getDisplay(), "Helvetica", 20, SWT.BOLD);
        categoryLabel.setFont(font);
        categoryLabel.setText("Category");
        categoryLabel.setBackground(new Color(255,255,255));

        Label categroyDescLabel = new Label(categoryComposite, SWT.CLOSE);
        categroyDescLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        categroyDescLabel.setText("Click on any category to see more books in that category.");
        categroyDescLabel.setBackground(new Color(255,255,255));

        categoryTabItem.setControl(categoryComposite);
        //end

        //Author tab item
        TabItem authorTabItem = new TabItem(tabFolder, SWT.CLOSE);
        authorTabItem.setText("Author");
        tabFolder.setSelection(categoryTabItem);

        Composite authorComposite = new Composite(tabFolder, SWT.NONE);
        authorComposite.setLayout(new GridLayout(1, false));
        authorComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        authorComposite.setBackground(new Color(255,255,255));

        Label authorLabel = new Label(authorComposite, SWT.CLOSE);
        authorLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        authorLabel.setFont(font);
        authorLabel.setText("Author");
        authorLabel.setBackground(new Color(255,255,255));

        Label authorDescLabel = new Label(authorComposite, SWT.CLOSE);
        authorDescLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        authorDescLabel.setText("Click on any author to see the books they wrote.");
        authorDescLabel.setBackground(new Color(255,255,255));

        authorTabItem.setControl(authorComposite);
        //end

        //Title tab item
        TabItem titleTabItem = new TabItem(tabFolder, SWT.CLOSE);
        titleTabItem.setText("Title");
        tabFolder.setSelection(categoryTabItem);

        Composite titleComposite = new Composite(tabFolder, SWT.NONE);
        titleComposite.setLayout(new GridLayout(1, false));
        titleComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        titleComposite.setBackground(new Color(255,255,255));

        Label titleLabel = new Label(titleComposite, SWT.CLOSE);
        titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        titleLabel.setFont(font);
        titleLabel.setText("Title");
        titleLabel.setBackground(new Color(255,255,255));

        Label titleDescLabel = new Label(titleComposite, SWT.CLOSE);
        titleDescLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        titleDescLabel.setText("Click on any title to view the book reviews.");
        titleDescLabel.setBackground(new Color(255,255,255));

        titleTabItem.setControl(titleComposite);
        //end

        tree.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem selectedItem = (TreeItem) e.item;
                selectedItemText = selectedItem.getText();

                bodyComponent = new BodyComponent(tabFolder);

                if (selectedItem.getItemCount() > 0) {
                    if(!children.isEmpty()) {
                        children.clear();
                    }
                    for (int i = 0; i < selectedItem.getItemCount(); i++) {
                        children.add(selectedItem.getItem(i));
                    }
                }

                String parentNode = (String) selectedItem.getData("tag");
                openedTabs.put(selectedItem.getText(),selectedItem);
                if ("category".equals(parentNode)) {
                    handleCategoryItemClick(children, selectedItemText, categoryTabItem);
                } else if ("author".equals(parentNode)) {
                    handleAuthorItemClick(children, selectedItemText, authorTabItem);
                } else if ("title".equals(parentNode)) {
                    try {
                        handleTitleItemClick(selectedItemText, titleTabItem);
                    } catch (ParserConfigurationException | IOException | SAXException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        tabFolder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TabItem newAuthorTabItem = (TabItem) e.item;
                if(newAuthorTabItem.getText().equals("Category")){
                    authorTabItem.setControl(authorComposite);
                    System.out.println(categoryTabItem.getControl());
                    categoryTabItem.setControl(bodyComponent.createTable(children, (String) authorItem.getData("tag"), selectedItemText, (String) categoryItem.getData("tag")));
                }
            }
        });
    }

    public void handleCategoryItemClick(List<TreeItem> children, String criteriaName, TabItem categoryTabItem){
        Composite newCategoryComposite = this.bodyComponent.createTable(children, (String) authorItem.getData("tag"), criteriaName, (String) categoryItem.getData("tag"));
        categoryTabItem.setControl(newCategoryComposite);
        tabFolder.setSelection(categoryTabItem);
    }

    public void handleAuthorItemClick(List<TreeItem> children, String criteriaName, TabItem authorTabItem){
        authorTabItem.setControl(this.bodyComponent.createTable(children, (String) titleItem.getData("tag"), criteriaName, (String) authorItem.getData("tag")));
        tabFolder.setSelection(authorTabItem);
    }

    public void handleTitleItemClick(String bookTitle, TabItem titleTabItem) throws ParserConfigurationException, IOException, SAXException {
        BookReviewComponent bookReviewComponent = new BookReviewComponent(tabFolder);
        titleTabItem.setControl(bookReviewComponent.createBookReviewComponent(bookTitle, bookNodes));
        tabFolder.setSelection(titleTabItem);
    }
}