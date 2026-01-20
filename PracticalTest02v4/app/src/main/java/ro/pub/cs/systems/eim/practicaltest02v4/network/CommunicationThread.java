/// Clasa Trhreadul de comunicare care gestionează comunicarea dintre Threadul pentru server și Threadul pentru client

package ro.pub.cs.systems.eim.practicaltest02v4.network;

import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import ro.pub.cs.systems.eim.practicaltest02v4.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v4.general.Utilities;

public class CommunicationThread extends Thread{

    private final ServerThread serverThread; /// Threadul pentru server
    private final Socket socket; /// Socketul

    /// Constructorul
    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    /// Funcția folosită atunci când Threadul pentru comunicație rulează
    @Override
    public void run() {
        try {

            BufferedReader reader = Utilities.getReader(socket); /// Pentru citirea pe canalul de comunicare
            PrintWriter writer = Utilities.getWriter(socket); /// Pentru scrierea pe canalul de comunicare

            String word = reader.readLine(); /// Citim cuvântul de de utilizator pe canal

            HttpClient client = new DefaultHttpClient(); /// Se crează un obiect de tipul HttpClient
            HttpGet get = new HttpGet(Constants.WEB_SERVICE_ADDRESS + word); /// Se crează un obiect de tipul HttpGet, care are ca uri adresa site-ului si cuvântul dat

            Log.d(Constants.TAG, "Cuvant primit de la client: " + word);
            Log.d(Constants.TAG, "Trimitere request catre web service: "
                    + Constants.WEB_SERVICE_ADDRESS + word);

            String page = EntityUtils.toString(client.execute(get).getEntity());

            Log.d(Constants.TAG, "Raspuns primit de la web service:");
            Log.d(Constants.TAG, page);

            String definition=null;

            if (page.trim().startsWith("[")) {
                JSONArray array = new JSONArray(page);

                JSONObject obj = array.getJSONObject(0);
                JSONArray meanings = obj.getJSONArray("meanings");
                JSONObject meaning0 = meanings.getJSONObject(0);
                JSONArray definitions = meaning0.getJSONArray("definitions");
                JSONObject def0 = definitions.getJSONObject(0);

                definition = def0.getString("definition");
            } else {
                JSONObject errorObject = new JSONObject(page);
                definition = errorObject.getString("message");
            }

            Log.d(Constants.TAG, "Definitie extrasa: " + definition);

            Intent intent = new Intent(Constants.BROADCAST_ACTION); /// Se crează un intent
            intent.putExtra(Constants.BROADCAST_EXTRA, definition);  /// Putem în intent mesajul și definiția
            serverThread.getContext().sendBroadcast(intent); /// Se ia contextul Threadului server

            writer.println(definition); /// Se pune pe canalul de comunicare definiția
            writer.flush(); /// Se șterge ce se află pe canalul de comunicație

            socket.close(); /// Se închide socketul
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage());
        }
    }
}
