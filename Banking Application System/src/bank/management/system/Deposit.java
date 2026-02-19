package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Deposit screen — allows users to deposit money into their account.
 * <p>
 * Security measures:
 * - Amount validation (positive, numeric, max limit)
 * - Parameterized SQL queries (prevents SQL injection)
 * - Database transaction with rollback on failure (atomicity)
 * - Input sanitization
 * - Audit trail via bank_transactions table
 */
public class Deposit extends JFrame implements ActionListener {

    private final String cardNumber;
    private JTextField amountField;
    private JButton depositButton, backButton;

    private static final BigDecimal MAX_DEPOSIT = new BigDecimal("500000.00"); // Max single deposit limit

    public Deposit(String cardNumber) {
        this.cardNumber = cardNumber;

        setTitle("ATM - DEPOSIT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // ATM Background
        java.net.URL atmUrl = ClassLoader.getSystemResource("icons/atm.jpg");
        JLabel backgroundLabel;
        if (atmUrl != null) {
            ImageIcon atmIcon = new ImageIcon(atmUrl);
            Image atmImg = atmIcon.getImage().getScaledInstance(900, 900, Image.SCALE_SMOOTH);
            backgroundLabel = new JLabel(new ImageIcon(atmImg));
        } else {
            System.err.println("WARNING: icons/atm.jpg not found on classpath");
            backgroundLabel = new JLabel();
            backgroundLabel.setOpaque(true);
            backgroundLabel.setBackground(Color.DARK_GRAY);
        }
        backgroundLabel.setBounds(0, 0, 900, 900);

        // Title
        JLabel titleLabel = new JLabel("Enter the amount you want to deposit");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(150, 210, 350, 30);
        backgroundLabel.add(titleLabel);

        // Amount text field
        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.BOLD, 16));
        amountField.setBounds(150, 260, 220, 35);
        backgroundLabel.add(amountField);

        // Deposit button
        depositButton = new JButton("Deposit");
        depositButton.setFont(new Font("Raleway", Font.BOLD, 13));
        depositButton.setBackground(new Color(220, 220, 220));
        depositButton.setFocusPainted(false);
        depositButton.setBounds(245, 340, 120, 30);
        depositButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        depositButton.addActionListener(this);
        backgroundLabel.add(depositButton);

        // Back button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Raleway", Font.BOLD, 13));
        backButton.setBackground(new Color(220, 220, 220));
        backButton.setFocusPainted(false);
        backButton.setBounds(245, 380, 120, 30);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        backgroundLabel.add(backButton);

        add(backgroundLabel);

        setSize(900, 900);
        setLocation(300, 0);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == depositButton) {
            performDeposit();
        } else if (e.getSource() == backButton) {
            setVisible(false);
            dispose();
            new Transactions(cardNumber);
        }
    }

    private void performDeposit() {
        String amountText = amountField.getText().trim();

        // ─── Input Validation ─────────────────────────────────────
        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an amount.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Allow only digits and optional one decimal point
        if (!amountText.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid numeric amount (e.g., 1000 or 1000.50).",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount format.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Amount must be greater than zero.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (amount.compareTo(MAX_DEPOSIT) > 0) {
            JOptionPane.showMessageDialog(this,
                    "Maximum deposit per transaction is Rs " + MAX_DEPOSIT.toPlainString() + ".",
                    "Limit Exceeded", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ─── Database Transaction (atomic deposit) ─────────────────
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Atomic upsert: insert new row or add to existing balance
                String upsertSql = "INSERT INTO bank (card_number, balance) VALUES (?, ?) "
                                 + "ON DUPLICATE KEY UPDATE balance = balance + ?";
                try (PreparedStatement upsertPs = conn.prepareStatement(upsertSql)) {
                    upsertPs.setString(1, cardNumber);
                    upsertPs.setBigDecimal(2, amount);
                    upsertPs.setBigDecimal(3, amount);
                    upsertPs.executeUpdate();
                }

                // Record transaction in audit log
                String txnSql = "INSERT INTO bank_transactions (card_number, transaction_type, amount, transaction_date) VALUES (?, 'Deposit', ?, NOW())";
                try (PreparedStatement txnPs = conn.prepareStatement(txnSql)) {
                    txnPs.setString(1, cardNumber);
                    txnPs.setBigDecimal(2, amount);
                    txnPs.executeUpdate();
                }

                conn.commit();

                JOptionPane.showMessageDialog(this,
                        "Rs " + amount.toPlainString() + " Deposited Successfully",
                        "Message", JOptionPane.INFORMATION_MESSAGE);

                amountField.setText("");
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Transaction failed. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Deposit("1234567890123456");
    }
}
