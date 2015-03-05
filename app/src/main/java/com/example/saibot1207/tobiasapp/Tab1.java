package com.example.saibot1207.tobiasapp;

/**
 * Created by saibot1207 on 23.02.15.
 */
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.app.Activity;

import java.util.Set;


public class Tab1 extends Activity {

    private enum BluetoothError {
        NO_ADAPTER,
        NOT_ENABLED,
        NO_DEVICE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment settingsFragment = new SettingsFragment();
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, settingsFragment)
                .commit();


        settingsFragment.getFragmentManager().executePendingTransactions();
        final DynamicListPreference btDevices = (DynamicListPreference) settingsFragment.findPreference("settingsBluetooth");
        setAvailableBTDevicesOnList(btDevices);
        btDevices.setOnClickListener(new DynamicListPreference.DynamicListPreferenceOnClickListener() {
            @Override
            public void onClick(DynamicListPreference preference) {
                setAvailableBTDevicesOnList(btDevices);
            }
        });
    }

    private static void setAvailableBTDevicesOnList(DynamicListPreference listPreference) {
        BluetoothError error = null;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String[] names = null;
        String[] values = null;
        if(bluetoothAdapter == null) {
            error = BluetoothError.NO_ADAPTER;
        } else if(!bluetoothAdapter.isEnabled()) {
            error = BluetoothError.NOT_ENABLED;
        } else {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() == 0) {
                error = BluetoothError.NO_DEVICE;
            } else {
                BluetoothDevice[] devices = new BluetoothDevice[pairedDevices.size()];
                devices = pairedDevices.toArray(devices);
                names = new String[devices.length];
                values = new String[devices.length];
                for (int i = 0; i < devices.length; i++) {
                    names[i] = devices[i].getName();
                    values[i] = devices[i].getName();
                }
            }
        }
        if(error != null) {
            switch(error) {
                case NO_ADAPTER:
                    listPreference.setDialogMessage(R.string.noAdapterBluetooth);
                    break;
                case NOT_ENABLED:
                    listPreference.setDialogMessage(R.string.disabledBluetooth);
                    break;
                case NO_DEVICE:
                    listPreference.setDialogMessage(R.string.noDeviceBluetooth);
                    break;
            }
            names = new String[]{};
            values = new String[]{};
        }
        listPreference.setEntries(names);
        listPreference.setEntryValues(values);
        listPreference.setDefaultValue("");
    }
}