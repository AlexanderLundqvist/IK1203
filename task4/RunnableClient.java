import java.net.*;
import java.io.*;
import java.util.*;
import tcpclient.TCPClient;
import java.nio.charset.StandardCharsets;

public class RunnableClient implements Runnable {
  public Socket connectionSocket;

  public RunnableClient (Socket socket) {
    this.connectionSocket = socket;
  }

  public void run() {
    try {
      // Connected
      System.out.println("Client connected to the server...");
      System.out.println(connectionSocket.toString());
      System.out.println();

      DataOutputStream fromServer = new DataOutputStream(connectionSocket.getOutputStream());
      BufferedReader toServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      String[] headersRaw = toServer.readLine().split("[\\?= &]");
      System.out.println("Getting input from socket...\n");

      // Initial query parameters
      String hostname = null;
      int port = -1;
      byte[] toServerBytes = null;
      boolean shutdown = false;
      Integer timeout = null;
      Integer limit = null;

      // Test headersRaw
      System.out.println("\n*********************** Headers from request ************************");
      for (int i = 0; i < headersRaw.length; i++) {
          System.out.println(headersRaw[i]);
      }
      System.out.println("\n*********************************************************************\n");

      System.out.println("Parsing HTTP request...");

      try {
        // Bad loop, reimplement with arrayList for task 4?
        for (int i = 2; i < headersRaw.length; i++) {
            if (headersRaw[i].equals("hostname")) {
                hostname = headersRaw[i+1];
            }
            if (headersRaw[i].equals("port")) {
                port = Integer.parseInt(headersRaw[i+1]);
            }
            if (headersRaw[i].equals("string")) {
                toServerBytes = (headersRaw[i+1]+"\n").getBytes(StandardCharsets.UTF_8);
            }
            if (headersRaw[i].equals("shutdown")) {
                shutdown = Boolean.parseBoolean(headersRaw[i+1]);
            }
            if (headersRaw[i].equals("timeout")) {
                timeout = Integer.valueOf(headersRaw[i+1]);
            }
            if (headersRaw[i].equals("limit")) {
                limit = Integer.valueOf(headersRaw[i+1]);
            }
        }
        //Needs more catches
      } catch (Exception ex){
          System.out.println("Error in parsing URL: " + ex);
          connectionSocket.close();
      }

      // Test headers parsed
      System.out.println("\n********************** Testing parsed headers ***********************");
      System.out.println("Hostname: " + hostname);
      System.out.println("Port: " + port);
      System.out.println("String: " + Arrays.toString(toServerBytes));
      System.out.println("Shutdown: " + shutdown);
      System.out.println("Timeout: " + timeout);
      System.out.println("Limit: " + limit);
      System.out.println("\n*********************************************************************\n");

        // Try opening the TCP client
      if (headersRaw[0].equals("GET") && headersRaw[1].equals("/ask") && headersRaw[headersRaw.length-1].equals("HTTP/1.1") && port >=0 && port <= 65535 && hostname != null) {
        System.out.println("Opening TCP client...");
        try {
          TCPClient tcpClient = new TCPClient(shutdown, timeout, limit);
          byte[] serverResponse = tcpClient.askServer(hostname, port, toServerBytes);
          String serverResponseString = new String(serverResponse);

          // Test output
          System.out.println("\n****************************** Output *******************************");
          System.out.println(serverResponseString);
          System.out.println("*********************************************************************\n");

          fromServer.writeBytes("HTTP/1.1 200 OK\r\n\r\n");
          //fromServer.write(finalOutput.getBytes(StandardCharsets.UTF_8));
          fromServer.write(serverResponse);
          connectionSocket.close();

        } catch (IOException ex) {
          fromServer.writeBytes("HTTP/1.1 404 Not Found\r\n\r\n");
          System.out.println("\nError in TCP connection: " + ex);
          connectionSocket.close();
        }
      }

      else {
        fromServer.writeBytes("HTTP/1.1 400 Bad Request\r\n\r\n");
        System.out.println("\nBad Request!");
        connectionSocket.close();
      }

      System.out.println("\nClient has been served...\n\n");

    } catch (Exception ex) {
      System.out.println("Server error: " + ex);
      System.exit(1);
    }
  }
}
