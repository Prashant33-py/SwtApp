package org.swt.components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class BookReviewComponent {
    public BookReviewComponent(){
    }

    public void createBookReviewComponent() throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File("./src/main/java/org/swt/data/Customers.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        NodeList bookNodes = doc.getElementsByTagName("customer");

        for(int i = 0; i<bookNodes.getLength(); i++){
            Element bookReviewElement = (Element) bookNodes.item(i);
            System.out.println(bookReviewElement);
        }
    }


}
