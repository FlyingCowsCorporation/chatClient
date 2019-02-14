import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

public class program {

    JTextField textfield = new JTextField();


    //create frame
    static GraphicsConfiguration gc;

    public static void main(String[] args){
        program program=new program();
        program.CreateWindow();
    }

    public void CreateWindow(){
        JFrame frame= new JFrame(gc);
        frame.setTitle("Chat app");
        frame.setSize(500,600);
        frame.setLocation(200, 300);
        frame.setVisible(true);
        frame.setBackground(Color.GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button1 = new JButton();
        button1.setBounds(355,425,100,50);
        button1.setBackground(Color.ORANGE);
        button1.setBorderPainted(true);
        button1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        button1.setText("Submit");
        frame.add(button1);
        button1.addActionListener(e -> {
            Post();
        });

        textfield.setBounds(25,425,300,50);
        textfield.setBackground(Color.white);
        textfield.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textfield.isVisible();
        textfield.setHorizontalAlignment(JTextField.LEFT);
        frame.add(textfield);
    }

    public void Post(){
        String pMSG = textfield.getText();
        System.out.print(pMSG);
    }

    public void Receive(){
        String rMSG = "Ja dat weet ik nog niet";

    }

}
