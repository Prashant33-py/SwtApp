package org.swt.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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
import java.util.*;

public class TreeComponent {
    public Composite treeComposite;
    public BodyComponent bodyComponent;
    public TreeComponent(Composite homeComposite){
        this.treeComposite = new Composite(homeComposite, SWT.NONE);
        this.bodyComponent = new BodyComponent(homeComposite);
    }
    Map<String, TreeItem> categoryMap = new HashMap<>();
    Map<String, TreeItem> authorMap = new HashMap<>();
    TreeItem categoryItem;
    TreeItem authorItem;
    TreeItem titleItem;

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
                categoryItem.setData("tag","category");
            }

            if(authorMap.containsKey(bookAuthor)) {
                authorItem = authorMap.get(bookAuthor);
            }else{
                authorItem = new TreeItem(categoryItem, SWT.NONE);
                authorItem.setText(bookAuthor);
                authorMap.put(bookAuthor, authorItem);
                authorItem.setData(bookElement);
                authorItem.setData("tag","author");
            }

            titleItem = new TreeItem(authorItem, SWT.NONE);
            titleItem.setText(bookTitle);
            titleItem.setData(bookElement);
            titleItem.setData("tag","title");
        }
        tree.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem selectedItem = (TreeItem) e.item;
                if (selectedItem != null) {
                    bodyComponent.disposeTable();
                    List<TreeItem> children = new ArrayList<>();
                    if(selectedItem.getItemCount() > 0) {
                        for(int i = 0; i < selectedItem.getItemCount(); i++) {
                            children.add(selectedItem.getItem(i));
                        }
                    }
                    String parentNode = (String) selectedItem.getData("tag");
                    if ("category".equals(parentNode)) {
                        handleCategoryItemClick(children, selectedItem.getText());
                    } else if ("author".equals(parentNode)) {
                        handleAuthorItemClick(children, selectedItem.getText());
                    } else if ("title".equals(parentNode)) {
                        handleTitleItemClick(children, selectedItem.getText());
                    }
                }
            }
        });
    }

    public void handleAuthorItemClick(List<TreeItem> children, String criteria){
        this.bodyComponent.createBodyComponent(children, (String)titleItem.getData("tag"), criteria, (String)authorItem.getData("tag"));
    }
    public void handleCategoryItemClick(List<TreeItem> children, String criteria){
        this.bodyComponent.createBodyComponent(children, (String)authorItem.getData("tag"), criteria, (String)categoryItem.getData("tag"));
    }
    public void handleTitleItemClick(List<TreeItem> children, String criteria){
        this.bodyComponent.createBodyComponent(children, "Title", criteria, (String)titleItem.getData("tag"));
    }
}