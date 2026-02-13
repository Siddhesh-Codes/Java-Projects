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
import java.time.LocalDate;
import java.time.YearMonth;

public class SignUpOne extends JFrame {

    // Text Fields
    JTextField nameTxtField = new JTextField();
    JTextField fatherTxtField = new JTextField();
    JTextField emailAddressTxtField = new JTextField();
    JTextField addressTxtField = new JTextField();
    JTextField cityTxtField = new JTextField();
    JTextField stateTxtField = new JTextField();
    JTextField pinCodeTxtField = new JTextField();

    // Radio Buttons
    JRadioButton maleRadioButton = new JRadioButton("Male");
    JRadioButton femaleRadioButton = new JRadioButton("Female");
    JRadioButton otherRadioButton = new JRadioButton("Other");

    JRadioButton marriedRadioButton = new JRadioButton("Married");
    JRadioButton unmarriedRadioButton = new JRadioButton("Unmarried");
    JRadioButton others = new JRadioButton("Other");

    // Date of Birth dropdowns
    JComboBox<String> dayCombo, monthCombo, yearCombo;

    SignUpOne() {
        setTitle("NEW ACCOUNT APPLICATION FORM - PAGE 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Use SecureRandom for form number (cryptographically secure)
        SecureRandom secureRandom = new SecureRandom();
        long random = 1000L + secureRandom.nextInt(9000);

        JLabel formNo = new JLabel("Application Form No. " + random);
        formNo.setFont(new Font("Raleway", Font.BOLD, 38));
        formNo.setBounds(140, 20, 600, 40);
        add(formNo);

        // Page Heading
        JLabel personalDetails = new JLabel("Page 1: Personal Details");
        personalDetails.setFont(new Font("Raleway", Font.BOLD, 20));
        personalDetails.setBounds(290, 80, 400, 30);
        add(personalDetails);

        // Name
        JLabel name = new JLabel("Name: ");
        name.setFont(new Font("Raleway", Font.BOLD, 18));
        name.setBounds(100, 140, 200, 30);
        add(name);
        nameTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        nameTxtField.setBounds(300, 140, 400, 30);
        add(nameTxtField);

        // Father Name
        JLabel fname = new JLabel("Father Name: ");
        fname.setFont(new Font("Raleway", Font.BOLD, 18));
        fname.setBounds(100, 190, 200, 30);
        add(fname);
        fatherTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        fatherTxtField.setBounds(300, 190, 400, 30);
        add(fatherTxtField);

        // DOB
        JLabel dateOfBirth = new JLabel("Date of Birth: ");
        dateOfBirth.setFont(new Font("Raleway", Font.BOLD, 18));
        dateOfBirth.setBounds(100, 240, 400, 30);
        add(dateOfBirth);

        // Day dropdown (1-31)
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) days[i] = String.format("%02d", i + 1);
        dayCombo = new JComboBox<>(days);
        dayCombo.setFont(new Font("Raleway", Font.BOLD, 14));
        dayCombo.setBounds(300, 240, 70, 30);
        dayCombo.setBackground(Color.WHITE);
        add(dayCombo);

        // Month dropdown (Jan-Dec)
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                           "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Raleway", Font.BOLD, 14));
        monthCombo.setBounds(380, 240, 80, 30);
        monthCombo.setBackground(Color.WHITE);
        add(monthCombo);

        // Year dropdown (current year down to 1940)
        int currentYear = LocalDate.now().getYear();
        String[] years = new String[currentYear - 1940 + 1];
        for (int i = 0; i < years.length; i++) years[i] = String.valueOf(currentYear - i);
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Raleway", Font.BOLD, 14));
        yearCombo.setBounds(470, 240, 100, 30);
        yearCombo.setBackground(Color.WHITE);
        add(yearCombo);

        // Auto-adjust days when month/year changes
        ActionListener dateUpdateListener = ev -> updateDays();
        monthCombo.addActionListener(dateUpdateListener);
        yearCombo.addActionListener(dateUpdateListener);

        // Gender
        JLabel gender = new JLabel("Gender: ");
        gender.setFont(new Font("Raleway", Font.BOLD, 18));
        gender.setBounds(100, 290, 200, 30);
        add(gender);
        maleRadioButton.setFont(new Font("Raleway", Font.BOLD, 14));
        maleRadioButton.setBounds(300, 290, 100, 30);
        maleRadioButton.setBackground(Color.WHITE);
        add(maleRadioButton);
        femaleRadioButton.setFont(new Font("Raleway", Font.BOLD, 14));
        femaleRadioButton.setBounds(400, 290, 100, 30);
        femaleRadioButton.setBackground(Color.WHITE);
        add(femaleRadioButton);
        otherRadioButton.setFont(new Font("Raleway", Font.BOLD, 14));
        otherRadioButton.setBounds(510, 290, 100, 30);
        otherRadioButton.setBackground(Color.WHITE);
        add(otherRadioButton);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        genderGroup.add(otherRadioButton);

        // E-Mail
        JLabel emailAdd = new JLabel("E-Mail: ");
        emailAdd.setFont(new Font("Raleway", Font.BOLD, 18));
        emailAdd.setBounds(100, 340, 200, 30);
        add(emailAdd);
        emailAddressTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        emailAddressTxtField.setBounds(300, 340, 400, 30);
        add(emailAddressTxtField);

        // Marital Status
        JLabel maritalStatus = new JLabel("Marital Status: ");
        maritalStatus.setFont(new Font("Raleway", Font.BOLD, 18));
        maritalStatus.setBounds(100, 390, 200, 30);
        add(maritalStatus);
        marriedRadioButton.setFont(new Font("Raleway", Font.BOLD, 14));
        marriedRadioButton.setBounds(300, 390, 100, 30);
        marriedRadioButton.setBackground(Color.WHITE);
        add(marriedRadioButton);
        unmarriedRadioButton.setFont(new Font("Raleway", Font.BOLD, 14));
        unmarriedRadioButton.setBounds(400, 390, 120, 30);
        unmarriedRadioButton.setBackground(Color.WHITE);
        add(unmarriedRadioButton);
        others.setFont(new Font("Raleway", Font.BOLD, 14));
        others.setBounds(530, 390, 100, 30);
        others.setBackground(Color.WHITE);
        add(others);

        ButtonGroup maritalStatusGroup = new ButtonGroup();
        maritalStatusGroup.add(marriedRadioButton);
        maritalStatusGroup.add(unmarriedRadioButton);
        maritalStatusGroup.add(others);

        // Address
        JLabel address = new JLabel("Address: ");
        address.setFont(new Font("Raleway", Font.BOLD, 18));
        address.setBounds(100, 440, 200, 30);
        add(address);
        addressTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        addressTxtField.setBounds(300, 440, 400, 30);
        add(addressTxtField);

        // City
        JLabel city = new JLabel("City: ");
        city.setFont(new Font("Raleway", Font.BOLD, 18));
        city.setBounds(100, 490, 200, 30);
        add(city);
        cityTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        cityTxtField.setBounds(300, 490, 400, 30);
        add(cityTxtField);

        // State
        JLabel state = new JLabel("State: ");
        state.setFont(new Font("Raleway", Font.BOLD, 18));
        state.setBounds(100, 540, 200, 30);
        add(state);
        stateTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        stateTxtField.setBounds(300, 540, 400, 30);
        add(stateTxtField);

        // Pin Code
        JLabel pinCode = new JLabel("Pin Code: ");
        pinCode.setFont(new Font("Raleway", Font.BOLD, 18));
        pinCode.setBounds(100, 590, 200, 30);
        add(pinCode);
        pinCodeTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        pinCodeTxtField.setBounds(300, 590, 400, 30);
        add(pinCodeTxtField);

        // Next Button
        JButton nextButton = new JButton("Next");
        nextButton.setFont(new Font("Raleway", Font.BOLD, 14));
        nextButton.setBounds(620, 660, 80, 30);
        nextButton.setBackground(Color.BLACK);
        nextButton.setForeground(Color.WHITE);
        add(nextButton);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Collect and sanitize form data
                String nameVal = DBConnection.sanitize(nameTxtField.getText());
                String father = DBConnection.sanitize(fatherTxtField.getText());
                String email = DBConnection.sanitize(emailAddressTxtField.getText());
                String addressVal = DBConnection.sanitize(addressTxtField.getText());
                String cityVal = DBConnection.sanitize(cityTxtField.getText());
                String stateVal = DBConnection.sanitize(stateTxtField.getText());
                String pinCodeVal = DBConnection.sanitize(pinCodeTxtField.getText());

                // Build date from combo boxes
                int day = dayCombo.getSelectedIndex() + 1;
                int month = monthCombo.getSelectedIndex() + 1;
                int year = Integer.parseInt((String) yearCombo.getSelectedItem());
                String dob = String.format("%04d-%02d-%02d", year, month, day);

                String genderVal = null;
                if (maleRadioButton.isSelected()) genderVal = "Male";
                else if (femaleRadioButton.isSelected()) genderVal = "Female";
                else if (otherRadioButton.isSelected()) genderVal = "Other";

                String maritalVal = null;
                if (marriedRadioButton.isSelected()) maritalVal = "Married";
                else if (unmarriedRadioButton.isSelected()) maritalVal = "Unmarried";
                else if (others.isSelected()) maritalVal = "Other";

                // ─── Validation ────────────────────────────────────────
                if (nameVal.isEmpty() || father.isEmpty() || genderVal == null
                        || email.isEmpty() || maritalVal == null || addressVal.isEmpty()
                        || cityVal.isEmpty() || stateVal.isEmpty() || pinCodeVal.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Name: letters and spaces only, 2-100 characters
                if (!nameVal.matches("^[a-zA-Z\\s]{2,100}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Name must contain only letters and spaces (2-100 characters).",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Father name: letters and spaces only
                if (!father.matches("^[a-zA-Z\\s]{2,100}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Father's Name must contain only letters and spaces (2-100 characters).",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Email format validation
                if (!email.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a valid email address.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // City: letters and spaces only
                if (!cityVal.matches("^[a-zA-Z\\s]{2,50}$")) {
                    JOptionPane.showMessageDialog(null,
                            "City must contain only letters and spaces.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // State: letters and spaces only
                if (!stateVal.matches("^[a-zA-Z\\s]{2,50}$")) {
                    JOptionPane.showMessageDialog(null,
                            "State must contain only letters and spaces.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Pin Code: exactly 6 digits
                if (!pinCodeVal.matches("^[0-9]{6}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Pin Code must be exactly 6 digits.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String formNoStr = formNo.getText().replace("Application Form No. ", "").trim();

                // Insert Page 1 data using try-with-resources for proper resource cleanup
                try {
                    Connection conn = DBConnection.getConnection();
                    String sql = "INSERT INTO signup (form_no, name, father_name, dob, gender, email, marital_status, address, city, state, pin_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, formNoStr);
                        ps.setString(2, nameVal);
                        ps.setString(3, father);
                        ps.setString(4, dob);
                        ps.setString(5, genderVal);
                        ps.setString(6, email);
                        ps.setString(7, maritalVal);
                        ps.setString(8, addressVal);
                        ps.setString(9, cityVal);
                        ps.setString(10, stateVal);
                        ps.setString(11, pinCodeVal);
                        ps.executeUpdate();
                    }

                    // Navigate to Page 2
                    setVisible(false);
                    new SignUpTwo(formNoStr);

                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    // Never expose raw database errors to the user
                    JOptionPane.showMessageDialog(null,
                            "An error occurred while saving your details. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        setSize(850, 800);
        setLocation(350, 10);
        setVisible(true);
    }

    /**
     * Adjusts the Day dropdown to show the correct number of days
     * based on the selected month and year (handles Feb/leap years).
     */
    private void updateDays() {
        int selectedDay = dayCombo.getSelectedIndex() + 1;
        int month = monthCombo.getSelectedIndex() + 1;
        int year = Integer.parseInt((String) yearCombo.getSelectedItem());
        int maxDays = YearMonth.of(year, month).lengthOfMonth();

        dayCombo.removeAllItems();
        for (int i = 1; i <= maxDays; i++) {
            dayCombo.addItem(String.format("%02d", i));
        }
        // Restore previous selection if still valid
        if (selectedDay > maxDays) selectedDay = maxDays;
        dayCombo.setSelectedIndex(selectedDay - 1);
    }

    public static void main(String[] args) {
        new SignUpOne();
    }

}
