import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

public class HTTPAsk {

  /*
   * Usage: explain how to use the program, then exit with failure status
   */
  private static void usage() {
      System.err.println("Usage: HTTPAsk <server port number>");
      System.exit(1);
  }

  /*
   * Main program. Parse arguments on command line and in case of error,
   * tell user how to operate the program.
   */
  public static void main( String[] args) {
    int serverPort = 0;
    ServerSocket serverSocket = null;

    try {
      serverPort = Integer.parseInt(args[0]);
    } catch (Exception ex) {
      usage();
    }

    // Create the server socket with the specified port. Catch any socket errors
    try {
      serverSocket = new ServerSocket(serverPort);
    } catch(IOException ex) {
      System.err.println(ex);
      System.exit(1);
    }

    try {
      // Keep the server running indefinitely and serve one client at a time
      while (true) {

        // Connect
        Socket connectionSocket = serverSocket.accept();
        System.out.println("Client connected to the server");
        System.out.println(connectionSocket.getLocalAddress());
        System.out.println();
        System.out.println();

        System.out.println();


        // http://localhost:8888/ask?hostname=time.nist.gov&limit=1200&port=13

        //
        DataOutputStream fromServer = new DataOutputStream(connectionSocket.getOutputStream());
        BufferedReader toServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

        //
        String response = toServer.readLine();
        //URL url = new URL(response);
        System.out.println("Response is: " + response);

        // Deconstruct the url to apropriate headers
        // String hostName = url.getHost();
        // int port = url.getPort();
        // String protocol = url.getProtocol();
        // Integer limit = url.getLimit();
        // Integer timeout = url.getTimeout();
        // boolean shutdown = url.getShutdown();
        // byte[] toServerBytes = null;
        // System.out.println("Host:" + hostName);
        // System.out.println("Port:" + port);

      }
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
      System.exit(1);
    }
  }
}
