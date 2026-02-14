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

/**
 * Cash Withdrawal screen — allows custom amount withdrawal.
 * <p>
 * Security measures:
 * - Sufficient balance check before withdrawal (prevents overdraft)
 * - Row-level locking (SELECT ... FOR UPDATE) prevents race conditions
 * - Atomic transaction (commit/rollback)
 * - Amount validation (positive, numeric, multiples of 100, max limit)
 * - Full audit trail
 */
public class CashWithdrawal extends JFrame implements ActionListener {

    private final String cardNumber;
    private JTextField amountField;
    private JButton withdrawButton, backButton;

    private static final double MAX_WITHDRAWAL = 25_000.00; // Max single withdrawal limit

    public CashWithdrawal(String cardNumber) {
        this.cardNumber = cardNumber;

        setTitle("ATM - CASH WITHDRAWAL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // ATM Background
        ImageIcon atmIcon = new ImageIcon(ClassLoader.getSystemResource("icons/atm.jpg"));
        Image atmImg = atmIcon.getImage().getScaledInstance(900, 900, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(atmImg));
        backgroundLabel.setBounds(0, 0, 900, 900);

        // Title
        JLabel titleLabel = new JLabel("Enter the amount you want to withdraw");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(140, 210, 350, 30);
        backgroundLabel.add(titleLabel);

        // Amount text field
        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.BOLD, 16));
        amountField.setBounds(150, 260, 220, 35);
        backgroundLabel.add(amountField);

        // Withdraw button
        withdrawButton = new JButton("Withdraw");
        withdrawButton.setFont(new Font("Raleway", Font.BOLD, 13));
        withdrawButton.setBackground(new Color(220, 220, 220));
        withdrawButton.setFocusPainted(false);
        withdrawButton.setBounds(245, 340, 120, 30);
        withdrawButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        withdrawButton.addActionListener(this);
        backgroundLabel.add(withdrawButton);

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
        if (e.getSource() == withdrawButton) {
            performWithdrawal();
        } else if (e.getSource() == backButton) {
            setVisible(false);
            dispose();
            new Transactions(cardNumber);
        }
    }

    private void performWithdrawal() {
        String amountText = amountField.getText().trim();

        // ─── Input Validation ─────────────────────────────────────
        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an amount.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!amountText.matches("^[0-9]+$")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid whole number (e.g., 1000).",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount format.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (amount <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Amount must be greater than zero.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (amount % 100 != 0) {
            JOptionPane.showMessageDialog(this,
                    "Amount must be in multiples of Rs 100.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (amount > MAX_WITHDRAWAL) {
            JOptionPane.showMessageDialog(this,
                    "Maximum withdrawal per transaction is Rs " + String.format("%.0f", MAX_WITHDRAWAL) + ".",
                    "Limit Exceeded", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ─── Database Transaction (atomic withdrawal) ──────────────
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Lock the row and check balance
            String checkSql = "SELECT balance FROM bank WHERE card_number = ? FOR UPDATE";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, cardNumber);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this,
                                "No account found. Please make a deposit first.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double currentBalance = rs.getDouble("balance");

                    if (amount > currentBalance) {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this,
                                "Insufficient balance. Available: Rs " + String.format("%.2f", currentBalance),
                                "Insufficient Funds", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Deduct amount
                    String updateSql = "UPDATE bank SET balance = balance - ? WHERE card_number = ?";
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                        updatePs.setDouble(1, amount);
                        updatePs.setString(2, cardNumber);
                        updatePs.executeUpdate();
                    }
                }
            }

            // Record transaction
            String txnSql = "INSERT INTO bank_transactions (card_number, transaction_type, amount, transaction_date) VALUES (?, 'Withdrawal', ?, NOW())";
            try (PreparedStatement txnPs = conn.prepareStatement(txnSql)) {
                txnPs.setString(1, cardNumber);
                txnPs.setDouble(2, amount);
                txnPs.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(this,
                    "Rs " + String.format("%.0f", amount) + " Withdrawn Successfully.\nPlease collect your cash.",
                    "Message", JOptionPane.INFORMATION_MESSAGE);

            amountField.setText("");

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            JOptionPane.showMessageDialog(this,
                    "Transaction failed. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
            }
        }
    }

    public static void main(String[] args) {
        new CashWithdrawal("1234567890123456");
    }
}
