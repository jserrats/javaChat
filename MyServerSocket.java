/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3_sad;

import java.net.*;
import java.io.*;

/**
 *
 * @author jaume
 */
class MyServerSocket {

    public ServerSocket server;

    public MyServerSocket(int port) {
        try {
            server = new ServerSocket(port);
        } catch (java.lang.NumberFormatException | IOException e) {
            System.err.println("Port incorrecte");
        }
    }

    public MySocket accept() {
        try {
            Socket s = server.accept();
            return new MySocket(s);
        } catch (IOException ex) {
            return null;
        }
    }
}
