package org.swt.components;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BodyComponent {
    public Composite bodyComposite;
    public Table table;
    public Composite tableComposite;
    public TabFolder tabFolder;
    private final Logger logger;
    public BodyComponent(Composite tabFolder, Logger logger) {
        this.tabFolder = (TabFolder) tabFolder;
        this.logger = logger;
        this.bodyComposite = new Composite(tabFolder, SWT.NONE);
        bodyComposite.setLayout(new GridLayout(1,false));
        GridData bodyCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
        bodyComposite.setLayoutData(bodyCompositeData);
        bodyComposite.setBackground(new Color(255,255,255));
    }

    public Composite createTable(List<TreeItem> children, String childCategoryTag, String criteriaName, String currentCategoryTag) {
        tableComposite = new Composite(bodyComposite, SWT.NONE);
        tableComposite.setLayout(new GridLayout(1,false));
        tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tableComposite.setBackground(new Color(255,255,255));

        Label titleLabel = new Label(tableComposite, SWT.NONE);
        titleLabel.setText(criteriaName);
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
        columnName.setText(capitalize(childCategoryTag));
        columnName.setWidth(250);

        if(currentCategoryTag.equals("category")){
            TableColumn authorName = new TableColumn(table, SWT.BORDER);
            authorName.setText("Title");
            authorName.setWidth(400);
        }

        for(int i = 0; i < children.size(); i++) {
            TableItem tableItem = new TableItem(table, SWT.BORDER);
            tableItem.setText(0, String.format("%d", i+1));
            tableItem.setText(1, children.get(i).getText());
            if(currentCategoryTag.equals("category")){
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

        Menu contextMenu = new Menu(table);
        // Create menu items
        MenuItem menuItem1 = new MenuItem(contextMenu, SWT.PUSH);
        menuItem1.setText("Option 1");
        MenuItem menuItem2 = new MenuItem(contextMenu, SWT.PUSH);
        menuItem2.setText("Option 2");

        table.setMenu(contextMenu);

        table.addMenuDetectListener(e -> {
            Point mouseLocation = table.toDisplay(e.x-358,e.y-165);
            Point selectedTableItemLocation = table.toControl(e.x,e.y);
            TableItem selectedTableItem = table.getItem(selectedTableItemLocation);

            System.out.println(selectedTableItem);
            contextMenu.setLocation(mouseLocation);
            contextMenu.setVisible(true);
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                if(childCategoryTag.equals("author")){
                    updateAuthorTable(e, children);
                }
                else{
                    System.out.println("Not author");
                }
            }
        });
        if(bodyComposite.getChildren().length > 1){
            bodyComposite.getChildren()[0].dispose();
        }
        bodyComposite.layout();
        return bodyComposite;
    }

    public String capitalize(String title){
        return title.toUpperCase().charAt(0) + title.substring(1);
    }

    public void updateAuthorTable(MouseEvent e, List<TreeItem> children) {
        String selectedItemText;
        TabItem authorTabItem;
        Point point = new Point(e.x, e.y);
        System.out.println(point);
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
                if (treeItem.getText().equals(selectedItemText)) {
                    authorTabItem = tabFolder.getItem(1);
                    List<TreeItem> treeItemChildren = Arrays.asList(treeItem.getItems());
                    tableComposite.dispose();

                    String childCategoryTag = (String) treeItem.getItem(0).getData("tag");
                    String criteriaName = selectedItemText;
                    String currentCategoryTag = (String) treeItem.getItem(0).getParentItem().getData("tag");

                    authorTabItem.setControl(createTable(treeItemChildren, childCategoryTag, criteriaName, currentCategoryTag));
                    tabFolder.setSelection(authorTabItem);
                }
            }
        }
    }
}