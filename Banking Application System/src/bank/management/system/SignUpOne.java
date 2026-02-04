package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import com.toedter.calendar.JDateChooser;

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

    // JCalender
    JDateChooser dateChooser = new JDateChooser();

    SignUpOne() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Random ran = new Random();

        // when we dont setLayout to null it takes by default
        // border layout so JLabel doesnt work with it properly.
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Form No - Heading.
        // to generate the long random numbers in Java.
//        long random = Math.abs((ran.nextLong() % 9000L) + 1000L);
        long random = ThreadLocalRandom.current().nextLong(1000L, 10000L);
//        System.out.println(random);

        JLabel formNo = new JLabel("Application Form No. " + random);
        formNo.setFont(new Font("Ralway", Font.BOLD, 38));
        formNo.setBounds(140, 20, 600, 40);
        add(formNo);

        // Personal Details
        JLabel personalDetails = new JLabel("Page 1: Personal Details");
        personalDetails.setFont(new Font("Raleway", Font.BOLD, 20));
        personalDetails.setBounds(290, 80, 400, 30);
        add(personalDetails);


        // Labels
        // Name
        JLabel name = new JLabel("Name: ");
        name.setFont(new Font("Raleway", Font.BOLD, 18));
        name.setBounds(100, 140, 200, 30);
        add(name);
        // Name Text Field
        nameTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        nameTxtField.setBounds(300, 140, 400, 30);
        add(nameTxtField);

        // Father Name
        JLabel fname = new JLabel("Father Name: ");
        fname.setFont(new Font("Raleway", Font.BOLD, 18));
        fname.setBounds(100, 190, 200, 30);
        add(fname);
        // Father's Name Text Field
        fatherTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        fatherTxtField.setBounds(300, 190, 400, 30);
        add(fatherTxtField);

        // DOB
        JLabel dateOfBirth = new JLabel("Date of Birth: ");
        dateOfBirth.setFont(new Font("Raleway", Font.BOLD, 18));
        dateOfBirth.setBounds(100, 240, 200, 30);
        add(dateOfBirth);
        // DOB Field
        dateChooser.setBounds(300, 240, 200, 30);
        dateChooser.setForeground(new Color(105, 105, 105));
        add(dateChooser);


        // Gender
        JLabel gender = new JLabel("Gender: ");
        gender.setFont(new Font("Raleway", Font.BOLD, 18));
        gender.setBounds(100, 290, 200, 30);
        add(gender);
        // Male Radio Button
        maleRadioButton.setFont(new Font("Raleway", Font.BOLD, 14));
        maleRadioButton.setBounds(300, 290, 100, 30);
        maleRadioButton.setBackground(Color.WHITE);
        add(maleRadioButton);
        // Female Radio Button
        femaleRadioButton.setFont(new Font("Raleway", Font.BOLD,14));
        femaleRadioButton.setBounds(400, 290, 100, 30);
        femaleRadioButton.setBackground(Color.WHITE);
        add(femaleRadioButton);
        // Other Radio Button
        otherRadioButton.setFont(new Font("Raleway", Font.BOLD,14));
        otherRadioButton.setBounds(510, 290, 100, 30);
        otherRadioButton.setBackground(Color.WHITE);
        add(otherRadioButton);


        // E-Mail
        JLabel emailAdd = new JLabel("E-Mail: ");
        emailAdd.setFont(new Font("Raleway", Font.BOLD, 18));
        emailAdd.setBounds(100, 340, 200, 30);
        add(emailAdd);
        // E-Male Text Field
        emailAddressTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        emailAddressTxtField.setBounds(300, 340, 400, 30);
        add(emailAddressTxtField);

        // Marital Status
        JLabel maritalStatus = new JLabel("Marital Status: ");
        maritalStatus.setFont(new Font("Raleway", Font.BOLD, 18));
        maritalStatus.setBounds(100, 390, 200, 30);
        add(maritalStatus);
        // Married Radio Button
        marriedRadioButton.setFont(new Font("Raleway", Font.BOLD, 14));
        marriedRadioButton.setBounds(300, 390, 100, 30);
        marriedRadioButton.setBackground(Color.WHITE);
        add(marriedRadioButton);
        // Unmarried Radio Button
        unmarriedRadioButton.setFont(new Font("Raleway", Font.BOLD,14));
        unmarriedRadioButton.setBounds(400, 390, 100, 30);
        unmarriedRadioButton.setBackground(Color.WHITE);
        add(unmarriedRadioButton);

        // Address
        JLabel address = new JLabel("Address: ");
        address.setFont(new Font("Raleway", Font.BOLD, 18));
        address.setBounds(100, 440, 200, 30);
        add(address);
        // Address Text Field
        addressTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        addressTxtField.setBounds(300, 440, 400, 30);
        add(addressTxtField);


        // City
        JLabel city = new JLabel("City: ");
        city.setFont(new Font("Raleway", Font.BOLD, 18));
        city.setBounds(100, 490, 200, 30);
        add(city);
        // City Text Field
        cityTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        cityTxtField.setBounds(300, 490, 400, 30);
        add(cityTxtField);

        // State
        JLabel state = new JLabel("State: ");
        state.setFont(new Font("Raleway", Font.BOLD, 18));
        state.setBounds(100, 540, 200, 30);
        add(state);
        // State Text Field
        stateTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        stateTxtField.setBounds(300, 540, 400, 30);
        add(stateTxtField);

        // Pin Code
        JLabel pinCode = new JLabel("Pin Code: ");
        pinCode.setFont(new Font("Raleway", Font.BOLD, 18));
        pinCode.setBounds(100, 590, 200, 30);
        add(pinCode);
        // Pin Code Text Field
        pinCodeTxtField.setFont(new Font("Raleway", Font.BOLD, 14));
        pinCodeTxtField.setBounds(300, 590, 400, 30);
        add(pinCodeTxtField);


        setSize(850, 800);
        setLocation(350, 10);
        setVisible(true);
    }
    static void main(String[] args) {
        new SignUpOne();
    }
}
