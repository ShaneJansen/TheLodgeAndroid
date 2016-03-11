package com.shanejansen.thelodge.data;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;
import com.shanejansen.thelodge.view.MainActivity;
import com.shanejansen.thelodge.view.MainFragment;

import java.util.List;

/**
 * Created by Shane Jansen on 3/10/16.
 */
public class WearMessageListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(MainActivity.TAG, "message received");
        switch (messageEvent.getPath()) {
            case MainFragment.WEAR_GET_STATE:
                getState();
                break;
            case MainFragment.WEAR_SET_STATE:
                String[] dataChunks = new String(messageEvent.getData()).split(" ");
                String deviceId = dataChunks[0];
                boolean state = Boolean.parseBoolean(dataChunks[1]);
                setState(deviceId, state);
                break;
        }
    }

    private void getState() {
        DataManager.refreshDevices(this, new DataManager.NetworkInf<List<Device>>() {
            @Override
            public void onCompleted(final List<Device> devices) {
                // Devices refreshed
                DataManager.setupGoogleApiClient(WearMessageListenerService.this,
                        new DataManager.NetworkInf<GoogleApiClient>() {
                            @Override
                            public void onCompleted(GoogleApiClient googleApiClient) {
                                // Google API connected
                                Gson gson = new Gson();
                                String json = gson.toJson(devices);
                                DataManager.sendWearMessage(googleApiClient, MainFragment.WEAR_GET_STATE,
                                        json);
                            }
                        });
            }
        });
    }

    private void setState(String deviceId, boolean state) {
        DataManager.activateDevice(this, deviceId, state, new DataManager.NetworkInf<String>() {
            @Override
            public void onCompleted(String result) {
                // State set
                DataManager.setupGoogleApiClient(WearMessageListenerService.this,
                        new DataManager.NetworkInf<GoogleApiClient>() {
                            @Override
                            public void onCompleted(GoogleApiClient googleApiClient) {
                                // Google API connected
                                DataManager.sendWearMessage(googleApiClient, MainFragment.WEAR_SET_STATE,
                                        "done");
                            }
                        });
            }
        });
    }
}
