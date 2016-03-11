package com.shanejansen.thelodge.data;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shanejansen.thelodge.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shane Jansen on 3/10/16.
 */
public class DataManager {
    // Constants
    private static final String GET_STATE_ENDPOINT = "/control/get-state.php";
    private static final String SET_STATE_ENDPOINT = "/control/set-state.php";

    public static void refreshDevices(Context context, final NetworkInf<List<Device>> networkInf) {
        Ion.with(context)
                .load(Secret.BASE_URL + GET_STATE_ENDPOINT)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        List<Device> devices = new ArrayList<>();
                        if (result != null) {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject j = result.get(i).getAsJsonObject();
                                devices.add(new Device(j.get("id").getAsString(),
                                        j.get("name").getAsString(),
                                        j.get("type").getAsString(),
                                        j.get("pin").getAsInt(),
                                        j.get("state").getAsString()));
                            }

                        }
                        if (networkInf != null) networkInf.onCompleted(devices);
                    }
                });
    }

    public static void activateDevice(Context context, String deviceId, boolean state,
                                      final NetworkInf<String> networkInf) {
        Ion.with(context)
                .load(Secret.BASE_URL + SET_STATE_ENDPOINT)
                .setStringBody(deviceId + " " + state)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (networkInf != null) networkInf.onCompleted(result);
                    }
                });
    }

    public static void setupGoogleApiClient(Context context, final NetworkInf<GoogleApiClient> networkInf) {
        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                if (networkInf != null) networkInf.onCompleted(googleApiClient);
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(MainActivity.TAG, "onConnectionSuspended: " + i);
            }
        });
        googleApiClient.connect();
    }

    public static void sendWearMessage(final GoogleApiClient googleApiClient, final String path,
                                       final String payload) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodeResult = Wearable.NodeApi
                        .getConnectedNodes(googleApiClient)
                        .await();
                for (Node node: nodeResult.getNodes()) {
                    MessageApi.SendMessageResult messageResult = Wearable.MessageApi
                            .sendMessage(googleApiClient, node.getId(), path, payload.getBytes())
                            .await();
                }
            }
        }).start();
    }

    public interface NetworkInf<T> {
        void onCompleted(T result);
    }
}
