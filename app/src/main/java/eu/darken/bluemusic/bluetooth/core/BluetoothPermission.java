package eu.darken.bluemusic.bluetooth.core;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

/** Android 12+ needs the runtime BLUETOOTH_CONNECT permission for paired devices and connection events. */
public final class BluetoothPermission {

    private BluetoothPermission() {
    }

    public static boolean isRequired(int sdkInt) {
        return sdkInt >= Build.VERSION_CODES.S;
    }

    public static boolean has(Context context) {
        return !isRequired(Build.VERSION.SDK_INT)
                || ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
                == PackageManager.PERMISSION_GRANTED;
    }
}
