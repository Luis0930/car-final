package com.udem.car_final;

import android.util.Log;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    private String serverMessage;
    public String SERVERIP;
    public static final int SERVERPORT = 8090;
    private OnMessageReceived mMessageListener = null;
    PrintWriter out;
    BufferedReader in;
    private Socket socket;
    public boolean con;

    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public String stopClient() throws Throwable {
        if (con == true) {
            socket.close();
            if (socket.isClosed()) {
                con = false;
                return "Estado: *Desconectado*";
            }
        }
        return null;
    }

    public String conection() {
        if (con == true) {
            return "Estado: ¡Conectado!";
        }
        return "Estado: Error en la conexion";
    }


    public void IP(String ip) {
        SERVERIP = ip;
        Log.e("TCP Client", SERVERIP);
    }

    public void run() {
        try {
            Log.e("TCP Client", "C: Connecting...");
            // Creamos un nuevo Socket para la conexión con el carro (Server)
            socket = new Socket(SERVERIP, SERVERPORT);
            try {
                // Enviamos el mensaje al servidor
                out = new PrintWriter(new BufferedWriter(new
                        OutputStreamWriter(socket.getOutputStream())), true);
                Log.e("TCP Client", "C: Done.");
                in = new BufferedReader(new
                        InputStreamReader(socket.getInputStream()));
                if (socket.isConnected() == true) {
                    con = true;
                }
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                Log.e("TCP", "S: Close");
            }
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }

    // Clase en la tarea asincrónica doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
