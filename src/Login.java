import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login {

    private Program program;
    public Login(Program program) {
        this.program = program;
    }

    private JFrame logInFrame = new JFrame();
    private JPanel logInPanel = new JPanel();
    private JLabel loginNameLabel = new JLabel("User name:");
    private JLabel serverNameLabel = new JLabel("Server:");
    private JTextField userNameField = new JTextField();
    private JTextField serverField = new JTextField("http://geluk.io:15000/messages");
    private JButton logInButton = new JButton("Log in");

    public void createLogInFrame(){
        logInFrame.setTitle("Chat app");
        logInFrame.setSize(250,240);
        logInFrame.setLocation(200, 300);
        logInFrame.setVisible(true);
        logInFrame.setBackground(Color.GRAY);
        logInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logInPanel.setBorder(new TitledBorder(new EtchedBorder(), "Choose a user name and a server"));
        logInPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
        userNameField.setPreferredSize(new Dimension(170,25));
        userNameField.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        super.keyPressed(e);
                        if(e.getKeyCode()==KeyEvent.VK_ENTER){
                            String userName = userNameField.getText();
                            logIn(userName);
                        }
                    }
                });

        serverField.setPreferredSize(new Dimension(170,25));

        logInButton.setPreferredSize(new Dimension(170,25));
        logInButton.setBackground(Color.ORANGE);
        logInButton.setForeground(Color.WHITE);
        logInButton.setBorderPainted(true);
        logInButton.setVisible(true);
        logInButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        logInButton.addActionListener(e -> {
            String userName = userNameField.getText();
            logIn(userName);
        });

        logInPanel.add(loginNameLabel);
        logInPanel.add(userNameField);
        logInPanel.add(serverNameLabel);
        logInPanel.add(serverField);
        logInPanel.add(logInButton);
        logInFrame.getContentPane().add(logInPanel);
    }

    private void logIn(String userName){
        if(program.users.size() > 0){
            for(int i=0; i<program.users.size();i++) {
                if(userName.equals(program.users.get(i).name)) {
                    //userNameField.setT
                }
                else {
                    MessageHandler.GET_URL = serverField.getText();
                    MessageHandler.POST_URL = serverField.getText();
                    logInFrame.dispose();
                    program.CreateWindow(userName);
                }
            }
        }
        else {
            MessageHandler.GET_URL = serverField.getText();
            MessageHandler.POST_URL = serverField.getText();
            logInFrame.dispose();
            program.CreateWindow(userName);
        }
    }
}


