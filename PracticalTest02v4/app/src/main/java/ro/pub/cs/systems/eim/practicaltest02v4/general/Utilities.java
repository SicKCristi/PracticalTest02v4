package ro.pub.cs.systems.eim.practicaltest02v4.general;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Utilities {

    /// Funcția pentru citire a datelor de pe un Socket
    public static BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /// Funcția pentru afișare a datelor pe un Socket
    public static PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }

}
