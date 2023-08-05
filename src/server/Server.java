package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    public static final int PORT = 9001;
    private final ServerSocket serverSocket;

    static volatile ArrayList<ServerThread> clients;

    static volatile ArrayList<String> chat;

    static final List<String> banedWords = List.of("Hitler", "niglet", "kosovo");
    private static final  Object CLIENT_LOCK = new Object();
    private static final  Object CHAT_LOCK = new Object();

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clients = new ArrayList<>();

        chat = new ArrayList<>();

    }


    public static void main(String[] args) {
        try {
            (new Server()).run();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException {


        while(true)
        {
            (new Thread(new ServerThread(serverSocket.accept()))).start();

        }
    }


    static void addClient(ServerThread serverThread) {
        synchronized (CLIENT_LOCK) {
            for (ServerThread curr : clients) {
                curr.out.println(serverThread.clientName + " has joined!");
            }
            clients.add(serverThread);
        }

        serverThread.out.println("Welcome " + serverThread.clientName + "!");

        synchronized (CLIENT_LOCK) {
            for (String message : chat){
                serverThread.out.println(message);
            }
        }
    }

    static void addMessage(String message){

        synchronized (CHAT_LOCK) {

            if(chat.size() >= 100)
            {
                for(int i = 0, n = chat.size() - 100; i <= n; i++)
                    chat.remove(i);

            }
            message = inspectMessage(message);
            chat.add(message);

        }

        synchronized (CLIENT_LOCK) {

            for(ServerThread curr : clients)
            {
                curr.out.println(message);
            }

        }

    }


    private static String inspectMessage(String message) {

        for(String s : banedWords) {
            if (message.contains(s)){

                message = message.replace(s ,s.charAt(0) + "*".repeat(s.length() - 2) + s.charAt(s.length() - 1) );
               // System.out.println(message);

            }
        }
        return message;
    }


}
