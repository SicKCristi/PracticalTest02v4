/// Clasa pentru genstionarea Threadului de la client

package ro.pub.cs.systems.eim.practicaltest02v4.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v4.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v4.general.Utilities;

public class ClientThread extends Thread {

    private final String address; /// Adresa
    private final int port; /// Portul
    private final String word; /// Cuvântul
    private final TextView definitionTextView; /// TextView-ul în care vom afișa în interfață informațiile

    private Socket socket; /// Socketul

    /// Constructorul pentru clasa ClientThread
    /// Primește toate atributele din clasă, mai puțin socketul
    public ClientThread(String address, int port, String word, TextView definitionTextView) {
        this.address = address;
        this.port = port;
        this.word = word;
        this.definitionTextView = definitionTextView;
    }

    /// Implementare funcției atunci când Threadul rulează
    @Override
    public void run() {
        try {
            socket = new Socket(address, port); /// Folosind adresa și portul, se crează un socket pentru atributul din clasă
            BufferedReader bufferedReader = Utilities.getReader(socket); /// Obiectul pentru citirea pe canalul de comunicare
            PrintWriter printWriter = Utilities.getWriter(socket); /// Obiectul pentru scrierea pe canalul de comunincare
            printWriter.println(word); /// Scrie cuvântul
            printWriter.flush(); /// Ștergem ce avem în stdin
            String definitionInformation;
            while ((definitionInformation = bufferedReader.readLine()) != null) { ///  Cât timp există valori de citit
                final String finalizedefinitionInformation = definitionInformation; /// Variabila primește valoarea din weatherinformation
                definitionTextView.post(() -> definitionTextView.setText(finalizedefinitionInformation)); /// Pune informația în TextView-ul afarent
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) { /// Dacă socjetul nu este închis, atunci se închide
                try {
                    socket.close(); /// Închiderea socketului
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }

}
