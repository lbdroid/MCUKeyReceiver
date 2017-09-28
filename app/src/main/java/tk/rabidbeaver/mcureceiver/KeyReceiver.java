package tk.rabidbeaver.mcureceiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class KeyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Intent sIntent = new Intent();
        if (intent.hasExtra("KEY")){
            int keycode = intent.getIntExtra("KEY", -1);
            int keyout = -1;
            switch (keycode){
                case 0x09:
                    // Google Maps
                    sIntent = new Intent(Intent.ACTION_MAIN);
                    sIntent.setComponent(ComponentName.unflattenFromString("com.google.android.apps.maps/com.google.android.maps.MapsActivity"));
                    sIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(sIntent);
                    return;
                case 0x18:
                    // Vol+
                    sIntent.setAction("tk.rabidbeaver.bd37033controller.VOL_UP");
                    break;
                case 0x19:
                    // Vol-
                    sIntent.setAction("tk.rabidbeaver.bd37033controller.VOL_DOWN");
                    break;
                case 0xa4:
                    // Mute
                    sIntent.setAction("tk.rabidbeaver.bd37033controller.MUTE");
                    break;
                case 0x1b:
                    // HOME
                    keyout = 0x03;
                    break;
                case 0x1c:
                    // BACK
                    keyout = 0x04;
                    break;
                case 0x25:
                    // RECENT
                    keyout = 0xbb;
                    break;
                case -1:
                    return;
                default:
                    keyout = keycode;
                    break;
            }
            if (keyout > 0 && RootShell.isRootAvailable() && RootShell.isAccessGiven()){
                String cmd = "input keyevent "+keyout;
                Command command = new Command(0, cmd);
                try {
                    RootShell.getShell(true).add(command);
                } catch (IOException | TimeoutException | RootDeniedException e){
                    e.printStackTrace();
                }
            } else {
                context.sendBroadcast(sIntent);
            }
        }
    }
}
