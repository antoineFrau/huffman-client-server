package Server;

import FilesManager.BinaryUtils;
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

    public Server(int port, String path) {
        this.fu = new FileUtils(path);
        this.port = port;
    }

    public Server() {
        this.fu = new FileUtils("ressource/files");
        this.port = 9999;
    }

    public void start() throws IOException {
        System.out.println("Starting the socket server at port:" + port);
        serverSocket = new ServerSocket(port);

        //Listen for clients. Block till one connects

        System.out.println("Waiting for clients...");
        while (true) {
            System.out.println("Waiting for client request");
            //creating socket and waiting for client connection
            Socket client = serverSocket.accept();
            //A client has connected to this server. Send welcome message
            sendWelcomeMessage(client);
//          Use ObjectOutputStream and ObjectInputStream to send the dictionary
//            https://stackoverflow.com/questions/27736175/how-to-send-receive-objects-using-sockets-in-java
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            int file = Integer.parseInt(reader.readLine());
            Tree t = generateEverythingForFile(file);
            sendFileWithBytes(client);
            sendCodeToDecrypt(client, t);
        }
    }

    private void sendWelcomeMessage(Socket client) throws IOException {
        ObjectOutputStream dos = new ObjectOutputStream(client.getOutputStream());
        dos.writeUTF("Bienvenue ! Voici les fichiers disponibles : ");
        dos.writeUTF("\n");
        dos.writeUTF(generateListOfFiles().toString());
        dos.flush();
    }

    private void sendFileWithBytes(Socket client) throws IOException {
        ObjectOutputStream dos = new ObjectOutputStream(client.getOutputStream());
        FileInputStream fis = new FileInputStream(fu.getCurrPath()+"compressed-message.txt");
        byte[] buffer = new byte[fu.readBinaryFile("compressed-message.txt").length];
        int read;
        fis.read(buffer);
        dos.write(buffer);
//        while ((read=fis.read(buffer)) > 0) {
//            dos.write(buffer,0,read);
//        }
        dos.flush();
    }

    private void sendCodeToDecrypt(Socket client, Tree t) throws IOException {
        ObjectOutputStream dos = new ObjectOutputStream(client.getOutputStream());
        dos.writeObject(t.getCharWithCode());
        dos.flush();
    }


    private Tree generateEverythingForFile(int index){
        this.fu.readFile(this.listOfFiles[index]);
        Tree tree = new Tree(fu.getResult());
        StringBuilder cryptedMessage = encryptMessage(tree.getCharWithCode(), fu.getMessage());
        fu.writeBinaryFile(cryptedMessage.toString(),"compressed-message.txt");
        return tree;
    }

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
