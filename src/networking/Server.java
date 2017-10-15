/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author athena
 */
public class Server {
    public final static String HOST = "127.0.0.1";
    public final static int PORT = 5000;
    
    public ServerSocket server;
    
    //client stuff
    private ArrayList<ClientSocket> connectedClients = new ArrayList<>();
    private Thread clientAcceptanceThread;
    
    
    public Server(){
        try{
            server = new ServerSocket(Server.PORT);
            
            //start accepting clients
            (clientAcceptanceThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try{
                            //accept new client
                            System.out.println("Listening for new clients...");
                            ClientSocket newClient = new ClientSocket(server.accept());
                            
                            //add the new client to the array list
                            connectedClients.add(newClient);
                            
                            newClient.send("Welcome to the server!");
                            
                            
                        }
                        catch(IOException ex){
                            ex.printStackTrace(System.err);
                        }
                    }
                }
            }, "Client acceptance thread")).start();
            
            //manage clients
            while(true){
                ArrayList<Integer> disconnectedClients = new ArrayList<>();
                
                //check for disconnected clients and send/receive message
                for(int i=0; i<connectedClients.size(); i++){
                    ClientSocket client = connectedClients.get(i);
                    
                    if(client.isConnected()){
                        
                        try{
                            String message = "Client " + i + ":" + client.receive();
                            
                            for(int j=0; j<connectedClients.size(); j++){
                                if(i != j){
                                    connectedClients.get(j).send(message);
                                }
                            }
                        }
                        catch(IOException ex){}  
                    }else{
                        disconnectedClients.add(i);
                    }
                }
                
                for(int i : disconnectedClients){
                    connectedClients.remove(i);
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace(System.err);
        }
    }
    
    public static void main(String[] args){
        new Server();
    }
}
