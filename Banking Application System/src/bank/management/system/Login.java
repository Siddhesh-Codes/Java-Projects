package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {

    JButton login, clear, signup;
    JLabel cardNo,pin;
    JTextField cardTextField;
    JPasswordField pinTextField;

    Login() {
        // used to set the title of the frame
        setTitle("ATM MACHINE");

        // used to prevent By Default layout.
        setLayout(null);

        // used to terminate the Program when JFrame Window terminates
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // used to set the size of the Frame
        setSize(800, 480);

        // by default the frame is hidden from the
        // user so we have to make it true so that use can see it.
        setVisible(true);

        // by default our frame opens at top left corner
        // so to set it in the middle  we uses below function
        setLocation(350,200);


        // We cannot directly place the image on the window.
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/logo.jpg"));
        // to set the size of the placed image on the Window.
        Image i2 = i1.getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT);
        // JLable is to place the actual image on the Window.
        ImageIcon i3 = new ImageIcon(i2);
        JLabel label = new JLabel(i3);
        label.setBounds(70, 10, 100, 100);
        add(label);

        // JLabel used to add any content on the window
        // Welcome Message
        JLabel text = new JLabel("Welcome to ATM", SwingConstants.CENTER);
        text.setBounds(200,40,400,40);
        text.setFont(new Font("Onward", Font.BOLD, 20));
        add(text);

        // Card No. text
        cardNo = new JLabel("Card No:");
        cardNo.setBounds(200,150,150,40);
        cardNo.setFont(new Font("Raleway", Font.BOLD, 20));
        add(cardNo);

        // Card Np. Text Fields
        cardTextField = new JTextField();
        cardTextField.setBounds(350,155,230,30);
        cardTextField.setFont(new Font("Arial", Font.BOLD, 14));
        add(cardTextField);

        //Enter Pin txt
        pin = new JLabel("Enter Pin:");
        pin.setBounds(200,200,150,40);
        pin.setFont(new Font("Raleway", Font.BOLD, 20));
        add(pin);

        // Text Fields
        pinTextField = new JPasswordField();
        pinTextField.setBounds(350,205,230,30);
        pinTextField.setFont(new Font("Arial", Font.BOLD, 14));
        add(pinTextField);



        // Buttons
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

    // what we have to do when we click on the particular button
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == login) {
        } else if (e.getSource() == clear) {
            cardTextField.setText("");
            pinTextField.setText("");
        } else if (e.getSource() == signup){
            SignUpOne signUp = new SignUpOne();

            signUp.setVisible(true);
            dispose();
        }
    }
    static void main(String[] args) {
//        System.out.println("Hello World");
        // to call the Constructor
//        JFrame frame =  new JFrame("ATM MACHINE");

        new Login();

//        Login log = new Login();
    }
}
