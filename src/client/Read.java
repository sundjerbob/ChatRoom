package client;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

public class Read implements Runnable {


    private final BufferedReader reader;

    Read(BufferedReader in) {
        reader = new BufferedReader(in);
    }
    @Override
    public void run() {
        String message;

        while(true) {
            try {

                message = reader.readLine();
                if(message == null)
                    return;
                System.out.println(message);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
