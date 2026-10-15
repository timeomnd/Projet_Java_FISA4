package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistrationWindow extends BaseWindow implements ActionListener {

    // Form fields mapped to your MCD (image_c97c6b.png)
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField; // Hides the typed characters

    private JButton registerButton;
    private JButton cancelButton;

    public RegistrationWindow() {
        super("Create an Account", 400, 400);

        // Prevent closing the entire application, just this window
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setVisible(true);
    }

    @Override
    protected void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TITLE PANEL ---
        JLabel titleLabel = new JLabel("Join the Game Library", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- CENTER PANEL (Form Grid) ---
        // 5 rows, 2 columns for the labels and text fields
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));

        formPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        formPanel.add(firstNameField);

        formPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        formPanel.add(lastNameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- SOUTH PANEL (Buttons) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");

        registerButton.addActionListener(this);
        cancelButton.addActionListener(this);

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            this.dispose(); // Close registration window
            new LoginWindow(); // Re-open login window
        }
        else if (e.getSource() == registerButton) {
            registerUser();
        }
    }

    private void registerUser() {
        // Retrieve data from fields
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();

        // JPasswordField returns a char array for security, we convert it to String
        String password = new String(passwordField.getPassword());

        // Basic validation: check if any field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simulate creation date generation for the `_created_at` field in your DB
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // LATER: This is where you will call the DatabaseManager/DAO.
        // It will need to do 2 INSERT queries:
        // 1. INSERT INTO registration_form (first_name, last_name, email, _created_at)
        // 2. INSERT INTO authentication (username, _password_hash, id_registration_form)

        // For now, we just simulate a success message
        JOptionPane.showMessageDialog(this,
                "Account successfully created!\nWelcome " + firstName + "!",
                "Registration Success",
                JOptionPane.INFORMATION_MESSAGE);

        this.dispose(); // Close registration window
        new LoginWindow(); // Go back to login so the user can log in
    }
}