package clases;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpClient {
    public static void main(String[] args) {
        final int PORT = 50000;
        byte[] buffer = new byte[1024];
        
        try {
            
            InetAddress direccion = InetAddress.getByName("localhost");
            DatagramSocket socketUDP = new DatagramSocket();
            
            String mensaje = "Anapaula,123pwd,anapaula@gmail.com,";
            
            buffer = mensaje.getBytes();
            
            DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccion, PORT);
            
            socketUDP.send(pregunta);
            
            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
            
            socketUDP.receive(peticion);
            
            mensaje = new String(peticion.getData());
            
            System.out.println(mensaje);
            
            socketUDP.close();
            
        } catch (SocketException ex) {
            Logger.getLogger(clases.UdpClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(clases.UdpClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(clases.UdpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
