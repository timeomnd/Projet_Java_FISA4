package ui;

import dao.UserDao;

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
        super("Login - My Game Library", 400, 200);
        this.setVisible(true);
    }

    @Override
    protected void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                // Call the backend to verify credentials
                UserDao userDao = new UserDao();
                int loggedInUserId = userDao.loginUser(username, password);

                // If loggedInUserId is not -1, the credentials are correct
                if (loggedInUserId != -1) {
                    JOptionPane.showMessageDialog(this,
                            "Login successful! Welcome " + username + ".",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    this.dispose();

                    new DashboardWindow(loggedInUserId);

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid credentials. Please try again.",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == registerButton) {
            this.dispose();
            new RegistrationWindow();
        }
    }
}