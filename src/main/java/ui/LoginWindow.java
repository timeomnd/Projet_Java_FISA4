package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends BaseWindow implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginWindow() {
        // Call to BaseWindow constructor (Title, Width, Height)
        super("Login - My Game Library", 400, 200);
        this.setVisible(true);
    }

    @Override
    protected void initUI() {
        // Main panel with margin
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Grid for the form (2 rows, 2 columns)
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Attach click listeners
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Assembly
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Input validation
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                // Simulation of a successful login (waiting for DAO)
                if (username.equals("admin") && password.equals("1234")) {
                    JOptionPane.showMessageDialog(this,
                            "Login successful! Welcome " + username + ".",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    this.dispose(); // Close the login window

                    // Here we will launch the DashboardWindow later
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid credentials.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == registerButton) {
            JOptionPane.showMessageDialog(this,
                    "The registration window will be available soon.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}