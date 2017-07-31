package com.rajesh.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by rajeshmaheswaran on 20/07/17.
 */
public class WifiDirectBroadcastReciever extends BroadcastReceiver implements WifiP2pManager.PeerListListener {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;
    Context c;



    WifiP2pManager.PeerListListener myPeerListListner;
    final List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();


    public WifiDirectBroadcastReciever(WifiP2pManager manager, WifiP2pManager.Channel channel, MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
        myPeerListListner = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {

                Collection<WifiP2pDevice> refreshedPeers=peerList.getDeviceList();
                if (!refreshedPeers.equals(peers)) {
                    peers.clear();
                    peers.addAll(refreshedPeers);
                    Log.d("PEER LISTING:", "devices found");
                    //Toast.makeText(Context, peers.toString(), Toast.LENGTH_SHORT).show();
                    // If an AdapterView is backed by this data, notify it
                    // of the change.  For instance, if you have a ListView of
                    // available peers, trigger an update.
                    ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();

                    // Perform any other updates needed based on the new list of
                    // peers connected to the Wi-Fi P2P network.
                }

                if (peers.size() == 0) {
                    Log.d("PEER LISTING:", "No devices found");
                    return;
                }
            }
        };
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context,"Wifi on",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,"Wifi off",Toast.LENGTH_SHORT).show();
            }

            if(mManager!=null){
                mManager.requestPeers(mChannel,myPeerListListner);
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        Toast.makeText(c,peers.toString(),Toast.LENGTH_LONG).show();
    }
}
