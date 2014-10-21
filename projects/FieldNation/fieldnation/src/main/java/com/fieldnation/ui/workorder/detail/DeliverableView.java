package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Deliverable;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DeliverableView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.DeliverableView";

	// UI
	private ImageView _fileTypeImageView;
	private TextView _filenameTextView;
	private TextView _dateTextView;
	private TextView _usernameTextView;
	private ImageButton _deleteButton;
	private ProgressBar _progressBar;
	private TextView _statusTextView;
	private LinearLayout _usernameLayout;

	// Data
	private Deliverable _doc;
	private long _profileId;
	private Listener _listener;
	private int _loadingCounter = 0;
	private boolean _isLoading = false;

	/*-*****************************-*/
	/*-			Life Cycle			-*/
	/*-*****************************-*/
	public DeliverableView(Context context) {
		super(context);
		init();
	}

	public DeliverableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DeliverableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_document, this);

		if (isInEditMode())
			return;

		_fileTypeImageView = (ImageView) findViewById(R.id.filetype_imageview);
		_filenameTextView = (TextView) findViewById(R.id.filename_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
		_usernameTextView = (TextView) findViewById(R.id.username_textview);
		_deleteButton = (ImageButton) findViewById(R.id.delete_imagebutton);
		_deleteButton.setOnClickListener(_delete_onClick);

		_progressBar = (ProgressBar) findViewById(R.id.progressBar);
		_statusTextView = (TextView) findViewById(R.id.status_textview);
		_usernameLayout = (LinearLayout) findViewById(R.id.username_layout);

		setOnClickListener(_this_onClick);
		setLoading(false, 0);
	}

	/*-*************************-*/
	/*-			Methods			-*/
	/*-*************************-*/

	public void setLoading(boolean isloading, int messageResId) {
		_isLoading = isloading;
		if (isloading) {
			_progressBar.setVisibility(View.VISIBLE);
			_statusTextView.setVisibility(View.VISIBLE);
			_usernameLayout.setVisibility(View.GONE);
			_dateTextView.setVisibility(View.GONE);
			_deleteButton.setVisibility(View.GONE);
			_statusTextView.setText(messageResId);
		} else {
			_progressBar.setVisibility(View.GONE);
			_statusTextView.setVisibility(View.GONE);
			_usernameLayout.setVisibility(View.VISIBLE);
			_dateTextView.setVisibility(View.VISIBLE);
			_deleteButton.setVisibility(View.VISIBLE);
		}
	}

	public void setDeliverable(long profileId, Deliverable deliverable) {
		_doc = deliverable;
		_profileId = profileId;
		populateUi();
	}

	public void setListener(Listener listener) {
		_listener = listener;
	}

	private void populateUi() {
		if (_doc == null)
			return;

		_filenameTextView.setText(_doc.getFileName());
		try {
			_dateTextView.setText(misc.formatDateLong(ISO8601.toCalendar(_doc.getUploadedTime())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		_usernameTextView.setText(_doc.getUploadedBy().getFullName());

		if (_profileId == _doc.getUploaderUserId() && !_isLoading) {
			_deleteButton.setVisibility(View.VISIBLE);
		} else {
			_deleteButton.setVisibility(View.GONE);
		}
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private View.OnClickListener _this_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (_isLoading)
				return;

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(_doc.getStorageSrc()), _doc.getFileType());
			getContext().startActivity(intent);
		}
	};

	private View.OnClickListener _delete_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setLoading(true, R.string.deleting);
			if (_listener != null)
				_listener.onDelete(DeliverableView.this, _doc);
		}
	};

	public interface Listener {
		public void onDelete(DeliverableView v, Deliverable document);
	}
}
