package com.fpt.submission.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketUtils {

    public void sendTCPMessage(String message, String serverHost, int serverPort) throws InterruptedException, IOException {
        Socket clientSocket = null;
        BufferedWriter bw = null;
        OutputStream os = null;
        OutputStreamWriter osw = null;

        try {
            // make a connection with server
            clientSocket = new Socket(serverHost, serverPort);

            os = clientSocket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);

            bw.write(message);
            bw.flush();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (osw != null) {
                    osw.close();
                }
                if (os != null) {
                    os.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
