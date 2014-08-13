package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Deliverable;
import com.fieldnation.data.workorder.Document;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

	// Data
	private Deliverable _deliverable;
	private long _profileId;

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
		LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_detail_deliverable, this);

		if (isInEditMode())
			return;

		_fileTypeImageView = (ImageView) findViewById(R.id.filetype_imageview);
		_filenameTextView = (TextView) findViewById(R.id.filename_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
		_usernameTextView = (TextView) findViewById(R.id.username_textview);
		_deleteButton = (ImageButton) findViewById(R.id.delete_imagebutton);

	}

	/*-*************************-*/
	/*-			Methods			-*/
	/*-*************************-*/
	public void setDeliverable(long profileId, Deliverable deliverable) {
		_deliverable = deliverable;
		_profileId = profileId;
		populateUi();
	}

	private void populateUi() {
		if (_deliverable == null)
			return;

		_filenameTextView.setText(_deliverable.getFileName());
		_dateTextView.setText(_deliverable.getUploadedTime() + "");
		_usernameTextView.setText(_deliverable.getUserId() + "");

		if (_profileId == _deliverable.getUserId().intValue()) {
			_deleteButton.setVisibility(View.VISIBLE);
		} else {
			_deleteButton.setVisibility(View.GONE);
		}

	}
	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/

}
