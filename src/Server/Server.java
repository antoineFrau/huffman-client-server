package Server;

import FilesManager.FileUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Tree.*;

public class Server implements Runnable{

    private ServerSocket serverSocket;
    private int port;
    private FileUtils fu;
    private File[] listOfFiles;

    public Server() {
        this.fu = new FileUtils("ressource/files");
        this.port = 9999;
    }

    private void start() throws IOException {
        System.out.println("Server : \nStarting the socket server at port:" + port);
        serverSocket = new ServerSocket(port);

        // The client will only be able to ask for one file
        // You will need to restart everything if you want to retry exchange

        System.out.println("Waiting for clients...");
        while (true) {
            // Creating socket and waiting for client connection
            Socket client = serverSocket.accept();
            // A client has connected to this server. Send welcome message
            sendWelcomeMessage(client);
            // Get the index of file choose by the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            int file = Integer.parseInt(reader.readLine());
            // Generate the huffman tree
            Tree t = generateEverythingForFile(file);
            // Send to the client the content of the file using bytes
            sendFileWithBytes(client);
            // Send him the dictionary who links binary code to a letter.
            sendCodeToDecrypt(client, t);
        }
    }

    private void sendWelcomeMessage(Socket client) throws IOException {
        // Using ObjectOutputStream for every type of content I send to the client
        // We can't change between different type of Output communication protocol
        ObjectOutputStream dos = new ObjectOutputStream(client.getOutputStream());
        dos.writeUTF("Bienvenue sur le serveur ! \nVoici les fichiers disponibles : \n" +
                generateListOfFiles().toString());
        dos.flush();
    }

    private void sendFileWithBytes(Socket client) throws IOException {
        // Using ObjectOutputStream for every type of content I send to the client
        // We can't change between different type of Output communication protocol
        ObjectOutputStream dos = new ObjectOutputStream(client.getOutputStream());
        dos.write(fu.readBinaryFile("compressed-message.txt"));
        dos.flush();
    }

    private void sendCodeToDecrypt(Socket client, Tree t) throws IOException {
        // Using ObjectOutputStream for every type of content I send to the client
        // We can't change between different type of Output communication protocol
        ObjectOutputStream dos = new ObjectOutputStream(client.getOutputStream());
        dos.writeObject(t.getCharWithCode());
        dos.flush();
    }

    private Tree generateEverythingForFile(int index){
        this.fu.readFile(this.listOfFiles[index]);
        Tree tree = new Tree(fu.getResult());
        StringBuilder cryptedMessage = encryptMessage(tree.getCharWithCode(), fu.getMessage());
        // Generate the file with the text compressed (Bytes)
        fu.writeBinaryFile(cryptedMessage.toString(),"compressed-message.txt");
        return tree;
    }

    /* Used to encrypt the text using the code<binaryString, Letter> */
    private StringBuilder encryptMessage(Map<String, Character> code, StringBuilder message){
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

    /* Find all files available on the server except the compressed one */
    private StringBuilder generateListOfFiles() {

        listOfFiles = fu.getAllFiles();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String name = listOfFiles[i].getName();
                Pattern pattern = Pattern.compile("compressed-message.txt");
                Matcher matcher = pattern.matcher(name);
                if(!matcher.matches())
                    sb.append(i+": "+name+"\n");
            }
        }
        return sb;
    }

    @Override
    public void run() {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
