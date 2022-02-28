package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    private final boolean shutdown;
    private final Integer timeout;
    private final Integer limit;

    /**
     * Constructor. Initializes an instance of TCPClient.
     */
    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }

    public byte[] askServer(String hostname, int port, byte[] toServerBytes) throws IOException {

        // Dynamic byte array to store the actual answer from the server
        ByteArrayOutputStream serverResponseBuffer = new ByteArrayOutputStream();

        try {
            // Create the socket
            Socket clientSocket = new Socket(hostname, port);

            // Set socket timout limit
            if (timeout != null) {clientSocket.setSoTimeout(timeout);}

            // Declare incoming and outgoing streams
            OutputStream toServer = clientSocket.getOutputStream();
            InputStream fromServer = clientSocket.getInputStream();

            // Sending request to server
            toServer.write(toServerBytes, 0, toServerBytes.length);

            // Close the outgoing stream after sending data to server if the shutdown flag is true
            if (shutdown) {clientSocket.shutdownOutput();}

            // Reading response from the server
            if (limit != null) {
                for (int i = 0; i < limit; i++) {
                    int byteRead = clientSocket.getInputStream().read();
                    if (byteRead == -1 || !clientSocket.isConnected()) {break;}
                    else                {serverResponseBuffer.write​(byteRead);}
                }
            }
            else {
                while (true) {
                    int byteRead = clientSocket.getInputStream().read();
                    if (byteRead == -1 || !clientSocket.isConnected()) {break;}
                    else                {serverResponseBuffer.write​(byteRead);}
                }
            }

            // End of process
            clientSocket.close();

        // Catch common errors whith the socket. Other errors are caught in TCPAsk.
        } catch (ConnectException | IllegalArgumentException | UnknownHostException ex) {
            System.out.println("Socket error: " + ex);
            System.exit(1);
        } catch (SocketTimeoutException ex) {
            System.out.println("Socket timeout!");
        }

        return serverResponseBuffer.toByteArray();
    }
}
