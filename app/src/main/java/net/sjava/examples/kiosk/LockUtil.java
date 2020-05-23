package net.sjava.examples.kiosk;

import android.content.Context;
import android.content.SharedPreferences;

public class LockUtil {
  static final String LOCK_KEY = "LOCK";

  public static boolean shouldLock(Context context) {
    SharedPreferences sharedpreferences = getSharedPreferences(context);
    return sharedpreferences.getBoolean(LOCK_KEY, false);
  }

  public static void lock(Context context) {
    setLock(context, true);
  }

  public static void unLock(Context context) {
    setLock(context, false);
  }

  static void setLock(Context context, boolean lock) {
    SharedPreferences sharedpreferences = getSharedPreferences(context);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putBoolean(LOCK_KEY, lock);
    // 동기화를 위해서 commit()을 쓴다.
    editor.commit();
  }

  static SharedPreferences getSharedPreferences(Context context) {
    return context.getSharedPreferences("KioskLock", Context.MODE_PRIVATE);
  }
}
