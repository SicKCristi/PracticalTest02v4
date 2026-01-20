/// Clasa care gestionează implementarea Threadului pentru server

package ro.pub.cs.systems.eim.practicaltest02v4.network;

import android.content.Context;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{

    private final ServerSocket serverSocket; /// Socketul
    private final Context context; /// Contextul

    /// Constructorul
    public ServerThread(int port, Context context) throws Exception {
        this.serverSocket = new ServerSocket(port);
        this.context = context;
    }

    /// Getterul pentru context
    public Context getContext() {
        return context;
    }

    /// Getterul pentru socket
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /// Funcția folosită atunci când Threadul pentru server rulează
    @Override
    public void run() {
        try {
            while (!isInterrupted()){
                Socket socket = serverSocket.accept();
                new CommunicationThread(this, socket).start();
            }
        } catch (Exception ignored) {}
    }

    /// Funcția folositî atunci când Threadul pentru server este oprit
    public void stopThread() {
        interrupt();
        try {
            serverSocket.close();
        } catch (Exception ignored) {}
    }
}
