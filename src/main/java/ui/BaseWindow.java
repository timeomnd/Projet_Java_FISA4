package ui;

import javax.swing.JFrame;
import java.awt.Dimension;

public abstract class BaseWindow extends JFrame {

    public BaseWindow(String title, int width, int height) {
        super(title);
        // Define window dimensions
        setSize(new Dimension(width, height));

        // Close the application completely when clicking the close button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Direct call to the UI construction method
        initUI();
    }

    // Abstract method that each child window must implement
    protected abstract void initUI();
}