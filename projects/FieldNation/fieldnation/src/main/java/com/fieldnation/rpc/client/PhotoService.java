package com.fieldnation.rpc.client;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.rpc.common.DataServiceConstants;
import com.fieldnation.rpc.common.PhotoServiceConstants;
import com.fieldnation.rpc.server.DataService;

public class PhotoService implements PhotoServiceConstants {
	private Context _context;
	private ResultReceiver _resultReceiver;

	public PhotoService(Context context, ResultReceiver resultReceiver) {
		_context = context.getApplicationContext();
		_resultReceiver = resultReceiver;
	}

	public Intent getPhoto(int resultCode, String url) {
		Intent intent = new Intent(_context, DataService.class);

		intent.setAction(DataServiceConstants.ACTION_RPC);
		intent.putExtra(DataServiceConstants.KEY_SERVICE, ACTION_NAME);

		intent.putExtra(KEY_RESULT_RECEIVER, _resultReceiver);
		intent.putExtra(KEY_PARAM_URL, url);
		intent.putExtra(KEY_RESULT_CODE, resultCode);

		return intent;
	}
}
