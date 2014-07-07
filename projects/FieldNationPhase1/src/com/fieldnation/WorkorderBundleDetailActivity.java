package com.fieldnation;

import android.content.Intent;
import android.os.Bundle;

public class WorkorderBundleDetailActivity extends BaseActivity {
	private static final String TAG = "WorkorderBundleDetailActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bundle_detail);

		Intent intent = getIntent();

		if (intent == null) {
			finish();
			return;
		}

		// try {
		// // TODO, need to figure out how to get bundles
		// } catch (ParseException e) {
		// e.printStackTrace();
		// finish();
		// }
	}

}
