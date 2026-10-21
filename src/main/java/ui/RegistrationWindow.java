package ui;

import dao.UserDao;
import models.dto.UserDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// Removed LocalDateTime imports as the database handles timestamps automatically

public class RegistrationWindow extends BaseWindow implements ActionListener {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private JButton registerButton;
    private JButton cancelButton;

    public RegistrationWindow() {
        super("Create an Account", 400, 400);
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
            this.dispose();
            new LoginWindow();
        }
        else if (e.getSource() == registerButton) {
            registerUser();
        }
    }

    private void registerUser() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Pack the form data into a Data Transfer Object
        UserDto newUser = new UserDto(firstName, lastName, email, username, password);

        // Call the backend to insert the user into the database
        UserDao userDao = new UserDao();
        boolean success = userDao.registerUser(newUser);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Account successfully created!\nWelcome " + firstName + "!",
                    "Registration Success",
                    JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
            new LoginWindow();
        } else {
            // Display an error if the username or email already exists, or if DB fails
            JOptionPane.showMessageDialog(this,
                    "Registration failed. Username or email might already exist.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}