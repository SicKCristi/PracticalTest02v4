package ro.pub.cs.systems.eim.practicaltest02v4.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02v4.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v4.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02v4.network.ServerThread;

public class PracticalTest02MainActivityv4 extends AppCompatActivity {

    private EditText serverPortEditText = null;
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText wordEditText = null;
    private TextView definitionTextView = null;

    private ServerThread serverThread = null;

    ///  Se definiște prima dată această variabilă înainte de implementarea funcționalității pentru onClick
    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();

    ///  Funcția care se folosește atunci când se apasă butonul de conectare
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString(); /// Se ia valoarea pentru portul serverului

            if (serverPort.isEmpty()) { /// Dacă acesta este gol, ieșim din funcție și socketul nu este creat
                Toast.makeText(getApplicationContext(),
                        "[MAIN ACTIVITY] Server port should be filled!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            ///  Se crează un Thread pentru server
            try {
                serverThread = new ServerThread(
                        Integer.parseInt(serverPort),
                        getBaseContext()
                );
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "[MAIN ACTIVITY] Cannot start server!",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            if (serverThread == null || serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG,
                        "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }

            serverThread.start();
        }
    }

    /// Se definește mai întâi variabila înainte de impelementarea în cazul apăsării butonului
    private final GetDefinitionButtonClickListener getDefinitionButtonClickListener=new GetDefinitionButtonClickListener();

    /// Implementarea efectivă a clasei care gestionează apăsarea butonului
    private class GetDefinitionButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString(); /// Luăm adresa dată de client
            String clientPort = clientPortEditText.getText().toString(); /// Luăm portul specificat de cleint

            if (clientAddress.isEmpty() || clientPort.isEmpty()) {  /// Dacă unul dintre ele este gol, atunci ieșim din activitate
                Toast.makeText(getApplicationContext(),
                        "[MAIN ACTIVITY] Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) { ///  Dacă Threadul pentru server nu există sau nu este pornit, atunci ieșim din funcție
                Toast.makeText(getApplicationContext(),
                        "[MAIN ACTIVITY] There is no server to connect to!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String word = wordEditText.getText().toString(); /// Luăm cuvântul specificat de către client

            if (word.isEmpty()) { /// Dacă acesta este gol sau nespecificat în câmpul respectiv, ieșim din funcție
                Toast.makeText(getApplicationContext(),
                        "[MAIN ACTIVITY] Word should be filled!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            definitionTextView.setText(Constants.EMPTY_STRING); /// Pentru început, în TextView avem doar un string gol

            ClientThread clientThread = new ClientThread(
                    clientAddress,
                    Integer.parseInt(clientPort),
                    word,
                    definitionTextView
            ); /// Se crează un Thread pentru client cu informațiile necesare

            clientThread.start(); /// Thread-ul client este pornit
        }
    }

    /// Funcția pentru crearea interfeței și legarea cu elementele de grafică din XML
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02v4_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text); /// Portul pentru server
        Button connectButton = (Button) findViewById(R.id.connect_button); /// Butonul pentru conecarea la server
        connectButton.setOnClickListener(connectButtonClickListener); /// Funcționalitatea pentru onCLick

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text); /// Adresa IP a utilizatorului
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text); /// Portul clientului
        wordEditText = (EditText)findViewById(R.id.word_edit_text); /// Cuvântul furnizat de către acesta

        Button getDefinitionButton = (Button) findViewById(R.id.get_definition_button);
        getDefinitionButton.setOnClickListener(getDefinitionButtonClickListener);

        definitionTextView = (TextView)findViewById(R.id.definition_text_view);
    }

    /// Funcția folosită atunci când activitatea este distrusă
    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) { ///  Dacă Threadul pentru server nu este încă oprit
            serverThread.stopThread(); /// Se oprește
        }
        super.onDestroy(); /// Se apelează metoda onDestyoy din superclasă
    }
}
