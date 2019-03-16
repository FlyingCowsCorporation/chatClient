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

  // Locally instantiate itself, so it can be passed on to external services.
  private static Program program = new Program();

  // Instantiate the necessary elements that will be used in the interface,
  // to send and receive chat messages.
  private JFrame chatFrame = new JFrame();
  private JPanel chatRoomPanel = new JPanel();
  private JTextArea chatRoom = new JTextArea(20, 20);
  private JScrollPane chatRoomScroll = new JScrollPane(chatRoom);
  private JPanel usersPanel = new JPanel();
  private JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
  private JScrollPane userScroll = new JScrollPane(buttonContainer);
  private JPanel msgPanel = new JPanel();
  private JTextArea msgField = new JTextArea(5, 22);
  private JButton sendButton = new JButton();

  // Set up user list array that will contain the list of all users connected to the chat room
  // server.
  List<User> users = new ArrayList<>();
  private User localUser;

  // Instantiate message handler, used to send and receive messages from the API.
  private MessageHandler messageHandler = new MessageHandler();
  private boolean usedEnter = false;

  /**
   * Presents the user with a configuration screen where he can set up his chat server parameters.
   */
  public static void main(String[] args) {
    Login login = new Login(program);
    login.createLogInFrame();
  }

  /**
   * Builds the initial window that will hold the various frames responsible for providing the user
   * with elements that enable him to interact with the chat server.
   */
  private void RenderChatRoom() {
    // Display root window frame.
    chatFrame.setTitle("Welcome to Chat app " + localUser.name);
    chatFrame.setSize(500, 600);
    chatFrame.setLocation(200, 300);
    chatFrame.setVisible(true);
    chatFrame.setBackground(Color.GRAY);
    chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    chatFrame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

    // Display text area that displays the conversation.
    chatRoomPanel.setBorder(new TitledBorder(new EtchedBorder(), "Chat room"));
    chatRoom.setEditable(false);
    chatRoom.setLineWrap(true);
    chatRoom.setWrapStyleWord(true);

    // Configure scroll for the chat conversation field.
    chatRoomScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    chatRoomScroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    chatRoomPanel.add(chatRoomScroll);
  }

  /**
   * Displays interface elements that are responsible for providing information on the currently
   * connected users.
   */
  private void RenderUserPanel() {
    // User panel
    usersPanel.setBorder(new TitledBorder(new EtchedBorder(), "Online users"));
    usersPanel.setPreferredSize(new Dimension(150, 355));

    // Scroll for the buttons
    userScroll.setVerticalScrollBarPolicy(userScroll.VERTICAL_SCROLLBAR_ALWAYS);
    userScroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    usersPanel.add(userScroll);
  }

  void CreateWindow(String userName) {
    // Start listening to messages.
    new Thread(new CheckMsg(program)).start();

    // Add current user to user list and set him as the local user.
    localUser = new User(userName);
    users.add(localUser);

    // Display the chat room where the user can view the message being sent to the server.
    RenderChatRoom();

    // Display the list of users that are currently connected to the chat server.
    RenderUserPanel();

    // Add users dynamically
    buttonContainer.setLayout(new GridLayout(users.size(), 1));
    for (User user : users) {
      JButton button = new JButton(user.name);
      button.setPreferredSize(new Dimension(100, 25));
      buttonContainer.add(button);
    }
    buttonContainer.setPreferredSize(new Dimension(110, (users.size() * 25)));
    userScroll.setPreferredSize(new Dimension(135, 320));

    // Msg area
    msgPanel.setBorder(new TitledBorder(new EtchedBorder(), "Message"));
    msgField.setLineWrap(true);
    msgField.setWrapStyleWord(true);
    msgPanel.setForeground(Color.BLUE);
    msgPanel.add(msgField);

    // Add KeyListener on Enter
    msgField.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent e) {
            super.keyPressed(e);
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              usedEnter = true;
              Post();
            }
          }
        });

    // Send button
    sendButton.setPreferredSize(new Dimension(145, 100));
    sendButton.setBackground(Color.ORANGE);
    sendButton.setForeground(Color.WHITE);
    sendButton.setBorderPainted(true);
    sendButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    sendButton.setText("Send");
    sendButton.addActionListener(
        e -> {
          usedEnter = false;
          Post();
        });

    // Add everything to the frame
    chatFrame.getContentPane().add(chatRoomPanel);
    chatFrame.getContentPane().add(usersPanel);
    chatFrame.getContentPane().add(msgPanel);
    chatFrame.getContentPane().add(sendButton);
  }

  private void Post() {
    if (!msgField.getText().equals("")) {
      String pMSG = msgField.getText();
      msgField.setText("");
      try {
        messageHandler.Send(localUser.name + " said: " + pMSG);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  void Receive() {
    System.out.println("Receiving messages");
    try {
      String rMSG = messageHandler.Receive();
      chatRoom.append(rMSG + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
