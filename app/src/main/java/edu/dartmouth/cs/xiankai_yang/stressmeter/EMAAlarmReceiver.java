package edu.dartmouth.cs.xiankai_yang.stressmeter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EMAAlarmReceiver extends BroadcastReceiver {
    //Receive broadcast
    @Override
    public void onReceive(final Context context, Intent intent) {
        startPSM(context);
    }

    // start the stress meter
    private void startPSM(Context context) {
        Intent emaIntent = new Intent(context, MainActivity.class); //The activity you want to start.
        emaIntent.putExtra("alarm", true);
        emaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(emaIntent);
    }
}