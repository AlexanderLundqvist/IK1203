import java.net.*;
import java.io.*;
import java.util.*;
import tcpclient.TCPClient;
import java.nio.charset.StandardCharsets;
import RunnableClient;

/******************************************************************************
* URLs to test
* http://localhost:8888/ask?hostname=time.nist.gov&limit=1200&port=13
* http://localhost:8888/ask?hostname=localhost&port=10002&string=The_time_is_Tue_Mar__25_00-02-48_2022
* http://localhost:8888/ask?hostname=java.lab.ssvl.kth.se&limit=200&port=19
* http://localhost:8888/ask?hostname=whois.internic.net&string=google.com&port=43
*
*
*
*******************************************************************************/

public class ConcHTTPAsk {

  /*
   * Usage: explain how to use the program, then exit with failure status
   */
  private static void usage() {
      System.err.println("Usage: ConcHTTPAsk <server port number>");
      System.exit(1);
  }

  /*
   * Main program. Parse arguments on command line and in case of error,
   * tell user how to operate the program.
   */
  public static void main( String[] args) throws IOException {
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
      // Keep the server running indefinitely and serve different client threads
      while (true) {
        Socket connectionSocket = serverSocket.accept();
        Runnable clientThread = new RunnableClient(connectionSocket);
        new Thread(clientThread).start();
      }

    } catch (Exception ex) {
      System.out.println("Server error: " + ex);
      System.exit(1);
    }
  }
}
