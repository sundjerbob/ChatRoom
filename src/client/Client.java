package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public  static final int PORT = 9001;

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket("127.0.0.1", PORT);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);


            System.out.println("Enter your username:");

            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();


            out.println(name);
            String message;

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(new Thread(new Read(in)));

            while (true) {

               message = scanner.nextLine();
               out.println(message);

               if(message.equals("quit"))
               {
                   executorService.shutdown();
                   executorService.close();
                   socket.close();
                   return;
               }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                out.close();
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}