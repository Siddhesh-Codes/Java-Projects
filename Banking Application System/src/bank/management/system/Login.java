package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Login extends JFrame implements ActionListener {

    JButton login, clear, signup;
    JLabel cardNo, pin;
    JTextField cardTextField;
    JPasswordField pinTextField;

    // ─── Brute Force Protection ────────────────────────────────────────
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION_MS = 5 * 60 * 1000; // 5 minutes
    private int failedAttempts = 0;
    private long lockoutEndTime = 0;

    Login() {
        setTitle("ATM MACHINE");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 480);
        setVisible(true);
        setLocation(350, 200);

        // Logo
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/logo.jpg"));
        Image i2 = i1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel label = new JLabel(i3);
        label.setBounds(70, 10, 100, 100);
        add(label);

        // Welcome Message
        JLabel text = new JLabel("Welcome to ATM", SwingConstants.CENTER);
        text.setBounds(200, 40, 400, 40);
        text.setFont(new Font("Onward", Font.BOLD, 20));
        add(text);

        // Card No
        cardNo = new JLabel("Card No:");
        cardNo.setBounds(200, 150, 150, 40);
        cardNo.setFont(new Font("Raleway", Font.BOLD, 20));
        add(cardNo);

        cardTextField = new JTextField();
        cardTextField.setBounds(350, 155, 230, 30);
        cardTextField.setFont(new Font("Arial", Font.BOLD, 14));
        add(cardTextField);

        // PIN
        pin = new JLabel("Enter Pin:");
        pin.setBounds(200, 200, 150, 40);
        pin.setFont(new Font("Raleway", Font.BOLD, 20));
        add(pin);

        pinTextField = new JPasswordField();
        pinTextField.setBounds(350, 205, 230, 30);
        pinTextField.setFont(new Font("Arial", Font.BOLD, 14));
        add(pinTextField);

        // Sign In Button
        login = new JButton("SIGN IN");
        login.setBounds(350, 300, 100, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        add(login);

        // Clear Button
        clear = new JButton("CLEAR");
        clear.setBounds(480, 300, 100, 30);
        clear.setBackground(Color.BLACK);
        clear.setForeground(Color.WHITE);
        clear.addActionListener(this);
        add(clear);

        // Sign Up Button
        signup = new JButton("SIGN UP");
        signup.setBounds(350, 350, 230, 30);
        signup.setBackground(Color.BLACK);
        signup.setForeground(Color.WHITE);
        signup.addActionListener(this);
        add(signup);

        getContentPane().setBackground(Color.WHITE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            // ─── Check if account is locked out ────────────────────
            if (System.currentTimeMillis() < lockoutEndTime) {
                long remainingSeconds = (lockoutEndTime - System.currentTimeMillis()) / 1000;
                JOptionPane.showMessageDialog(null,
                        "Account temporarily locked due to too many failed attempts.\n"
                                + "Please try again in " + remainingSeconds + " seconds.",
                        "Account Locked", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cardNumber = DBConnection.sanitize(cardTextField.getText());
            char[] pinInput = pinTextField.getPassword();

            try {
                // ─── Input Validation ──────────────────────────────────
                if (cardNumber.isEmpty() || pinInput.length == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter Card Number and PIN.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Card number: 16 digits only
                if (!cardNumber.matches("^[0-9]{16}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Card Number must be exactly 16 digits.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // PIN: exactly 4 digits
                if (!DBConnection.isValidPinFormat(pinInput)) {
                    JOptionPane.showMessageDialog(null,
                            "PIN must be exactly 4 digits.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // ─── Authenticate against signup table (hashed PIN) ────
                try {
                    Connection conn = DBConnection.getConnection();
                    // Only fetch the hash and salt — never select * in production
                    String sql = "SELECT pin_hash, pin_salt FROM signup WHERE card_number = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, cardNumber);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                String storedHash = rs.getString("pin_hash");
                                String storedSalt = rs.getString("pin_salt");

                                // Verify PIN using constant-time comparison
                                if (DBConnection.verifyPin(pinInput, storedHash, storedSalt)) {
                                    // Reset failed attempts on successful login
                                    failedAttempts = 0;
                                    // Navigate to main ATM transaction menu
                                    setVisible(false);
                                    dispose();
                                    new Transactions(cardNumber);
                                } else {
                                    handleFailedLogin();
                                }
                            } else {
                                // Don't reveal whether card exists or not (prevents enumeration)
                                handleFailedLogin();
                            }
                        }
                    }
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "An error occurred. Please try again later.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } finally {
                // Zero out PIN from memory immediately
                if (pinInput != null) Arrays.fill(pinInput, '\0');
                pinTextField.setText("");
            }

        } else if (e.getSource() == clear) {
            cardTextField.setText("");
            pinTextField.setText("");

        } else if (e.getSource() == signup) {
            setVisible(false);
            dispose();
            new SignUpOne();
        }
    }

    /**
     * Handles a failed login attempt with brute force protection.
     * Locks the account after MAX_LOGIN_ATTEMPTS consecutive failures.
     */
    private void handleFailedLogin() {
        failedAttempts++;
        int remaining = MAX_LOGIN_ATTEMPTS - failedAttempts;

        if (failedAttempts >= MAX_LOGIN_ATTEMPTS) {
            lockoutEndTime = System.currentTimeMillis() + LOCKOUT_DURATION_MS;
            failedAttempts = 0; // Reset counter for next lockout period
            JOptionPane.showMessageDialog(null,
                    "Too many failed attempts. Account locked for 5 minutes.",
                    "Account Locked", JOptionPane.ERROR_MESSAGE);
        } else {
            // Generic message — don't reveal if card number exists
            JOptionPane.showMessageDialog(null,
                    "Invalid Card Number or PIN.\n" + remaining + " attempt(s) remaining.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
