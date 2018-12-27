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


        /*FileUtils fu = new FileUtils("ressource/files");
        fu.getAllFiles();
        fu.readFile(fu.findFile("test.txt"));

        Tree tree = new Tree(fu.getResult());
        StringBuilder cryptedMessage = encryptMessage(tree.getCharWithCode(), fu.getMessage());
        fu.writeBinaryFile(cryptedMessage.toString(),"compressed-message.txt");
        byte[] b = fu.readBinaryFile("compressed-message.txt");
        String s = BinaryUtils.bytesToBinaryString(b);
        StringBuilder decryptedMessage = decryptMessageWithoutSpace(tree.getCharWithCode(), s);

        System.out.println("Crypted : " + cryptedMessage.toString());
        System.out.println("Decrypted : " + decryptedMessage.toString());*/
    }

    private static void startServer(){
        Server socketServer = new Server();
        Thread t = new Thread(socketServer);
        t.start();
    }

    private static void createClient(){
        Client client = new Client ("127.0.0.1",9999);
        try {
            //trying to establish connection to the server
            client.connect();
            //if successful, read response from server
            client.readResponse();
            String cryptedMessage = client.readBytesData();
            System.out.println(cryptedMessage);
            client.readObjectCode(cryptedMessage);
        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }
    }

    /*private static StringBuilder encryptMessage(Map<String, Character> code, StringBuilder message){
        StringBuilder cryptedMessage = new StringBuilder();
        ArrayList<Character> listChar = new ArrayList<>(code.values());
        ArrayList<String> listCode = new ArrayList<>(code.keySet());
        for (int i = 0; i < message.length(); i++) {
            Character letter = message.charAt(i);
            int j = listChar.indexOf(letter);
            cryptedMessage.append(listCode.get(j)).append(' ');
        }
        return cryptedMessage;
    }

    private static StringBuilder decryptMessage(Map<String, Character> code, StringBuilder message){
        StringBuilder decryptedMessage = new StringBuilder();
        ArrayList<Character> listChar = new ArrayList<>(code.values());
        ArrayList<String> listCode = new ArrayList<>(code.keySet());
        String[] cryptedMessageList = message.toString().split(" ");
        for (int i = 0; i < cryptedMessageList.length; i++) {
            int j = listCode.indexOf(cryptedMessageList[i]);
            decryptedMessage.append(listChar.get(j));
        }
        return decryptedMessage;
    }

    private static StringBuilder decryptMessageWithoutSpace(Map<String, Character> code, String message){
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
        }
        return decryptedMessage;
    }*/
}
