package com.example.a2dpcontroller;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

public class BluetoothA2DPRequester implements BluetoothProfile.ServiceListener {

    private Callback mCallback;

    public BluetoothA2DPRequester(Callback callback) {
        mCallback = callback;
    }

    public void request (Context c, BluetoothAdapter adapter) {
        adapter.getProfileProxy(c, this, BluetoothProfile.A2DP);
    }

    @Override
    public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
        CodecController.setA2dp((BluetoothA2dp)bluetoothProfile);
    }

    @Override
    public void onServiceDisconnected(int i) {
        mCallback.onBluetoothDisconnected();
    }

    public static interface Callback {
        public void onBluetoothDisconnected();
    }
}
