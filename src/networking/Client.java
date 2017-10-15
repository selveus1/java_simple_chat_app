/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author athena
 */
public class Client {
    
    private Socket client;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    
    private JFrame window;
    private JTextField input;
    private JTextArea chat;
    
    public Client(){
        //init chat window
        input = new JTextField();
        
        //when enter is pressed, send text in input field
        input.addKeyListener(new KeyListener(){
           
            public void keyTyped(KeyEvent e) {}
            
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String message = input.getText();
                    
                    //check message length
                    if(message.length() > 0 && message.length() < 250){
                        input.setText("");
                        chat.append("You: " + message + "\n");
                        try{
                            send(message);
                            
                        }
                        catch(IOException ex){
                            ex.printStackTrace(System.err);
                        }
                    }
                    
                }
            }
        
        
        });
        
        chat = new JTextArea(34,50);
        chat.setEditable(false);
        
        //set window traits
        window = new JFrame("Networking Chat");
        window.setVisible(true);
        window.setResizable(false);
        window.setSize(800, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //add input and chat to window
        window.add(chat, BorderLayout.PAGE_START);
        window.add(input, BorderLayout.PAGE_END);
                
        
        
        try{
            client = new Socket(Server.HOST, Server.PORT);
            
            if(client.isConnected()){
                fromServer = new DataInputStream(client.getInputStream());
                toServer = new DataOutputStream(client.getOutputStream());
                
                //receive a welcome message
                receive();
                
                while(!client.isClosed()){
                    receive();
                }

            }
            else{
                System.err.println("Could not connected to server.");
            }
            
        }
        catch(Exception ex){
            ex.printStackTrace(System.err);
        }
    }
    
    private void receive() throws IOException{
        
        String message = fromServer.readUTF();
        chat.append(message + "\n");
    }
    
    private void send(String str) throws IOException{
        toServer.writeUTF(str);
    } 
    
    public static void main(String[] args){
        new Client();
       
    }
}
