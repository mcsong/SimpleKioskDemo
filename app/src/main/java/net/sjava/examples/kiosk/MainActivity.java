package net.sjava.examples.kiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

  private Button mStartLockButton;
  private Button mEndLockButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mStartLockButton = findViewById(R.id.startLockButton);
    mEndLockButton = findViewById(R.id.endLockButton);

    mStartLockButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LockUtil.lock(MainActivity.this);
        startLockTask();
      }
    });

    mEndLockButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LockUtil.unLock(MainActivity.this);
        stopLockTask();
      }
    });

  }

  @Override
  protected void onResume() {
    super.onResume();

    if(LockUtil.shouldLock(this)) {
      startLockTask();
    }

  }
}
