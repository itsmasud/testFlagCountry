package com.fieldnation.rpc.server;

import android.app.IntentService;
import android.content.Intent;

import com.fieldnation.rpc.common.RpcServiceConstants;

import java.util.HashMap;

public class RpcService extends IntentService implements RpcServiceConstants {
    private static final String TAG = "rpc.server.RpcService";

    private HashMap<String, RpcInterface> _rpcs = new HashMap<>();

    public RpcService() {
        super(TAG);

        // fill in the hashmap
        new AuthRpc(_rpcs);
        new WebRpc(_rpcs);
        new PhotoRpc(_rpcs);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if (ACTION_RPC.equals(action)) {
            _rpcs.get(intent.getStringExtra(KEY_SERVICE)).execute(this, intent);
        }
    }

}
