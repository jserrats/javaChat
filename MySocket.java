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
class MySocket {

    protected Socket s;
    protected BufferedReader in;
    protected PrintWriter out;

    public MySocket(String host, int port) {
        try {
            s = new Socket(host, port);
            init_streams();
        } catch (IOException e) {
        }

    }

    public MySocket(Socket sc) {
        this.s = sc;
        init_streams();
    }

    private void init_streams() {
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) {
        }
    }

    public String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public int read_int() {
        return Integer.parseInt(readLine());
    }

    public boolean read_boolean() {
        return Boolean.parseBoolean(readLine());
    }

    public char read_char() {
        return readLine().charAt(0);
    }

    public void println(String st) {
        out.println(st);
    }

    public void write_int(int i) {
        println(Integer.toString(i));
    }

    public void write_boolean(Boolean b) {
        println(Boolean.toString(b));
    }

    public void write_char(char c) {
        println(new Character(c).toString());
    }

    public void close() {
        try {
            in.close();
            out.close();
            s.close();
        } catch (IOException ex) {
        }
    }
}