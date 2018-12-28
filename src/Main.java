import Client.Client;
import FilesManager.FileUtils;
import Server.Server;

import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        startServer();
        createClient();
    }

    private static void startServer(){
        // Create the server and execute it on a Thread
        // To let the program continue
        Server socketServer = new Server();
        Thread t = new Thread(socketServer);
        t.start();
    }

    private static void createClient(){
        Client client = new Client ("127.0.0.1",9999);
        try {
            // Trying to establish connection to the server
            client.connect();
            // If successful, read response from server (Welcome message)
            client.readResponse();
            // Then ask to the user to choose a file
            client.sendResponse();
            // Get all bytes which the server sending (one shoot)
            String cryptedMessage = client.readBytesData();
            // Get the object with the code to decrypt the text (write with bytes).
            client.readObjectCode(cryptedMessage);
        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }
    }
}
