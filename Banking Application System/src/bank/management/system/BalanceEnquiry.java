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
 * Balance Enquiry screen — displays the current account balance.
 * <p>
 * Security measures:
 * - Card number displayed in masked format (only last 4 digits visible)
 * - Parameterized queries prevent SQL injection
 * - Read-only operation
 */
public class BalanceEnquiry extends JFrame implements ActionListener {

    private final String cardNumber;
    private JButton backButton;

    public BalanceEnquiry(String cardNumber) {
        this.cardNumber = cardNumber;

        setTitle("ATM - BALANCE ENQUIRY");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JLabel titleLabel = new JLabel("Balance Enquiry");
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(210, 210, 250, 30);
        backgroundLabel.add(titleLabel);

        // Masked card number (safe for null/short values)
        String maskedCard;
        if (cardNumber != null && cardNumber.length() >= 4) {
            maskedCard = "XXXX-XXXX-XXXX-" + cardNumber.substring(cardNumber.length() - 4);
        } else {
            maskedCard = "XXXX-XXXX-XXXX-????";
            System.err.println("WARNING: cardNumber is null or shorter than 4 characters");
        }
        JLabel cardLabel = new JLabel("Card: " + maskedCard);
        cardLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        cardLabel.setForeground(Color.GREEN);
        cardLabel.setBounds(170, 260, 300, 25);
        backgroundLabel.add(cardLabel);

        // Balance display
        JLabel balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Consolas", Font.BOLD, 20));
        balanceLabel.setForeground(Color.GREEN);
        balanceLabel.setBounds(150, 305, 350, 35);
        backgroundLabel.add(balanceLabel);

        // Load balance
        loadBalance(balanceLabel);

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

    private void loadBalance(JLabel balanceLabel) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT balance FROM bank WHERE card_number = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cardNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double balance = rs.getDouble("balance");
                        balanceLabel.setText("Balance: Rs " + String.format("%,.2f", balance));
                    } else {
                        balanceLabel.setText("No record found for this card.");
                        System.err.println("BalanceEnquiry: No bank record found for card ending "
                                + (cardNumber != null && cardNumber.length() >= 4
                                   ? cardNumber.substring(cardNumber.length() - 4) : "????"));
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            balanceLabel.setText("Error loading balance.");
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            setVisible(false);
            dispose();
            new Transactions(cardNumber);
        }
    }

    public static void main(String[] args) {
        new BalanceEnquiry("1234567890123456");
    }
}
