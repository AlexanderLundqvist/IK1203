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
        
        // Fixed size buffer for write operation from server
        byte[] serverReadBuffer = new byte[1024];
        
        // Dynamic byte array to store the actual answer from the server
        ByteArrayOutputStream serverResponseBuffer = new ByteArrayOutputStream();
        
        // Create the socket
        Socket clientSocket = new Socket(hostname, port);
        
        // Sending request to server
        clientSocket.getOutputStream().write(toServerBytes, 0, toServerBytes.length);
        
        // Reading response from the server
        clientSocket.getInputStream().read(fromServerBuffer);
        
        // Extract server response bytes from the buffer
        byte[] serverResponse = serverResponseBuffer.toByteArray();
                
        // End of process
        clientSocket.close();
        return serverResponse;
    }
}
