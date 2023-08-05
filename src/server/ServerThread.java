package server;

import server.Server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    final Socket socket;
    volatile String clientName;

    BufferedReader in;
    PrintWriter out;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {



        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String name = in.readLine();
            if(name != null && !name.equals(""))
                clientName = name;

            Server.addClient(this);

            String message;

            while(true) {

                message = in.readLine();
                //System.out.println(message);
                if(message.equals("quit"))
                {
                    in.close();
                    out.close();
                    socket.close();
                    return;
                }
                Server.addMessage(clientName +" : "+  message);
            }


        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}
