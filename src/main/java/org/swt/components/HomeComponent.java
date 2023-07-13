package org.swt.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class HomeComponent {
    private final Shell shell;
    private final Composite composite;

    public HomeComponent(Shell shell) {
        this.composite = new Composite(shell, SWT.NONE);
        this.shell = shell;
    }

    public void createHomeComponent() {
        composite.setLayout(new GridLayout(1, false));
        shell.setText("Home - SWT App");
        shell.setMaximized(true);

        Label welcomeLabel = new Label(composite, SWT.CENTER);
        welcomeLabel.setText("Welcome to Home Page");
        Font font = new Font(composite.getDisplay(), "Helvetica", 20,SWT.BOLD);
        welcomeLabel.setFont(font);
        welcomeLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        Composite bodyComposite = new Composite(composite, SWT.NONE);
        bodyComposite.setLayout(new GridLayout(2, false));
        GridData bodyCompositeData = new GridData(SWT.FILL, SWT.FILL, false, false);
        bodyCompositeData.heightHint = 400;
        bodyComposite.setLayoutData(bodyCompositeData);

        Tree tree = new Tree(bodyComposite, SWT.NONE);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        TreeItem categoryNode = new TreeItem(tree, SWT.NONE);
        categoryNode.setText("Category");

        TreeItem authorNode = new TreeItem(categoryNode, SWT.NONE);
        authorNode.setText("Author");

        TreeItem titleNode1 = new TreeItem(authorNode, SWT.NONE);
        titleNode1.setText("Title 1");

        TreeItem titleNode2 = new TreeItem(authorNode, SWT.NONE);
        titleNode2.setText("Title 2");

        // Read XML file and populate the tree dynamically
        // ...

        // Apply localization
        applyLocalization(bodyComposite);

    }

    private void applyLocalization(Composite bodyComposite) {
        Tree tree = null;
        Control[] children = bodyComposite.getChildren();
        for (Control control : children) {
            if (control instanceof Tree) {
                tree = (Tree) control;
                break;
            }
        }

        if (tree != null) {
            // Retrieve localized strings based on the selected language
            String categoryLabel = getLocalizedString("Category");
            String authorLabel = getLocalizedString("Author");
            String titleLabel = getLocalizedString("Title");

            // Update the labels in the tree hierarchy
            tree.getItem(0).setText(categoryLabel);
            tree.getItem(0).getItem(0).setText(authorLabel);
            tree.getItem(0).getItem(0).getItem(0).setText(titleLabel);
        }
    }


    private String getLocalizedString(String key) {
        // Retrieve localized string based on the selected language and key
        // ...
        // Example: return ResourceBundle.getBundle("translations").getString(key);
        return key; // Placeholder implementation
    }
}