/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p3_sad;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jaume
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("===============================");
            System.err.println("Inicia el servidor així:");
            System.err.println("  > java Server <port>");
            System.err.println("===============================");
            System.exit(1);
        }
        
        MyServerSocket mss = new MyServerSocket(Integer.parseInt(args[0]));
        final ConcurrentHashMap<String, MySocket> users = new ConcurrentHashMap();
        
        System.out.println("Servidor iniciat - port: " + args[0]);
        System.out.println("Esperant a que els clients es conectin..");
        
        while (true) {
            final MySocket ms = mss.accept();
            new Thread_usuari(ms, users).start();
        }
    }
}

class Thread_usuari extends Thread {
    
    MySocket msocket;
    ConcurrentHashMap<String, MySocket> users;
    String nick;
    String missatge;
    
    public Thread_usuari(MySocket ms, ConcurrentHashMap usr) {
        this.msocket = ms;
        users = usr;
    }
    
    public void run() {
        // Protocol de missatgeria instantànea inventat:
        //  El servidor envia un una string on el primer char és un byte,
        //  que indica que ha de fer el client amb la resta del missatge.
        //      0x01 es una peticio de nick
        //      0x02 es un missatge de reconeixement de nick
        //      0x03 vol dir que el string que el segueix és un missatge que cal imprimir
        //      0x04 nou usuari s'ha unit
        //      0x05 usuari ha marxat, cal esborrarlo
        System.out.println("Un nou usuari s'ha conectat");

        //Solicitud de nick
        msocket.write_char('\01');
        
        nick = msocket.readLine();
        
        if (nick == null) {
            System.out.println("El usuari s'ha desconectat sense posar el nick");
            return;
        }
        
        while (users.putIfAbsent(nick, msocket) != null) {
            msocket.write_char('\01');
            nick = msocket.readLine();
        }
        msocket.write_char('\02');
        msocket.println('\03' + "Benvingut al xat, " + nick);

        //TODO : enviar al usuari que s'acaba de conectar la informacio de tots els usuaris que ja hi estan conectats
        for (Map.Entry<String, MySocket> usuaris_map : users.entrySet()) {
            String nick = usuaris_map.getKey();
            msocket.println('\04' + nick);
        }
        
        System.out.println(nick + " s'ha unit al xat");
        iterar_i_imprimir('\04' + nick);
        
        while ((missatge = msocket.readLine()) != null) {
            iterar_i_imprimir('\03' + nick + ": " + missatge);
        }
        
        System.out.println(nick + " ha deixat el xat");
        
        iterar_i_imprimir('\05' + nick);
        
        users.remove(nick, msocket);
        msocket.close();
    }
    
    public void iterar_i_imprimir(String to_print) {
        for (Map.Entry<String, MySocket> usuaris_map : users.entrySet()) {
            MySocket socket_usuari = usuaris_map.getValue();
            if (msocket != socket_usuari) {
                socket_usuari.println(to_print);
            }
        }
    }
}
