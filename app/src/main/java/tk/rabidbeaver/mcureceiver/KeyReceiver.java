package tk.rabidbeaver.mcureceiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class KeyReceiver extends BroadcastReceiver {
    protected static boolean loaded = false;
    private static String[] actions = new String[256];
    private static int[] actionTypes = new int[256];

    private void activity(Context context, String component){
        Intent sIntent = new Intent(Intent.ACTION_MAIN);
        sIntent.setComponent(ComponentName.unflattenFromString(component));
        sIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }

    private void broadcast(Context context, String action){
        Intent sIntent = new Intent();
        sIntent.setAction(action);
        context.sendBroadcast(sIntent);
    }

    private void key(Context context, int keycode){
        Intent buttonServiceIntent;
        switch(keycode){
            case 0x03:
                buttonServiceIntent = new Intent(context, ButtonService.class);
                buttonServiceIntent.setAction("HOME");
                context.startService(buttonServiceIntent);
                break;
            case 0x04:
                buttonServiceIntent = new Intent(context, ButtonService.class);
                buttonServiceIntent.setAction("BACK");
                context.startService(buttonServiceIntent);
                break;
            case 0xbb:
                buttonServiceIntent = new Intent(context, ButtonService.class);
                buttonServiceIntent.setAction("RECENT");
                context.startService(buttonServiceIntent);
                break;
            default:
                // Ideally, don't use this. It is slow.
                String cmd = "input keyevent "+keycode;
                Command command = new Command(0, cmd);
                try {
                    RootShell.getShell(true).add(command);
                } catch (IOException | TimeoutException | RootDeniedException e){
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent){

        if (intent.getAction().contentEquals("tk.rabidbeaver.maincontroller.STANDBY")){
            String cmd = "input keyevent 26";
            Command command = new Command(0, cmd);
            try {
                RootShell.getShell(true).add(command);
            } catch (IOException | TimeoutException | RootDeniedException e){
                e.printStackTrace();
            }
            return;
        }

        if (!loaded){
            SharedPreferences keyActionStore = context.getSharedPreferences("keyActionStore", Context.MODE_PRIVATE);
            for (int i=0; i<256; i++){
                actionTypes[i] = Constants.ACTIONTYPES.NULL;
                if (keyActionStore.contains(Integer.toString(i))){
                    String keyString = keyActionStore.getString(Integer.toString(i), null);
                    if (keyString != null){
                        actions[i] = keyString.substring(1);
                        actionTypes[i] = Integer.parseInt(keyString.substring(0,1));
                    }
                }
            }
            loaded = true;
        }

        if (intent.hasExtra("KEY")){
            int keycode = intent.getIntExtra("KEY", -1);

            if (keycode >= 0 && keycode < 256){
                switch(actionTypes[keycode]){
                    case Constants.ACTIONTYPES.BROADCAST_INTENT:
                        broadcast(context, actions[keycode]);
                        break;
                    case Constants.ACTIONTYPES.ACTIVITY_INTENT:
                        activity(context, actions[keycode]);
                        break;
                    case Constants.ACTIONTYPES.KEYCODE:
                        key(context, Integer.parseInt(actions[keycode]));
                        break;
                    default:
                        key(context, keycode);
                }
            }
        }
    }
}
