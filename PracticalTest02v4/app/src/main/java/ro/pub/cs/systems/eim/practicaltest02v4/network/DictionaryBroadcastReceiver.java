package ro.pub.cs.systems.eim.practicaltest02v4.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import ro.pub.cs.systems.eim.practicaltest02v4.general.Constants;

public class DictionaryBroadcastReceiver extends BroadcastReceiver{

    private final TextView textView;

    /// Constructor
    public DictionaryBroadcastReceiver(TextView textView) {
        this.textView = textView;
    }

    ///  Funcția pentru afișarea în TextView a textului primit pe canal
    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra(Constants.BROADCAST_EXTRA);
        textView.setText(data);
    }
}
