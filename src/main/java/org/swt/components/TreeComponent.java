package org.swt.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeComponent {
    private final Composite treeComposite;
    private BodyComponent bodyComponent;
    private final Map<String, TreeItem> categoryMap = new HashMap<>();
    private final Map<String, TreeItem> authorMap = new HashMap<>();
    private final Map<String, TreeItem> openedTabs = new HashMap<>();
    private CTabFolder tabFolder;
    private TreeItem categoryItem;
    private final Composite homeComposite;
    private TreeItem authorItem;
    private TreeItem titleItem;
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

        NodeList bookNodes = doc.getElementsByTagName("book");

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

        tabFolder = new CTabFolder(homeComposite, SWT.NONE);
        tabFolder.setLayout(new GridLayout(1, false));
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tabFolder.setBackground(new Color(255,255,255));

        CTabItem defaultTabItem = new CTabItem(tabFolder, SWT.CLOSE);
        defaultTabItem.setText("Welcome!");
        tabFolder.setSelection(defaultTabItem);

        Composite defaultComposite = new Composite(tabFolder, SWT.NONE);
        defaultComposite.setLayout(new GridLayout(1, false));
        defaultComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        defaultComposite.setBackground(new Color(255,255,255));

        Label defaultWelcomeLabel = new Label(defaultComposite, SWT.CLOSE);
        defaultWelcomeLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        Font font = new Font(defaultComposite.getDisplay(), "Helvetica", 20, SWT.BOLD);
        defaultWelcomeLabel.setFont(font);
        defaultWelcomeLabel.setText("Welcome!");
        defaultWelcomeLabel.setBackground(new Color(255,255,255));

        Label defaultDescLabel = new Label(defaultComposite, SWT.CLOSE);
        defaultDescLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        defaultDescLabel.setText("Click on any category to see more books in that category.");
        defaultDescLabel.setBackground(new Color(255,255,255));

        defaultTabItem.setControl(defaultComposite);

        tree.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem selectedItem = (TreeItem) e.item;
                String selectedItemText = selectedItem.getText();
                CTabItem tabItem = new CTabItem(tabFolder, SWT.CLOSE);

                tabItem.setText(selectedItemText);
                bodyComponent = new BodyComponent(tabFolder);

                defaultTabItem.dispose();

                if (selectedItem != null) {
                    List<TreeItem> children = new ArrayList<>();
                    if (selectedItem.getItemCount() > 0) {
                        for (int i = 0; i < selectedItem.getItemCount(); i++) {
                            children.add(selectedItem.getItem(i));
                        }
                    }
                    String parentNode = (String) selectedItem.getData("tag");
                    openedTabs.put(selectedItem.getText(),selectedItem);
                    if ("category".equals(parentNode)) {
                        try {
                            handleCategoryItemClick(children, selectedItem.getText(),tabItem);
                        } catch (ParserConfigurationException | IOException | SAXException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else if ("author".equals(parentNode)) {
                        try {
                            handleAuthorItemClick(children, selectedItem.getText(),tabItem);
                        } catch (ParserConfigurationException | IOException | SAXException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else if ("title".equals(parentNode)) {
                        try {
                            handleTitleItemClick(children, selectedItem.getText(),tabItem);
                        } catch (ParserConfigurationException | IOException | SAXException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
    }

    public void handleCategoryItemClick(List<TreeItem> children, String criteria, CTabItem tabItem) throws ParserConfigurationException, IOException, SAXException {
        tabItem.setControl(this.bodyComponent.createBodyComponent(children, (String) authorItem.getData("tag"), criteria, (String) categoryItem.getData("tag")));
        tabFolder.setSelection(tabItem);
    }

    public void handleAuthorItemClick(List<TreeItem> children, String criteria, CTabItem tabItem) throws ParserConfigurationException, IOException, SAXException {
        tabItem.setControl(this.bodyComponent.createBodyComponent(children, (String) titleItem.getData("tag"), criteria, (String) authorItem.getData("tag")));
        tabFolder.setSelection(tabItem);
    }

    public void handleTitleItemClick(List<TreeItem> children, String criteria, CTabItem tabItem) throws ParserConfigurationException, IOException, SAXException {
        tabItem.setControl(this.bodyComponent.createBodyComponent(children, "Title", criteria, (String) titleItem.getData("tag")));
        tabFolder.setSelection(tabItem);
    }
}