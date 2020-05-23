package net.sjava.examples.kiosk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
  static final String TAG = "SIMPLE_KIOSK";

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    Log.i(TAG, "onReceive: " + action);
    if(Intent.ACTION_BOOT_COMPLETED.equals(action)) {
        startActivity(context);
    }
  }

  static void startActivity(Context context) {
    Intent i = new Intent(context, MainActivity.class);
    // For Android 9 and below
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(i);
      return;
    }

    // Android 10 ?
  }

}
