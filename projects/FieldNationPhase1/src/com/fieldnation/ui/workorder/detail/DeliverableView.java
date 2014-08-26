package com.fieldnation.ui.workorder.detail;

import java.text.ParseException;

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
	private Listener _listener;

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
		_deleteButton.setOnClickListener(_delete_onClick);

		setOnClickListener(_this_onClick);
	}

	/*-*************************-*/
	/*-			Methods			-*/
	/*-*************************-*/
	public void setDeliverable(long profileId, Deliverable deliverable) {
		_deliverable = deliverable;
		_profileId = profileId;
		populateUi();
	}

	public void setListener(Listener listener) {
		_listener = listener;
	}

	private void populateUi() {
		if (_deliverable == null)
			return;

		_filenameTextView.setText(_deliverable.getFileName());
		try {
			_dateTextView.setText(misc.formatDateLong(ISO8601.toCalendar(_deliverable.getUploadedTime())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		_usernameTextView.setText(_deliverable.getUploadedBy().getFirstName() + " " + _deliverable.getUploadedBy().getLastname());

		if (_profileId == _deliverable.getUserId().intValue()) {
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
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(_deliverable.getStorageSrc()), _deliverable.getFileType());
			getContext().startActivity(intent);
		}
	};

	private View.OnClickListener _delete_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (_listener != null)
				_listener.onDelete(_deliverable);
		}
	};

	public interface Listener {
		public void onDelete(Deliverable deliverable);
	}
}
