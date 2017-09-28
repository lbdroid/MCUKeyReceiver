package tk.rabidbeaver.mcureceiver;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class KeyConfiguration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_configuration);

        SharedPreferences.Editor spe = getApplicationContext().getSharedPreferences("keyActionStore", Context.MODE_PRIVATE).edit();
        spe.clear();
        spe.putString(Integer.toString(0x09), Integer.toString(Constants.ACTIONTYPES.ACTIVITY_INTENT)+"com.google.android.apps.maps/com.google.android.maps.MapsActivity");
        spe.putString(Integer.toString(0x18), Integer.toString(Constants.ACTIONTYPES.BROADCAST_INTENT)+"tk.rabidbeaver.bd37033controller.VOL_UP");
        spe.putString(Integer.toString(0x19), Integer.toString(Constants.ACTIONTYPES.BROADCAST_INTENT)+"tk.rabidbeaver.bd37033controller.VOL_DOWN");
        spe.putString(Integer.toString(0xa4), Integer.toString(Constants.ACTIONTYPES.BROADCAST_INTENT)+"tk.rabidbeaver.bd37033controller.MUTE");
        spe.putString(Integer.toString(0x1b), Integer.toString(Constants.ACTIONTYPES.KEYCODE)+Integer.toString(0x03));
        spe.putString(Integer.toString(0x1c), Integer.toString(Constants.ACTIONTYPES.KEYCODE)+Integer.toString(0x04));
        spe.putString(Integer.toString(0x25), Integer.toString(Constants.ACTIONTYPES.KEYCODE)+Integer.toString(0xbb));
        spe.commit();

        KeyReceiver.loaded = false;
    }
}
