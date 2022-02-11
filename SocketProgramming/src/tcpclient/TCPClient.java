package tcpclient;

import java.net.*;
import java.io.*;

public class TCPClient {
    
    /**
     * Constructor. Initializes an instance of the object TCPClient.
     */
    public TCPClient() {
    }
    
    /**
     * 
     * @param hostname the name of the server
     * @param port the port number of the server
     * @param toServerBytes the message to the server
     * @return
     * @throws IOException 
     */
    public byte[] askServer(String hostname, int port, byte[] toServerBytes) throws IOException {
        
        // Dynamic byte array to store the actual answer from the server
        ByteArrayOutputStream serverResponseBuffer = new ByteArrayOutputStream();
        
        try {
            // Create the socket
            Socket clientSocket = new Socket(hostname, port);

            // Sending request to server
            clientSocket.getOutputStream().write(toServerBytes, 0, toServerBytes.length);

            // Reading response from the server
            while(true) {
                int byteRead = clientSocket.getInputStream().read();
                if (byteRead == -1) {break;}
                else                {serverResponseBuffer.write​(byteRead);}
            
            }
                
            // End of process
            clientSocket.close();
            
        // Catch common errors whith the socket. Other errors are caught in TCPAsk.    
        } catch (ConnectException | IllegalArgumentException | UnknownHostException ex) {
            //System.err.println(ex);
            System.out.println("Socket error: " + ex);
            System.exit(1);
        }
        
        return serverResponseBuffer.toByteArray();
    }    
}
