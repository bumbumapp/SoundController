package eu.darken.bluemusic.bluetooth.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BluetoothPermissionTest {
    @Test
    public void connectPermissionNeededFromAndroid12() {
        assertFalse(BluetoothPermission.isRequired(30));
        assertTrue(BluetoothPermission.isRequired(31));
        assertTrue(BluetoothPermission.isRequired(36));
    }
}
