package com.fieldnation.rpc.webclient;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.rpc.common.PhotoServiceConstants;
import com.fieldnation.rpc.common.RpcServiceConstants;

class PhotoWebService implements PhotoServiceConstants {
    private Context _context;
    private ResultReceiver _resultReceiver;

    public PhotoWebService(Context context, ResultReceiver resultReceiver) {
        _context = context.getApplicationContext();
        _resultReceiver = resultReceiver;
    }

/*
    public Intent getPhoto(int resultCode, String url, boolean getCircle) {
        Intent intent = null; //new Intent(_context, RpcService.class);

        intent.setAction(RpcServiceConstants.ACTION_RPC);
        intent.putExtra(RpcServiceConstants.KEY_SERVICE, ACTION_NAME);

        intent.putExtra(KEY_RESULT_RECEIVER, _resultReceiver);
        intent.putExtra(KEY_PARAM_URL, url);
        intent.putExtra(KEY_RESULT_CODE, resultCode);
        intent.putExtra(KEY_GET_CIRCLE, getCircle);

        return intent;
    }
*/
}
