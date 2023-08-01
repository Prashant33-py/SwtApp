package org.swt.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BodyComponent {
    public Composite bodyComposite;
    public Table table;
    public Composite tableComposite;
    public BodyComponent(Composite homeComposite) {
        this.bodyComposite = new Composite(homeComposite, SWT.NONE);
        bodyComposite.setLayout(new GridLayout(1,false));
        GridData bodyCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
        bodyComposite.setLayoutData(bodyCompositeData);
        bodyComposite.setBackground(new Color(255,255,255));
    }

    public Composite createBodyComponent(List<TreeItem> children, String parentCategory, String criteria, String currentCategory){
        tableComposite = new Composite(bodyComposite, SWT.NONE);
        tableComposite.setLayout(new GridLayout(1,false));
        tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tableComposite.setBackground(new Color(255,255,255));

        Label titleLabel = new Label(tableComposite, SWT.NONE);
        titleLabel.setText(criteria);
        titleLabel.setFont(new Font(bodyComposite.getDisplay(), "Helvetica", 16, SWT.BOLD));
        titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        titleLabel.setBackground(new Color(255,255,255));

        table = new Table(tableComposite, SWT.NONE | SWT.BORDER | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn columnNo = new TableColumn(table, SWT.BORDER);
        columnNo.setText("S. No.");
        columnNo.setWidth(50);

        TableColumn columnName = new TableColumn(table, SWT.BORDER);
        columnName.setText(capitalize(parentCategory));
        columnName.setWidth(250);

        if(currentCategory.equals("category")){
            TableColumn authorName = new TableColumn(table, SWT.BORDER);
            authorName.setText("Title");
            authorName.setWidth(400);
        }

        for(int i = 0; i < children.size(); i++) {
            TableItem tableItem = new TableItem(table, SWT.BORDER);
            tableItem.setText(0, String.format("%d", i+1));
            tableItem.setText(1, children.get(i).getText());
            if(currentCategory.equals("category")){
                columnName.setWidth(150);
                Map<String, String> authorBookMap = new HashMap<>();
                String books = "";
                for(TreeItem book: children.get(i).getItems()){
                    String key = book.getParentItem().getText();
                    String value = book.getText();
                    if(authorBookMap.containsKey(key)){
                        books = books + ", " + value;
                    }else{
                        books = value;
                    }
                    authorBookMap.put(key, books);
                    tableItem.setText(2, authorBookMap.get(key));
                }
            }
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                updateTable(e, children, titleLabel);
            }
        });

        bodyComposite.layout();
        return bodyComposite;
    }

    public void updateTable(MouseEvent e, List<TreeItem> children, Label columnName){
        String selectedItemText;
        Point point = new Point(e.x, e.y);
        TableItem item = table.getItem(point);
        int columnIndex = -1;
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (item.getBounds(i).contains(point)) {
                columnIndex = i;
                break;
            }
        }

        if (columnIndex != -1) {
            selectedItemText = item.getText(columnIndex);
            for(TreeItem treeItem : children) {
                if(treeItem.getText().equals(selectedItemText)){
//                    createBodyComponent(Arrays.asList(treeItem.getItem(0)), (String) treeItem.getItem(0).getParentItem().getData("tag"), treeItem.getItem(0).getParentItem().getText(), (String) treeItem.getItem(0).getData("tag"));
                }
            }
        }
    }

    public String capitalize(String title){
        return title.toUpperCase().charAt(0) + title.substring(1);
    }
}