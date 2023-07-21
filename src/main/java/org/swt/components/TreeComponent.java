package org.swt.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
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
import java.util.HashMap;
import java.util.Map;

public class TreeComponent {
    public Composite bodyComposite;
    public TreeComponent(Composite composite){
        this.bodyComposite = composite;
    }
    Map<String, TreeItem> categoryMap = new HashMap<>();
    Map<String, TreeItem> authorMap = new HashMap<>();

    public void createTreeComponent() throws ParserConfigurationException, IOException, SAXException {
        Tree tree = new Tree(bodyComposite, SWT.NONE);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        File xmlFile = new File("./src/main/java/org/swt/data/Books.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        NodeList bookNodes = doc.getElementsByTagName("book");

        TreeItem categoryItem;
        TreeItem authorItem;

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
            }

            if(authorMap.containsKey(bookAuthor)) {
                authorItem = authorMap.get(bookAuthor);
            }else{
                authorItem = new TreeItem(categoryItem, SWT.NONE);
                authorItem.setText(bookAuthor);
                authorMap.put(bookAuthor, authorItem);
                authorItem.setData(bookElement);
            }

            TreeItem titleItem = new TreeItem(authorItem, SWT.NONE);
            titleItem.setText(bookTitle);
            titleItem.setData(bookElement);
        }
        tree.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem selectedItem = (TreeItem) e.item;
                if (selectedItem != null) {
                    Object data = selectedItem.getData();
                    if (data instanceof Element) {
                        Element element = (Element) data;
                        System.out.println(element.getTagName());
                        String tagName = element.getTagName();
                        if ("category".equals(tagName)) {
                            handleCategoryItemClick(selectedItem);
                        } else if ("author".equals(tagName)) {
                            handleAuthorItemClick(selectedItem);
                        } else if ("title".equals(tagName)) {
                            handleTitleItemClick(selectedItem);
                        }
                    }
                }
            }
        });
    }

    public void handleAuthorItemClick(TreeItem selectedItem){
        System.out.println(selectedItem);
        System.out.println("Author clicked");
    }
    public void handleCategoryItemClick(TreeItem selectedItem){
        System.out.println(selectedItem);
        System.out.println("Category clicked");
    }
    public void handleTitleItemClick(TreeItem selectedItem){
        System.out.println(selectedItem);
        System.out.println("title clicked");
    }
}
