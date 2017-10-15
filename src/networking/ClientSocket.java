/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author athena
 */
public class ClientSocket {
    
    private Socket client;
    private DataInputStream fromClient;
    private DataOutputStream toClient;
    
    public ClientSocket(Socket socket) throws IOException{
        client = socket;
        fromClient = new DataInputStream(socket.getInputStream());
        toClient = new DataOutputStream(socket.getOutputStream());
        
        client.setSoTimeout(50);
        
    }
    
    
    public String receive() throws IOException{
        return fromClient.readUTF();
    }
    
    public void send(String str) throws IOException{
        toClient.writeUTF(str);
    }
    
    public boolean isConnected(){
        return !client.isClosed();
    }
}
