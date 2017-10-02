package tk.rabidbeaver.mcureceiver;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class ButtonService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

    @Override
    public void onInterrupt() {}

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ButtonService", "started");

        boolean result = false;
        String act = intent.getAction();
        switch (act){
            case "BACK":
                result = performGlobalAction(GLOBAL_ACTION_BACK);
                break;
            case "HOME":
                result = performGlobalAction(GLOBAL_ACTION_HOME);
                break;
            case "RECENT":
                result = performGlobalAction(GLOBAL_ACTION_RECENTS);
                break;
        }

        if (!result) Log.d("ButtonService", "result is FALSE");

        return Service.START_STICKY;
    }
}
