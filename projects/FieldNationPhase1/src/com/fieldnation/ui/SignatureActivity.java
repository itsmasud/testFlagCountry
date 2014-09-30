package com.fieldnation.ui;

import com.fieldnation.R;

import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/*-
 * 
 * Based on code from:
 * http://www.intertech.com/Blog/android-gestureoverlayview-to-capture-a-quick-signature-or-drawing/
 */

public class SignatureActivity extends ActionBarActivity {
	private static final String TAG = "ui.SignatureActivity";

	public static final String RESULT_KEY_BITMAP = "com.fieldnation.ui.SignatureActivity:SIGNATURE";

	// UI
	private SignatureView _sigView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signature);

		_sigView = (SignatureView) findViewById(R.id.sig_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.signature, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.done_menuitem:
			onDone();
			break;
		case R.id.clear_menuitem:
			_sigView.clear();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onDone() {
		Bitmap sig = _sigView.getSignature();

		Intent intent = new Intent();
		intent.putExtra(RESULT_KEY_BITMAP, sig);
		setResult(RESULT_OK, intent);
		finish();
	}
}
