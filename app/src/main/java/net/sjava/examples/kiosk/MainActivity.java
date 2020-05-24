package net.sjava.examples.kiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
  static final String TAG = "SIMPLE_KIOSK";

  private Button mStartLockButton;
  private Button mEndLockButton;

  // for device admin app
  static final int DEVICE_ADMIN_ADD_REQUEST = 1001;
  private ComponentName mAdminComponentName;
  private DevicePolicyManager mDevicePolicyManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mStartLockButton = findViewById(R.id.startLockButton);
    mEndLockButton = findViewById(R.id.endLockButton);

    mStartLockButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 첫번째 예제 코드
        //LockUtil.lock(MainActivity.this);
        //startLockTask();

        // 두번째 예제 코드
        disableCamera(true);
        setUserRestrictions(true);
        setPackagesSuspended(mSuspendedPackageNames, true);
      }
    });

    mEndLockButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 두번째 예제 코드
        disableCamera(false);
        setUserRestrictions(false);
        setPackagesSuspended(mSuspendedPackageNames, false);

        // 첫 번째 예제 코드
        //LockUtil.unLock(MainActivity.this);
        //stopLockTask();
      }
    });

    mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
    mAdminComponentName = new ComponentName(this, KioskDeviceAdminReceiver.class);

    if (!isDeviceAdminApp()) {
      enableAdmin();
    }

  }

  @Override
  protected void onResume() {
    super.onResume();

    if (LockUtil.shouldLock(this)) {
      startLockTask();
    }

    Log.i(TAG, "isAdminApp: " + isDeviceAdminApp());
    Log.i(TAG, "isOwnerApp: " + isDeviceOwnerApp());
  }


  private boolean isDeviceAdminApp() {
    return mDevicePolicyManager.isAdminActive(mAdminComponentName);
  }

  private boolean isDeviceOwnerApp() {
    return mDevicePolicyManager.isDeviceOwnerApp(getPackageName());
  }

  private void enableAdmin() {
    if (isDeviceAdminApp()) {
      return;
    }
    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminComponentName);
    // Start the add device admin activity
    startActivityForResult(intent, DEVICE_ADMIN_ADD_REQUEST);
  }

  private void disableAdmin() {
    if (!isDeviceAdminApp()) {
      return;
    }
    mDevicePolicyManager.removeActiveAdmin(mAdminComponentName);
  }

  private void disableCamera(boolean disabled) {
    mDevicePolicyManager.setCameraDisabled(mAdminComponentName, disabled);
  }

  private void setPackagesSuspended(String[] packageNames, boolean suspended) {
    if(Build.VERSION.SDK_INT >= 24) {
      mDevicePolicyManager.setPackagesSuspended(mAdminComponentName, packageNames, suspended);
    }
  }

  private void setUserRestrictions(boolean restricted) {
    for (String restriction : mUserRestrictions) {
      if (restricted) {
        mDevicePolicyManager.addUserRestriction(mAdminComponentName, restriction);
      } else {
        mDevicePolicyManager.clearUserRestriction(mAdminComponentName, restriction);
      }
    }
  }

  static final String[] mSuspendedPackageNames = {"com.twitter.android",
      "com.facebook.katana",
      "com.google.android.apps.nbu.files"
  };

  static final ArrayList<String> mUserRestrictions = new ArrayList<>(
      Arrays.asList(
          UserManager.DISALLOW_SMS,
          UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA,
          //UserManager.DISALLOW_USB_FILE_TRANSFER,
          UserManager.DISALLOW_BLUETOOTH
      )
  );




}
