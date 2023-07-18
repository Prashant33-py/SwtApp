package org.swt.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeComponent {
    public Composite bodyComposite;
    public TreeComponent(Composite composite){
        this.bodyComposite = composite;
    }

    public void createTreeComponent() throws ParserConfigurationException, IOException, SAXException {
        Tree tree = new Tree(bodyComposite, SWT.BORDER);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        File xmlFile = new File("./src/main/java/org/swt/data/Books.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        NodeList bookNodes = doc.getElementsByTagName("book");

        Map<String, TreeItem> categoryMap = new HashMap<>();
        Map<String, TreeItem> authorMap = new HashMap<>();
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
            }


            if(authorMap.containsKey(bookAuthor)) {
                authorItem = authorMap.get(bookAuthor);
            }else{
                authorItem = new TreeItem(categoryItem, SWT.NONE);
                authorItem.setText(bookAuthor);
                authorMap.put(bookAuthor, authorItem);
            }

            TreeItem titleItem = new TreeItem(authorItem, SWT.NONE);
            titleItem.setText(bookTitle);
        }
    }

}
