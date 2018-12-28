package Client;
import FilesManager.BinaryUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


public class Client {

    private String hostname;
    private int port;
    Socket socketClient;

    public Client(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() throws UnknownHostException, IOException{
        // Connect to the server
        System.out.println("Client :\nAttempting to connect to "+hostname+":"+port);
        socketClient = new Socket(hostname,port);
        System.out.println("Connection Established");

    }

    public void readResponse() throws IOException{
        // The first response of the server will be UTF (String)
        ObjectInputStream dis = new ObjectInputStream(socketClient.getInputStream());
        System.out.println("Response from server:");
        System.out.println(dis.readUTF());
    }

    public String readBytesData() throws IOException {
        // Second response, we will get the compressed files
        ObjectInputStream dis = new ObjectInputStream(socketClient.getInputStream());
        // Here we generate a new file with the data we receive from the server
        FileOutputStream fos = new FileOutputStream("compressed-message-downloaded.txt");
        byte[] buffer = new byte[2048];
        // dis.read() read data from the server and write them inside the buffer array
        int read = dis.read(buffer, 0, buffer.length);
        // We troncate the buffer array to delete excess of bytes
        byte[] byteMessage = BinaryUtils.subbytes(buffer, 0, read);
        // Write inside the new file
        fos.write(buffer, 0, read);
        // Convert to String the array of bytes
        return BinaryUtils.bytesToBinaryString(byteMessage);
    }

    public void readObjectCode(String s) throws IOException {
        // Finaly we will get the Map Array with the code to decrypt to bytes
        ObjectInputStream dis = new ObjectInputStream(socketClient.getInputStream());
        Map<String, Character> code = null;
        try {
            code = (Map<String, Character>) dis.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Show the message in the console
        System.out.println(decryptMessage(code, s));
    }

    public void sendResponse() throws IOException{
        // Send to the server the chosen file
        PrintWriter pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())),true);
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the number of the file you want to download: ");
        // Ask user to pick a file
        int n = reader.nextInt();
        // Send to the server the response
        pred.println(n);
        System.out.println("File asked to the server. Waiting for response..");
    }

    private StringBuilder decryptMessage(Map<String, Character> code, String message){
        StringBuilder decryptedMessage = new StringBuilder();
        ArrayList<Character> listChar = new ArrayList<>(code.values());
        ArrayList<String> listCode = new ArrayList<>(code.keySet());
        String bytes = "";
        // Here we will get the binary message (full of 1 and 0)
        // And we will get every 8 bits and test if it's feet with a code
        for (int i = 0; i < message.length(); i = i+8) {
            bytes = bytes + message.substring(i, i+8);
            int foundLetter = listCode.indexOf(bytes);
            if(foundLetter != -1){
                bytes = "";
                decryptedMessage.append(listChar.get(foundLetter));
            }
        }
        return decryptedMessage;
    }
}
