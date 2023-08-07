package org.swt.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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
    private final Composite bookReviewComposite;
    private String bookAuthor;
    private Element book;
    public BookReviewComponent(Composite bookReviewComposite){
        this.bookReviewComposite  = new Composite(bookReviewComposite, SWT.NONE);
        this.bookReviewComposite.setLayout(new GridLayout(1, false));
        this.bookReviewComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.bookReviewComposite.setBackground(new Color(255,255,255));
    }

    public Composite createBookReviewComponent(String bookTitle, NodeList bookNodes) throws ParserConfigurationException, IOException, SAXException {
        Label bookTitleLabel = new Label(this.bookReviewComposite, SWT.NONE);
        bookTitleLabel.setText(bookTitle);
        bookTitleLabel.setFont(new Font(this.bookReviewComposite.getDisplay(), "Helvetica", 16, SWT.BOLD));
        bookTitleLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        bookTitleLabel.setBackground(new Color(255, 255, 255));

        Label bookAuthorLabel = new Label(this.bookReviewComposite, SWT.NONE);

        File xmlFile = new File("./src/main/java/org/swt/data/Customers.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        NodeList customerNodes = doc.getElementsByTagName("customer");

        Composite reviewsComposite = new Composite(this.bookReviewComposite, SWT.NONE);
        reviewsComposite.setLayout(new GridLayout(1, true));
        reviewsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        reviewsComposite.setBackground(new Color(255,255,255));

        for(int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);
            String bookAuthorName = bookElement.getElementsByTagName("author").item(0).getTextContent();
            String bookTitleName = bookElement.getElementsByTagName("title").item(0).getTextContent();
            if(bookTitle.equals(bookTitleName)){
                this.bookAuthor = bookAuthorName;
            }

        }

        for (int i = 0; i < customerNodes.getLength(); i++) {
            Element bookReviewElement = (Element) customerNodes.item(i);
            book = (Element) bookReviewElement.getElementsByTagName("book").item(0);
            String bookName = book.getElementsByTagName("title").item(0).getTextContent();

            String customerName = bookReviewElement.getElementsByTagName("customerName").item(0).getTextContent();
            String customerRating = bookReviewElement.getElementsByTagName("rating").item(0).getTextContent();
            String customerReview = bookReviewElement.getElementsByTagName("review").item(0).getTextContent();

            if (bookName.equals(bookTitle)) {
                Composite reviewComposite = new Composite(reviewsComposite, SWT.BORDER);
                reviewComposite.setLayout(new GridLayout(1, false));
                reviewComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

                Label ratingLabel = new Label(reviewComposite, SWT.BOLD);
                ratingLabel.setText(customerRating);
                ratingLabel.setFont(new Font(this.bookReviewComposite.getDisplay(), "Helvetica", 14, SWT.BOLD));
                ratingLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

                Label customerNameLabel = new Label(reviewComposite, SWT.NONE);
                customerNameLabel.setText("by " + customerName);
                customerNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

                Label reviewLabel = new Label(reviewComposite, SWT.NONE);
                reviewLabel.setText(customerReview);
                reviewLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
            }
        }

        bookAuthorLabel.setText("by "+bookAuthor);
        bookAuthorLabel.setBackground(new Color(255,255, 255));

        return bookReviewComposite;
    }
}
