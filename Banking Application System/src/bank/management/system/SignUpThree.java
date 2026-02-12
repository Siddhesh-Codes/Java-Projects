package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpThree extends JFrame implements ActionListener {

    JRadioButton savingAccount, fixedDeposit, currentAccount, recurringDeposit;
    JCheckBox atmCard, internetBanking, mobileBanking, emailAlerts, chequeBook, eStatement;
    JCheckBox declarationCheck;
    JButton submitButton, cancelButton;

    String formNo;
    String cardNumber;
    String rawPin; // The plain 4-digit PIN (shown to user once, then hashed for storage)

    SignUpThree(String formNo) {
        this.formNo = formNo;

        setTitle("NEW ACCOUNT APPLICATION FORM - PAGE 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // ─── Generate Card Number & PIN using SecureRandom ──────────────
        // SecureRandom is cryptographically strong (unlike java.util.Random)
        SecureRandom secRandom = new SecureRandom();
        long part1 = 1000L + secRandom.nextInt(9000);
        long part2 = 1000L + secRandom.nextInt(9000);
        long part3 = 1000L + secRandom.nextInt(9000);
        long part4 = 1000L + secRandom.nextInt(9000);
        cardNumber = "" + part1 + part2 + part3 + part4;
        rawPin = String.valueOf(1000 + secRandom.nextInt(9000));

        String lastFour = cardNumber.substring(cardNumber.length() - 4);

        // Page Heading
        JLabel heading = new JLabel("Page 3: Account Details");
        heading.setFont(new Font("Raleway", Font.BOLD, 22));
        heading.setBounds(200, 30, 400, 30);
        add(heading);

        // Account Type
        JLabel accountTypeLabel = new JLabel("Account Type");
        accountTypeLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        accountTypeLabel.setBounds(100, 90, 200, 30);
        add(accountTypeLabel);

        savingAccount = new JRadioButton("Saving Account");
        savingAccount.setFont(new Font("Raleway", Font.BOLD, 14));
        savingAccount.setBounds(100, 130, 180, 30);
        savingAccount.setBackground(Color.WHITE);
        savingAccount.setSelected(true);
        add(savingAccount);

        fixedDeposit = new JRadioButton("Fixed Deposit Account");
        fixedDeposit.setFont(new Font("Raleway", Font.BOLD, 14));
        fixedDeposit.setBounds(350, 130, 220, 30);
        fixedDeposit.setBackground(Color.WHITE);
        add(fixedDeposit);

        currentAccount = new JRadioButton("Current Account");
        currentAccount.setFont(new Font("Raleway", Font.BOLD, 14));
        currentAccount.setBounds(100, 165, 180, 30);
        currentAccount.setBackground(Color.WHITE);
        add(currentAccount);

        recurringDeposit = new JRadioButton("Recurring Deposit Account");
        recurringDeposit.setFont(new Font("Raleway", Font.BOLD, 14));
        recurringDeposit.setBounds(350, 165, 250, 30);
        recurringDeposit.setBackground(Color.WHITE);
        add(recurringDeposit);

        ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(savingAccount);
        accountGroup.add(fixedDeposit);
        accountGroup.add(currentAccount);
        accountGroup.add(recurringDeposit);

        // Card Number (masked for security — only last 4 digits shown)
        JLabel cardLabel = new JLabel("Card Number");
        cardLabel.setFont(new Font("Raleway", Font.BOLD, 18));
        cardLabel.setBounds(100, 220, 200, 30);
        add(cardLabel);

        JLabel cardSubLabel = new JLabel("Your 16 Digit Card Number");
        cardSubLabel.setFont(new Font("Raleway", Font.PLAIN, 11));
        cardSubLabel.setForeground(Color.GRAY);
        cardSubLabel.setBounds(100, 245, 200, 20);
        add(cardSubLabel);

        JLabel cardDisplay = new JLabel("XXXX-XXXX-XXXX-" + lastFour);
        cardDisplay.setFont(new Font("Raleway", Font.BOLD, 18));
        cardDisplay.setBounds(320, 220, 300, 30);
        add(cardDisplay);

        // PIN (masked — user will receive full details after submission)
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Raleway", Font.BOLD, 18));
        pinLabel.setBounds(100, 280, 200, 30);
        add(pinLabel);

        JLabel pinSubLabel = new JLabel("Your 4 Digit Password");
        pinSubLabel.setFont(new Font("Raleway", Font.PLAIN, 11));
        pinSubLabel.setForeground(Color.GRAY);
        pinSubLabel.setBounds(100, 305, 200, 20);
        add(pinSubLabel);

        JLabel pinDisplay = new JLabel("XXXX");
        pinDisplay.setFont(new Font("Raleway", Font.BOLD, 18));
        pinDisplay.setBounds(320, 280, 200, 30);
        add(pinDisplay);

        // Services Required
        JLabel servicesLabel = new JLabel("Services Required:");
        servicesLabel.setFont(new Font("Raleway", Font.BOLD, 18));
        servicesLabel.setBounds(100, 350, 250, 30);
        add(servicesLabel);

        atmCard = new JCheckBox("ATM CARD");
        atmCard.setFont(new Font("Raleway", Font.BOLD, 14));
        atmCard.setBounds(100, 395, 180, 30);
        atmCard.setBackground(Color.WHITE);
        add(atmCard);

        internetBanking = new JCheckBox("Internet Banking");
        internetBanking.setFont(new Font("Raleway", Font.BOLD, 14));
        internetBanking.setBounds(350, 395, 200, 30);
        internetBanking.setBackground(Color.WHITE);
        add(internetBanking);

        mobileBanking = new JCheckBox("Mobile Banking");
        mobileBanking.setFont(new Font("Raleway", Font.BOLD, 14));
        mobileBanking.setBounds(100, 435, 180, 30);
        mobileBanking.setBackground(Color.WHITE);
        add(mobileBanking);

        emailAlerts = new JCheckBox("EMAIL & SMS Alerts");
        emailAlerts.setFont(new Font("Raleway", Font.BOLD, 14));
        emailAlerts.setBounds(350, 435, 220, 30);
        emailAlerts.setBackground(Color.WHITE);
        add(emailAlerts);

        chequeBook = new JCheckBox("Cheque Book");
        chequeBook.setFont(new Font("Raleway", Font.BOLD, 14));
        chequeBook.setBounds(100, 475, 180, 30);
        chequeBook.setBackground(Color.WHITE);
        add(chequeBook);

        eStatement = new JCheckBox("E-Statement");
        eStatement.setFont(new Font("Raleway", Font.BOLD, 14));
        eStatement.setBounds(350, 475, 200, 30);
        eStatement.setBackground(Color.WHITE);
        add(eStatement);

        // Declaration
        declarationCheck = new JCheckBox("I hereby declare that the above entered details are correct to the best of my knowledge");
        declarationCheck.setFont(new Font("Raleway", Font.PLAIN, 12));
        declarationCheck.setBounds(100, 530, 550, 30);
        declarationCheck.setBackground(Color.WHITE);
        add(declarationCheck);

        // Submit Button
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Raleway", Font.BOLD, 14));
        submitButton.setBounds(200, 590, 100, 30);
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(this);
        add(submitButton);

        // Cancel Button
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Raleway", Font.BOLD, 14));
        cancelButton.setBounds(350, 590, 100, 30);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(this);
        add(cancelButton);

        setSize(700, 700);
        setLocation(400, 50);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            if (!declarationCheck.isSelected()) {
                JOptionPane.showMessageDialog(null,
                        "Please accept the declaration to proceed.",
                        "Declaration Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String accountType = "";
            if (savingAccount.isSelected()) accountType = "Saving Account";
            else if (fixedDeposit.isSelected()) accountType = "Fixed Deposit Account";
            else if (currentAccount.isSelected()) accountType = "Current Account";
            else if (recurringDeposit.isSelected()) accountType = "Recurring Deposit Account";

            // Collect services
            StringBuilder services = new StringBuilder();
            if (atmCard.isSelected()) services.append("ATM CARD, ");
            if (internetBanking.isSelected()) services.append("Internet Banking, ");
            if (mobileBanking.isSelected()) services.append("Mobile Banking, ");
            if (emailAlerts.isSelected()) services.append("EMAIL & SMS Alerts, ");
            if (chequeBook.isSelected()) services.append("Cheque Book, ");
            if (eStatement.isSelected()) services.append("E-Statement, ");

            String servicesStr = services.toString();
            if (servicesStr.endsWith(", ")) {
                servicesStr = servicesStr.substring(0, servicesStr.length() - 2);
            }

            // ─── Hash the PIN before storing ────────────────────────
            // Generate a unique salt for this user
            String salt = DBConnection.generateSalt();
            String hashedPin = DBConnection.hashPin(rawPin, salt);

            try {
                Connection conn = DBConnection.getConnection();
                String sql = "UPDATE signup SET account_type = ?, card_number = ?, pin_hash = ?, pin_salt = ?, services = ? WHERE form_no = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, accountType);
                    ps.setString(2, cardNumber);
                    ps.setString(3, hashedPin);   // Store hash, NOT plain PIN
                    ps.setString(4, salt);        // Store salt for verification
                    ps.setString(5, servicesStr);
                    ps.setString(6, formNo);
                    ps.executeUpdate();
                }

                // Show credentials to user ONCE — they must note them down
                JOptionPane.showMessageDialog(null,
                        "Account Created Successfully!\n\n"
                                + "Your Card Number: " + cardNumber + "\n"
                                + "Your PIN: " + rawPin + "\n\n"
                                + "IMPORTANT: Please note down your Card Number and PIN.\n"
                                + "This is the ONLY time your PIN will be displayed.",
                        "Account Created", JOptionPane.INFORMATION_MESSAGE);

                // Clear sensitive data from memory
                rawPin = null;

                setVisible(false);
                new Login();

            } catch (ClassNotFoundException | SQLException | IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "An error occurred while creating your account. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        } else if (e.getSource() == cancelButton) {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to cancel? All entered data will be lost.",
                    "Confirm Cancel", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                setVisible(false);
                new Login();
            }
        }
    }

    public static void main(String[] args) {
        new SignUpThree("");
    }
}
