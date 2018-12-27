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
        System.out.println("Attempting to connect to "+hostname+":"+port);
        socketClient = new Socket(hostname,port);
        System.out.println("Connection Established");

    }

    public void readResponse() throws IOException{
        ObjectInputStream dis = new ObjectInputStream(socketClient.getInputStream());

        System.out.println("Response from server:");

        System.out.println(dis.readUTF());
        sendResponse();
    }

    public String readBytesData() throws IOException {
        ObjectInputStream dis = new ObjectInputStream(socketClient.getInputStream());
        FileOutputStream fos = new FileOutputStream("compressed-message-downloaded.txt");
        String s = "";


        byte[] buffer = new byte[2048];
//        Peut etre utiliser ça pour recuper toutes les données, on les ajoutes dans un byte[]
//        Et a la fin on le rezise + decrypt
//        int filesize = 15123; // Send file size in separate msg
//        int read = 0;
//        int totalRead = 0;
//        int remaining = filesize;
//        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
//            totalRead += read;
//            remaining -= read;
//            System.out.println("read " + totalRead + " bytes.");
//            fos.write(buffer, 0, read);
//        }

        int read = dis.read(buffer, 0, buffer.length);
        byte[] byteMessage = BinaryUtils.subbytes(buffer, 0, read);
        fos.write(buffer, 0, read);
        s = BinaryUtils.bytesToBinaryString(byteMessage);
        return s;
    }

    public void readObjectCode(String s) throws IOException {
        ObjectInputStream dis = new ObjectInputStream(socketClient.getInputStream());

        Map<String, Character> code = null;
        try {
            code = (Map<String, Character>) dis.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(decryptMessage(code, s));
    }

    private void sendResponse() throws IOException{
        PrintWriter pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())),true);
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter a number: ");
        int n = reader.nextInt();
        pred.println(n);
        System.out.println("the message was sent to the server");
    }

    private StringBuilder decryptMessage(Map<String, Character> code, String message){
        StringBuilder decryptedMessage = new StringBuilder();
        ArrayList<Character> listChar = new ArrayList<>(code.values());
        ArrayList<String> listCode = new ArrayList<>(code.keySet());
        String bytes = "";

        for (int i = 0; i < message.length(); i++) {
            bytes = bytes + message.charAt(i);
            int foundLetter = listCode.indexOf(bytes);
            if(foundLetter != -1){
                bytes = "";
                decryptedMessage.append(listChar.get(foundLetter));
            }
            if(bytes.length() == 8)
                bytes = "";
        }
        return decryptedMessage;
    }
}
