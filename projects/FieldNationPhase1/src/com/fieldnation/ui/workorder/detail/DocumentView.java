package com.fieldnation.ui.workorder.detail;

import java.text.ParseException;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Deliverable;
import com.fieldnation.data.workorder.Document;
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

public class DocumentView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.DocumentView";

	// UI
	private ImageView _fileTypeImageView;
	private TextView _filenameTextView;
	private TextView _dateTextView;
	private TextView _usernameTextView;
	private ImageButton _deleteButton;

	// Data
	private Document _document;

	/*-*****************************-*/
	/*-			Life Cycle			-*/
	/*-*****************************-*/
	public DocumentView(Context context) {
		super(context);
		init();
	}

	public DocumentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DocumentView(Context context, AttributeSet attrs, int defStyle) {
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
		_deleteButton.setVisibility(GONE);

		setOnClickListener(_this_onClick);
	}

	/*-*************************-*/
	/*-			Methods			-*/
	/*-*************************-*/
	public void setDocument(Document document) {
		_document = document;
		populateUi();
	}

	private void populateUi() {
		if (_document == null)
			return;

		_filenameTextView.setText(_document.getFileName());
		try {
			_dateTextView.setText(misc.formatDateLong(ISO8601.toCalendar(_document.getLastUpdated())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		_usernameTextView.setText(_document.getUpdatedBy().getFirstname() + " " + _document.getUpdatedBy().getLastname());

	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private View.OnClickListener _this_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(_document.getFilePath()), _document.getFileType());
				getContext().startActivity(intent);
			} catch (Exception ex) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_document.getFilePath()));
					getContext().startActivity(intent);
				} catch (Exception ex1) {
					ex1.printStackTrace();
				}
			}
		}
	};
}
