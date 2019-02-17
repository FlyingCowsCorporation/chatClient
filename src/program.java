import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.util.List;

public class Program {

    static Program program = new Program();

    //////////Frame///////////
    private JFrame chatFrame = new JFrame();
    private JPanel chatRoomPanel = new JPanel();
    private JTextArea chatRoom = new JTextArea(20,20);
    private JScrollPane chatRoomScroll = new JScrollPane(chatRoom);
    private JPanel usersPanel = new JPanel();
    private JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    private JScrollPane userScroll = new JScrollPane(buttonContainer);
    private JPanel msgPanel = new JPanel();
    private JTextArea msgField = new JTextArea(5,22);
    private JButton sendButton = new JButton();

    //////////User list///////////
    public List<User> users = new ArrayList<User>();
    private String localUser = "";

    //////////Message handler///////////
    private MessageHandler messageHandler = new MessageHandler();
    private boolean usedEnter = false;

    public static void main(String[] args){
        Login login = new Login(program);
        login.createLogInFrame();
    }

    public void CreateWindow(String userName){
        //Start listening to messages
        new Thread(new CheckMsg(program)).start();

        //Add user to user list and set local user
        users.add(new User(userName));
        int currentUserNumber = (users.size()) - 1;
        localUser = users.get(currentUserNumber).name;

        //Create chat frame
        chatFrame.setTitle("Welcome to Chat app "+localUser);
        chatFrame.setSize(500,600);
        chatFrame.setLocation(200, 300);
        chatFrame.setVisible(true);
        chatFrame.setBackground(Color.GRAY);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setLayout(new FlowLayout(FlowLayout.LEFT,20,20));

        //Text area that displays the conversation
        chatRoomPanel.setBorder(new TitledBorder(new EtchedBorder(), "Chat room"));
        chatRoom.setEditable(false);
        chatRoom.setLineWrap(true);
        chatRoom.setWrapStyleWord(true);
        //Scroll for the chat conversation field
        chatRoomScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatRoomScroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chatRoomPanel.add(chatRoomScroll);

        //User panel
        usersPanel.setBorder(new TitledBorder(new EtchedBorder(), "Online users"));
        usersPanel.setPreferredSize(new Dimension(150,355));
        //Scroll for the buttons
        userScroll.setVerticalScrollBarPolicy(userScroll.VERTICAL_SCROLLBAR_ALWAYS);
        userScroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        usersPanel.add(userScroll);

        //Add users dynamically
        buttonContainer.setLayout(new GridLayout(users.size(), 1));
        for (int i = 0; i < users.size(); i++) {
            JButton button = new JButton(users.get(i).name);
            button.setPreferredSize(new Dimension(100, 25));
            buttonContainer.add(button);
        }
        buttonContainer.setPreferredSize(new Dimension(110,(users.size()*25)));
        userScroll.setPreferredSize(new Dimension(135,320));

        //Msg area
        msgPanel.setBorder(new TitledBorder(new EtchedBorder(), "Message"));
        msgField.setLineWrap(true);
        msgField.setWrapStyleWord(true);
        msgPanel.setForeground(Color.BLUE);
        msgPanel.add(msgField);

        //Add KeyListener on Enter
        msgField.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        super.keyPressed(e);
                        if(e.getKeyCode()==KeyEvent.VK_ENTER){
                            usedEnter=true;
                            Post();
                        }
                    }
                });

        //Send button
        sendButton.setPreferredSize(new Dimension(145,100));
        sendButton.setBackground(Color.ORANGE);
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorderPainted(true);
        sendButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        sendButton.setText("Send");
        sendButton.addActionListener(e -> {
            usedEnter=false;
            Post();
        });

        //Add everything to the frame
        chatFrame.getContentPane().add(chatRoomPanel);
        chatFrame.getContentPane().add(usersPanel);
        chatFrame.getContentPane().add(msgPanel);
        chatFrame.getContentPane().add(sendButton);
    }

    public void Post(){
        if(msgField.getText().equals("")) {/*if text field is empty do nothing*/}
        else {
            String pMSG = msgField.getText();
            msgField.setText("");
            try {
                messageHandler.Send(localUser + " said: " + pMSG);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Receive(){
        System.out.println("Receiving messages");
        try {
            String rMSG = messageHandler.Receive();
            if(usedEnter){
                chatRoom.append(rMSG+"\n");}
            else if (!usedEnter){chatRoom.append(rMSG+"\n");}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
