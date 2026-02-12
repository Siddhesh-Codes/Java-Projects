package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpTwo extends JFrame implements ActionListener {

    JComboBox<String> religionCombo, categoryCombo, incomeCombo, educationCombo, occupationCombo;
    JTextField panTxtField, aadharTxtField;
    JRadioButton seniorYes, seniorNo, existingYes, existingNo;
    JButton nextButton;

    String formNo;

    SignUpTwo(String formNo) {
        this.formNo = formNo;

        setTitle("NEW ACCOUNT APPLICATION FORM - PAGE 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Page Heading
        JLabel heading = new JLabel("Page 2: Additional Details");
        heading.setFont(new Font("Raleway", Font.BOLD, 22));
        heading.setBounds(200, 30, 400, 30);
        add(heading);

        // Religion
        JLabel religionLabel = new JLabel("Religion:");
        religionLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        religionLabel.setBounds(100, 100, 200, 30);
        add(religionLabel);

        String[] religions = {"Hindu", "Muslim", "Sikh", "Christian", "Other"};
        religionCombo = new JComboBox<>(religions);
        religionCombo.setFont(new Font("Raleway", Font.PLAIN, 14));
        religionCombo.setBounds(300, 100, 300, 30);
        religionCombo.setBackground(Color.WHITE);
        add(religionCombo);

        // Category
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        categoryLabel.setBounds(100, 150, 200, 30);
        add(categoryLabel);

        String[] categories = {"General", "OBC", "SC", "ST", "Other"};
        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Raleway", Font.PLAIN, 14));
        categoryCombo.setBounds(300, 150, 300, 30);
        categoryCombo.setBackground(Color.WHITE);
        add(categoryCombo);

        // Income
        JLabel incomeLabel = new JLabel("Income:");
        incomeLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        incomeLabel.setBounds(100, 200, 200, 30);
        add(incomeLabel);

        String[] incomes = {"Null", "< 1,50,000", "< 2,50,000", "< 5,00,000", "Up to 10,00,000", "Above 10,00,000"};
        incomeCombo = new JComboBox<>(incomes);
        incomeCombo.setFont(new Font("Raleway", Font.PLAIN, 14));
        incomeCombo.setBounds(300, 200, 300, 30);
        incomeCombo.setBackground(Color.WHITE);
        add(incomeCombo);

        // Educational Qualification
        JLabel educationLabel = new JLabel("Educational Qualification:");
        educationLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        educationLabel.setBounds(100, 250, 200, 40);
        add(educationLabel);

        String[] educations = {"Non-Graduation", "Graduation", "Post-Graduation", "Doctorate", "Others"};
        educationCombo = new JComboBox<>(educations);
        educationCombo.setFont(new Font("Raleway", Font.PLAIN, 14));
        educationCombo.setBounds(300, 255, 300, 30);
        educationCombo.setBackground(Color.WHITE);
        add(educationCombo);

        // Occupation
        JLabel occupationLabel = new JLabel("Occupation:");
        occupationLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        occupationLabel.setBounds(100, 310, 200, 30);
        add(occupationLabel);

        String[] occupations = {"Salaried", "Self-Employed", "Business", "Student", "Retired", "Others"};
        occupationCombo = new JComboBox<>(occupations);
        occupationCombo.setFont(new Font("Raleway", Font.PLAIN, 14));
        occupationCombo.setBounds(300, 310, 300, 30);
        occupationCombo.setBackground(Color.WHITE);
        add(occupationCombo);

        // PAN Number
        JLabel panLabel = new JLabel("PAN Number:");
        panLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        panLabel.setBounds(100, 370, 200, 30);
        add(panLabel);

        panTxtField = new JTextField();
        panTxtField.setFont(new Font("Raleway", Font.PLAIN, 14));
        panTxtField.setBounds(300, 370, 300, 30);
        add(panTxtField);

        // Aadhar Number
        JLabel aadharLabel = new JLabel("Aadhar Number:");
        aadharLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        aadharLabel.setBounds(100, 420, 200, 30);
        add(aadharLabel);

        aadharTxtField = new JTextField();
        aadharTxtField.setFont(new Font("Raleway", Font.PLAIN, 14));
        aadharTxtField.setBounds(300, 420, 300, 30);
        add(aadharTxtField);

        // Senior Citizen
        JLabel seniorLabel = new JLabel("Senior Citizen:");
        seniorLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        seniorLabel.setBounds(100, 480, 200, 30);
        add(seniorLabel);

        seniorYes = new JRadioButton("Yes");
        seniorYes.setFont(new Font("Raleway", Font.BOLD, 14));
        seniorYes.setBounds(300, 480, 80, 30);
        seniorYes.setBackground(Color.WHITE);
        add(seniorYes);

        seniorNo = new JRadioButton("No");
        seniorNo.setFont(new Font("Raleway", Font.BOLD, 14));
        seniorNo.setBounds(400, 480, 80, 30);
        seniorNo.setBackground(Color.WHITE);
        seniorNo.setSelected(true);
        add(seniorNo);

        ButtonGroup seniorGroup = new ButtonGroup();
        seniorGroup.add(seniorYes);
        seniorGroup.add(seniorNo);

        // Existing Account
        JLabel existingLabel = new JLabel("Existing Account:");
        existingLabel.setFont(new Font("Raleway", Font.BOLD, 16));
        existingLabel.setBounds(100, 530, 200, 30);
        add(existingLabel);

        existingYes = new JRadioButton("Yes");
        existingYes.setFont(new Font("Raleway", Font.BOLD, 14));
        existingYes.setBounds(300, 530, 80, 30);
        existingYes.setBackground(Color.WHITE);
        existingYes.setSelected(true);
        add(existingYes);

        existingNo = new JRadioButton("No");
        existingNo.setFont(new Font("Raleway", Font.BOLD, 14));
        existingNo.setBounds(400, 530, 80, 30);
        existingNo.setBackground(Color.WHITE);
        add(existingNo);

        ButtonGroup existingGroup = new ButtonGroup();
        existingGroup.add(existingYes);
        existingGroup.add(existingNo);

        // Next Button
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Raleway", Font.BOLD, 14));
        nextButton.setBounds(520, 620, 80, 30);
        nextButton.setBackground(Color.BLACK);
        nextButton.setForeground(Color.WHITE);
        nextButton.addActionListener(this);
        add(nextButton);

        setSize(700, 700);
        setLocation(400, 50);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            String religion = (String) religionCombo.getSelectedItem();
            String category = (String) categoryCombo.getSelectedItem();
            String income = (String) incomeCombo.getSelectedItem();
            String education = (String) educationCombo.getSelectedItem();
            String occupation = (String) occupationCombo.getSelectedItem();
            String pan = DBConnection.sanitize(panTxtField.getText());
            String aadhar = DBConnection.sanitize(aadharTxtField.getText());
            String seniorCitizen = seniorYes.isSelected() ? "Yes" : "No";
            String existingAccount = existingYes.isSelected() ? "Yes" : "No";

            // ─── Validation ────────────────────────────────────────
            if (pan.isEmpty() || aadhar.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Please fill PAN Number and Aadhar Number.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // PAN format: 5 letters + 4 digits + 1 letter (e.g., ABCDE1234F)
            if (!pan.toUpperCase().matches("^[A-Z]{5}[0-9]{4}[A-Z]$")) {
                JOptionPane.showMessageDialog(null,
                        "PAN Number must be in format: ABCDE1234F (5 letters, 4 digits, 1 letter).",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Aadhar: exactly 12 digits
            if (!aadhar.matches("^[0-9]{12}$")) {
                JOptionPane.showMessageDialog(null,
                        "Aadhar Number must be exactly 12 digits.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                String sql = "UPDATE signup SET religion = ?, category = ?, income = ?, education = ?, occupation = ?, pan_number = ?, aadhar_number = ?, senior_citizen = ?, existing_account = ? WHERE form_no = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, religion);
                    ps.setString(2, category);
                    ps.setString(3, income);
                    ps.setString(4, education);
                    ps.setString(5, occupation);
                    ps.setString(6, pan.toUpperCase());
                    ps.setString(7, aadhar);
                    ps.setString(8, seniorCitizen);
                    ps.setString(9, existingAccount);
                    ps.setString(10, formNo);
                    ps.executeUpdate();
                }

                setVisible(false);
                new SignUpThree(formNo);

            } catch (ClassNotFoundException | SQLException | IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "An error occurred while saving your details. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SignUpTwo("");
    }
}
