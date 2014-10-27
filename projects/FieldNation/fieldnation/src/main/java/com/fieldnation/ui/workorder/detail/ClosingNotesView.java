package com.fieldnation.ui.workorder.detail;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.dialog.ClosingNotesDialog;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;

public class ClosingNotesView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ClosingNotesView";

	private static final int WEB_SAVE_NOTES = 1;

	// UI
	private TextView _notesTextView;
	private LinearLayout _editLayout;
	private ImageView _editImageView;
	private ClosingNotesDialog _dialog;
	private ProgressBar _progressBar;

	// Data
	private GlobalState _gs;
	private Workorder _workorder;
	private WorkorderService _service = null;
    private Integer[] woStatus = { 5, 6, 7 }; //work order status approved, paid, canceled

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public ClosingNotesView(Context context) {
		this(context, null);
	}

	public ClosingNotesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_wd_closing_notes, this);

		if (isInEditMode())
			return;

		_gs = (GlobalState) context.getApplicationContext();

		_editLayout = (LinearLayout) findViewById(R.id.edit_layout);
		_editLayout.setOnClickListener(_notes_onClick);
		_editImageView = (ImageView) findViewById(R.id.edit_imageview);

		_notesTextView = (TextView) findViewById(R.id.notes_textview);
		_notesTextView.setOnClickListener(_notes_onClick);

		_progressBar = (ProgressBar) findViewById(R.id.progressBar);

		_dialog = new ClosingNotesDialog(getContext());

		setLoading(true);
		_gs.requestAuthentication(_authClient);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private ClosingNotesDialog.Listener _closingNotes_listener = new ClosingNotesDialog.Listener() {
		@Override
		public void onOk(String message) {
			_notesTextView.setText(message);
			setLoading(true);
			_gs.startService(_service.closingNotes(WEB_SAVE_NOTES, _workorder.getWorkorderId(), message));
		}

		@Override
		public void onCancel() {
		}
	};
	private View.OnClickListener _notes_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (_service != null && !Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
				_dialog.show(_workorder.getClosingNotes(), _closingNotes_listener);
			}
		}
	};

	private AuthenticationClient _authClient = new AuthenticationClient() {
		@Override
		public void onAuthenticationFailed(Exception ex) {
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			}
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(getContext(), username, authToken, _resultReceiver);
			setLoading(false);
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {
		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			setLoading(false);
			_workorder.dispatchOnChange();
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			}
			_gs.requestAuthentication(_authClient);
		}
	};

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		refresh();
	}

	private void refresh() {
		if (!misc.isEmptyOrNull(_workorder.getClosingNotes())) {
			_notesTextView.setText(_workorder.getClosingNotes());
		}
	}

	private void setLoading(boolean isLoading) {
		if (isLoading) {
			_editImageView.setVisibility(View.GONE);
			_progressBar.setVisibility(View.VISIBLE);
		} else {
			_editImageView.setVisibility(View.VISIBLE);
			_progressBar.setVisibility(View.GONE);
		}
	}
}
